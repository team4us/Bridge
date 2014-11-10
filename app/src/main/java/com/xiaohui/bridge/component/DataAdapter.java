package com.xiaohui.bridge.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xiaohui.bridge.util.ListUtil;

import java.util.List;

/**
 * Created by xhChen on 14-3-1.
 */
public class DataAdapter<T> extends BaseAdapter {

    private List<T> list;
    private LayoutInflater inflater;
    private IViewCreator<T> creator;

    public DataAdapter(Context context, IViewCreator<T> creator) {
        inflater = LayoutInflater.from(context);
        this.creator = creator;
    }

    protected LayoutInflater getLayoutInflater() {
        return inflater;
    }

    public void setContent(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return ListUtil.sizeOfList(list);
    }

    @Override
    public T getItem(int position) {
        if (position >= getCount())
            return null;
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            holder = new ViewHolder();
            view = creator.createView(getLayoutInflater(), parent);
            view.setTag(holder);
            holder.view = view;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        creator.bindDataToView(holder.view, getItem(position), position);
        return view;
    }

    public interface IViewCreator<T> {
        public View createView(LayoutInflater inflater, ViewGroup parent);

        public void bindDataToView(View view, T data, int position);
    }

    private static class ViewHolder {
        public View view;
    }
}
