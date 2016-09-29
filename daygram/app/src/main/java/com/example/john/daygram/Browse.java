package com.example.john.daygram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Browse extends AppCompatActivity {
    List<Data> listData = new ArrayList<>();
    List<Data> listOfYear = new ArrayList<>();
    private List<List<Data>> yearData = new ArrayList<>();
    String browse = "";
    Data tempData;
    TextView textView;
    ImageView imageView;
    GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        //显示浏览页面
        textView = (TextView)findViewById(R.id.browse_string);
        listOfYear = (List<Data>)getIntent().getSerializableExtra("allyear");
        List<Data> temp1 = new ArrayList<>();
        List<Data> temp2 = new ArrayList<>();
        List<Data> temp3 = new ArrayList<>();
        List<Data> temp4 = new ArrayList<>();
        List<Data> temp5 = new ArrayList<>();
        List<Data> temp6 = new ArrayList<>();
        List<Data> temp7 = new ArrayList<>();
        List<Data> temp8 = new ArrayList<>();
        List<Data> temp9 = new ArrayList<>();
        List<Data> temp10 = new ArrayList<>();
        List<Data> temp11 = new ArrayList<>();
        List<Data> temp12 = new ArrayList<>();
        for(int i=0;i < listOfYear.size();i++){
            if(listOfYear.get(i).getMonth().equals("JANUARY")){
                temp1.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("FEBRUARY")){
                temp2.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("MARCH")){
                temp3.add(listOfYear.get(i));

            }
            else if(listOfYear.get(i).getMonth().equals("APRIL")){
                temp4.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("MAY")){
                temp5.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("JUNE")){
                temp6.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("JULY")){
                temp7.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("AUGUST")){
                temp8.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("SEPTEMBER")){
                temp9.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("OCTOBER")){
                temp10.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("NOVEMBER")){
                temp11.add(listOfYear.get(i));
            }
            else if(listOfYear.get(i).getMonth().equals("DECEMBER")){
                temp12.add(listOfYear.get(i));
            }
        }
        yearData.add(temp1);
        yearData.add(temp2);
        yearData.add(temp3);
        yearData.add(temp4);
        yearData.add(temp5);
        yearData.add(temp6);
        yearData.add(temp7);
        yearData.add(temp8);
        yearData.add(temp9);
        yearData.add(temp10);
        yearData.add(temp11);
        yearData.add(temp12);
        listData = (List<Data>)getIntent().getSerializableExtra("browse");

        imageView = (ImageView)findViewById(R.id.browse2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Browse.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        grid = (GridView)findViewById(R.id.horilist);
        refresh();
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listData.clear();
                ((ArrayList<Data>) listData).addAll(yearData.get(position));
                refresh();
            }
        });
    }
    public void refresh(){
        browse = "";
        for(int i = 0;i < listData.size();i++){
            String str = listData.get(i).getDetail();
            if(!str.equals("")){
                tempData = listData.get(i);
                browse += tempData.getDay()+" "+tempData.getWeek()+" / "+tempData.getDetail()+"\n\n";
            }
        }
        textView.setText(browse);

        int size = yearData.size();
        int length = 60;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        Log.i("屏幕密度",density+"");
        Log.i("屏幕高度和宽度",dm.heightPixels+" "+dm.widthPixels);
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        grid.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        grid.setColumnWidth(itemWidth); // 设置列表项宽
        grid.setHorizontalSpacing(5); // 设置列表项水平间距
        grid.setStretchMode(GridView.NO_STRETCH);
        grid.setNumColumns(size); // 设置列数量=列表集合数

        ChoosingAdapter adapter = new ChoosingAdapter(this,(ArrayList<List<Data>>)yearData,(ArrayList<Data>)listData);
        grid.setAdapter(adapter);
    }
}
