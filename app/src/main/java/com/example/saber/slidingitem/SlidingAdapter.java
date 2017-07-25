package com.example.saber.slidingitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by saber on 2017/7/25.
 */

public class SlidingAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private OnClickListenerEditOrDelete onClickListenerEditOrDelete;

    public SlidingAdapter(List<String> list, Context context, OnClickListenerEditOrDelete onClickListenerEditOrDelete) {
        this.list = list;
        this.context = context;
        this.onClickListenerEditOrDelete = onClickListenerEditOrDelete;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        String content = list.get(position);
        ViewHolder holder = null;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_title);
            holder.iv=(ImageView)view.findViewById(R.id.iv);
            holder.tvDelete=(TextView)view.findViewById(R.id.tv_delete);
            holder.tvEdit=(TextView)view.findViewById(R.id.tv_edit);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvName.setText(content);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListenerEditOrDelete!=null){
                    onClickListenerEditOrDelete.OnClickListenerDelete(position);
                }
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListenerEditOrDelete!=null){
                    onClickListenerEditOrDelete.OnClickListenerEdit(position);
                }
            }
        });


        return view;
    }

    private class ViewHolder{
        TextView tvName,tvEdit,tvDelete;
        ImageView iv;
    }


    public void setOnClickListenerEditOrDelete(OnClickListenerEditOrDelete onClickListenerEditOrDelete1){
        this.onClickListenerEditOrDelete=onClickListenerEditOrDelete1;
    }

    public interface OnClickListenerEditOrDelete{
        void OnClickListenerEdit(int position);
        void OnClickListenerDelete(int position);
    }
}
