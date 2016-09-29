package com.example.john.daygram;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ABAdapter extends BaseAdapter {
    private static final int TYPE_A = 0;            //代表有数据，显示表格
    private static final int TYPE_B = 1;            //代表没数据,显示黑点
    private static final int TYPE_C = 2;            //代表没数据，显示红点

    private Context context;

    //整合数据
    private List<Data> data = new ArrayList<>();


    public ABAdapter(Context context, ArrayList<Data> as) {
        this.context = context;
        //把数据装载同一个list里面
        data.addAll(as);
    }


    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if (!data.get(position).getDetail().equals("")) {
            result = TYPE_A;
        } else if (data.get(position).getDetail().equals("")) {
            if(data.get(position).getWeek().equals("SUNDAY")){
                result = TYPE_C;
            }
            else{
                result = TYPE_B;
            }
        }
        return result;
    }

    //获得有多少中view type
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Data getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建两种不同种类的viewHolder变量
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        //根据position获得View的type
        int type = getItemViewType(position);
        if (convertView == null) {
            //实例化
            holder1 = new ViewHolder1();
            holder2 = new ViewHolder2();
            holder3 = new ViewHolder3();
            //根据不同的type 来inflate不同的item layout
            //然后设置不同的tag
            //这里的tag设置是用的资源ID作为Key
            switch (type) {
                case TYPE_A:
                    convertView = View.inflate(context, R.layout.table, null);
                    //设置布局宽度和高度
                    AbsListView.LayoutParams param = new AbsListView.LayoutParams(-1,130);
                    convertView.setLayoutParams(param);
                    holder1.week = (TextView) convertView.findViewById(R.id.week);
                    holder1.day = (TextView) convertView.findViewById(R.id.day);
                    holder1.detail = (TextView) convertView.findViewById(R.id.detail);
                    convertView.setTag(R.id.tag_first, holder1);
                    break;
                case TYPE_B:
                    convertView = View.inflate(context, R.layout.dot_black, null);
                    AbsListView.LayoutParams param1 = new AbsListView.LayoutParams(-1,100);
                    convertView.setLayoutParams(param1);
                    holder2.dot_black = (ImageView) convertView.findViewById(R.id.dot_black);
                    convertView.setTag(R.id.tag_second, holder2);
                    break;
                case TYPE_C:
                    convertView = View.inflate(context, R.layout.dot_red, null);
                    AbsListView.LayoutParams param2 = new AbsListView.LayoutParams(-1,100);
                    convertView.setLayoutParams(param2);
                    holder3.dot_red = (ImageView) convertView.findViewById(R.id.dot_red);
                    convertView.setTag(R.id.tag_third, holder3);
                    break;
            }
        } else {
            //根据不同的type来获得tag
            switch (type) {
                case TYPE_A:
                    holder1 = (ViewHolder1) convertView.getTag(R.id.tag_first);
                    break;
                case TYPE_B:
                    holder2 = (ViewHolder2) convertView.getTag(R.id.tag_second);
                    break;
                case TYPE_C:
                    holder3 = (ViewHolder3) convertView.getTag(R.id.tag_third);
                    break;
            }
        }

        Data o = data.get(position);

        //根据不同的type设置数据
        switch (type) {
            case TYPE_A:
                Data a = o;
                holder1.week.setTextColor(Color.BLACK);
                holder1.week.setText(a.getWeek().substring(0,3));
                if(a.getWeek().equals("SUNDAY")){
                    holder1.day.setTextColor(Color.RED);
                    holder1.day.setText(a.getDay());
                }else{
                    holder1.day.setTextColor(Color.BLACK);
                    holder1.day.setText(a.getDay());
                }
                holder1.detail.setTextColor(Color.BLACK);
                holder1.detail.setText(a.getDetail());
                break;

            case TYPE_B:
                holder2.dot_black.setImageResource(R.drawable.notable_black);
                break;
            case TYPE_C:
                holder3.dot_red.setImageResource(R.drawable.notable_red);
                break;
        }
        return convertView;
    }

    private static class ViewHolder1 {
        TextView week;
        TextView day;
        TextView detail;
    }

    private static class ViewHolder2 {
        ImageView dot_black;
    }

    private static class ViewHolder3{
        ImageView dot_red;
    }
}
