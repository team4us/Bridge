package com.xiaohui.bridge.business;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;
import com.xiaohui.bridge.business.request.AbstractRequest;
import com.xiaohui.bridge.business.response.IResponse;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by xiaohui on 14/11/18.
 */
public class RequestManager {
    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 100;
    /**
     * 获取连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 10000;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 20;
    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 30000;
    /**
     * 读取超时时间
     */
    public final static int READ_TIMEOUT = 30000;

    private RequestQueue requestQueue;

    public RequestManager() {
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            HttpStack stack = new HttpClientStack(initHttpClient());
            Network network = new BasicNetwork(stack);
            requestQueue = new RequestQueue(new NoCache(), network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public void request(AbstractRequest<? extends IResponse> request) {
        if (request.prePostOperation()) {
            addToRequestQueue(request);
        }
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    private HttpClient initHttpClient() {

        HttpParams httpParams = new BasicHttpParams();
        // 设置最大连接数
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
        // 设置获取连接的最大等待时间
        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
        // 设置每个路由最大连接数
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        // 设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        // 设置读取超时时间
        HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        T4ClientConnManager connectionManager = new T4ClientConnManager(httpParams, schReg);
        return new DefaultHttpClient(connectionManager, httpParams) {
            @Override
            protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
                return new T4KeepAliveStrategy();
            }

            @Override
            protected ConnectionReuseStrategy createConnectionReuseStrategy() {
                return new T4ConnectionReuseStrategy();
            }
        };
    }

    private static class T4KeepAliveStrategy implements
            ConnectionKeepAliveStrategy {
        private static final int KEEP_ALIVE_DURATION_SECS = 15;

        public long getKeepAliveDuration(HttpResponse response,
                                         HttpContext context) {
            // Keep-alive for the shorter of 20 seconds or what the server
            // specifies.
            long timeout = KEEP_ALIVE_DURATION_SECS * 1000;

            HeaderElementIterator i = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (i.hasNext()) {
                HeaderElement element = i.nextElement();
                String name = element.getName();
                String value = element.getValue();
                if (value != null && name.equalsIgnoreCase("timeout")) {
                    try {
                        timeout = Math.min(timeout,
                                Long.parseLong(value) * 1000);
                    } catch (NumberFormatException e) {
                    }
                }
            }

            return timeout;
        }
    }

    private static class T4ConnectionReuseStrategy extends
            DefaultConnectionReuseStrategy {

        /**
         * Implements a patch out in 4.1.x and 4.2 that isn't available in 4.0.x
         * which fixes a bug where connections aren't reused when the response
         * is gzipped. See https://issues.apache.org/jira/browse/HTTPCORE-257
         * for info about the issue, and
         * http://svn.apache.org/viewvc?view=revision&revision=1124215 for the
         * patch which is copied here.
         */
        @Override
        public boolean keepAlive(final HttpResponse response,
                                 final HttpContext context) {
            if (response == null) {
                throw new IllegalArgumentException(
                        "HTTP response may not be null.");
            }
            if (context == null) {
                throw new IllegalArgumentException(
                        "HTTP context may not be null.");
            }

            // Check for a self-terminating entity. If the end of the entity
            // will
            // be indicated by closing the connection, there is no keep-alive.
            ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
            Header teh = response.getFirstHeader(HTTP.TRANSFER_ENCODING);
            if (teh != null) {
                if (!HTTP.CHUNK_CODING.equalsIgnoreCase(teh.getValue())) {
                    return false;
                }
            } else {
                Header[] clhs = response.getHeaders(HTTP.CONTENT_LEN);
                // Do not reuse if not properly content-length delimited
                if (clhs == null || clhs.length != 1) {
                    return false;
                }
                Header clh = clhs[0];
                try {
                    int contentLen = Integer.parseInt(clh.getValue());
                    if (contentLen < 0) {
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    return false;
                }
            }

            // Check for the "Connection" header. If that is absent, check for
            // the "Proxy-Connection" header. The latter is an unspecified and
            // broken but unfortunately common extension of HTTP.
            HeaderIterator hit = response.headerIterator(HTTP.CONN_DIRECTIVE);
            if (!hit.hasNext())
                hit = response.headerIterator("Proxy-Connection");

            // Experimental usage of the "Connection" header in HTTP/1.0 is
            // documented in RFC 2068, section 19.7.1. A token "keep-alive" is
            // used to indicate that the connection should be persistent.
            // Note that the final specification of HTTP/1.1 in RFC 2616 does
            // not
            // include this information. Neither is the "Connection" header
            // mentioned in RFC 1945, which informally describes HTTP/1.0.
            //
            // RFC 2616 specifies "close" as the only connection token with a
            // specific meaning: it disables persistent connections.
            //
            // The "Proxy-Connection" header is not formally specified anywhere,
            // but is commonly used to carry one token, "close" or "keep-alive".
            // The "Connection" header, on the other hand, is defined as a
            // sequence of tokens, where each token is a header name, and the
            // token "close" has the above-mentioned additional meaning.
            //
            // To get through this mess, we treat the "Proxy-Connection" header
            // in exactly the same way as the "Connection" header, but only if
            // the latter is missing. We scan the sequence of tokens for both
            // "close" and "keep-alive". As "close" is specified by RFC 2068,
            // it takes precedence and indicates a non-persistent connection.
            // If there is no "close" but a "keep-alive", we take the hint.

            if (hit.hasNext()) {
                try {
                    TokenIterator ti = createTokenIterator(hit);
                    boolean keepalive = false;
                    while (ti.hasNext()) {
                        final String token = ti.nextToken();
                        if (HTTP.CONN_CLOSE.equalsIgnoreCase(token)) {
                            return false;
                        } else if (HTTP.CONN_KEEP_ALIVE.equalsIgnoreCase(token)) {
                            // continue the loop, there may be a "close"
                            // afterwards
                            keepalive = true;
                        }
                    }
                    if (keepalive)
                        return true;
                    // neither "close" nor "keep-alive", use default policy

                } catch (ParseException px) {
                    // invalid connection header means no persistent connection
                    // we don't have logging in HttpCore, so the exception is
                    // lost
                    return false;
                }
            }

            // default since HTTP/1.1 is persistent, before it was
            // non-persistent
            return !ver.lessEquals(HttpVersion.HTTP_1_0);
        }
    }

    private static class T4ClientConnManager extends
            ThreadSafeClientConnManager {
        private static final int KEEP_ALIVE_DURATION_SECS = 15;
        private static final int KEEP_ALIVE_MONITOR_INTERVAL_SECS = 5;

        public T4ClientConnManager(HttpParams params, SchemeRegistry schreg) {
            super(params, schreg);
        }

        @Override
        public ClientConnectionRequest requestConnection(HttpRoute route,
                                                         Object state) {
            IdleConnectionCloserThread.ensureRunning(this,
                    KEEP_ALIVE_DURATION_SECS, KEEP_ALIVE_MONITOR_INTERVAL_SECS);
            return super.requestConnection(route, state);
        }
    }

    private static class IdleConnectionCloserThread extends Thread {
        private static IdleConnectionCloserThread thread = null;
        private final T4ClientConnManager manager;
        private final int idleTimeoutSeconds;
        private final int checkIntervalMs;

        public IdleConnectionCloserThread(T4ClientConnManager manager,
                                          int idleTimeoutSeconds, int checkIntervalSeconds) {
            super();
            this.manager = manager;
            this.idleTimeoutSeconds = idleTimeoutSeconds;
            this.checkIntervalMs = checkIntervalSeconds * 1000;
        }

        public static synchronized void ensureRunning(
                T4ClientConnManager manager, int idleTimeoutSeconds,
                int checkIntervalSeconds) {
            if (thread == null) {
                thread = new IdleConnectionCloserThread(manager,
                        idleTimeoutSeconds, checkIntervalSeconds);
                thread.start();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (this) {
                        wait(checkIntervalMs);
                    }
                    manager.closeExpiredConnections();
                    manager.closeIdleConnections(idleTimeoutSeconds,
                            TimeUnit.SECONDS);
                    synchronized (IdleConnectionCloserThread.class) {
                        if (manager.getConnectionsInPool() == 0) {
                            thread = null;
                            return;
                        }
                    }
                }
            } catch (InterruptedException e) {
                thread = null;
            }
        }
    }

}
