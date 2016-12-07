package com.example.john.daygram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView addDiary;
    private ImageView browse;
    private ListView list;
    private List<Data> allListData;                         //记录所有数据
    private List<Data> listOfYear = new ArrayList<>();                          //记录同一年的数据
    private List<Data> listData = new ArrayList<>();                            //记录同一月的数据
    private Map<String,String> mEngMonth = new HashMap<String,String>(){{
        put("1","JANUARY");put("2","FEBRUARY");put("3","MARCH");put("4","APRIL");put("5","MAY");put("6","JUNE");
        put("7","JULY");put("8","AUGUST");put("9","SEPTEMBER");put("10","OCTOBER");put("11","NOVEMBER");put("12","DECEMBER");
    }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //第一次登录preference的“firststart”为“true”
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        Log.i("判断是否为第一次登录",preferences.getBoolean("firststart", true)+"");
        //判断是不是首次登录
        if (preferences.getBoolean("firststart", true)) {
            editor = preferences.edit();
            //将登录标志位设置为false，即将preference的“firststart”修改为“false”
            editor.putBoolean("firststart", false);
            //提交修改
            editor.commit();
            //第一次登录，初始化数据
            try{
                Initialization initialization = new Initialization();
                AllData allData = (AllData)getApplication();                //调用全局变量
                allData.setData(initialization.init());
                saveObject("daygram",allData.getData());
                Log.i("得到的时间",allData.getData().get(365).getDate());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);

        //调用全局变量,从文件中读取数据，赋给全局变量allData
        final AllData allData = (AllData)getApplication();
        allData.setData((List<Data>)getObject("daygram"));
        allListData = allData.getData();

        //获取系统当前日期
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int week = c.get(Calendar.DAY_OF_WEEK);

        //实现年份的选择
        final Spinner spinner = (Spinner)findViewById(R.id.year);
        spinner.getSelectedItem();
        int yearOrder = year - 2016;
        spinner.setSelection(yearOrder);                //设置默认显示的是当前年份
        //实现月份的选择
        final Spinner spinner2 = (Spinner)findViewById(R.id.month);
        spinner2.getSelectedItem();
        spinner2.setSelection(month);               //设置默认显示的是当前月份

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = MainActivity.this.getResources().getStringArray(R.array.ctype1)[position];
                Log.i("选中年份",year);
                String selectMonth = spinner2.getSelectedItem().toString();
                refresh(year,selectMonth);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String month = MainActivity.this.getResources().getStringArray(R.array.ctype2)[position];
                Log.i("选中月份",month);
                String selectYear = spinner.getSelectedItem().toString();
                refresh(selectYear,month);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //添加新日记操作，获取当前系统时间并传递给下个activity
        addDiary = (ImageView) findViewById(R.id.add);
        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //获取当前时间
                String time[] = new String[5];

                time[0] = String.valueOf(year); // 获取当前年份
                time[1] = mEngMonth.get(String.valueOf(month + 1));// 获取当前月份
                time[2] = String.valueOf(day);// 获取当前月份的日期号码
                time[3] = String.valueOf(week);
                Date date = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String str = sdf.format(date);
                String detail = "";
                time[4] = str;
                for(int i = 0;i < allListData.size();i++){
                    if(allListData.get(i).getDate().equals(str)){
                        detail = allListData.get(i).getDetail();
                        break;
                    }
                }
                if("1".equals(time[3])){
                    time[3] ="SUNDAY";
                }else if("2".equals(time[3])){
                    time[3] ="MONDAY";
                }else if("3".equals(time[3])){
                    time[3] ="TUESDAY";
                }else if("4".equals(time[3])){
                    time[3] ="WEDNESDAY";
                }else if("5".equals(time[3])){
                    time[3] ="THURSDAY";
                }else if("6".equals(time[3])){
                    time[3] ="FRIDAY";
                }else if("7".equals(time[3])){
                    time[3] ="SATURDAY";
                }
                intent.putExtra("signal","add");
                intent.putExtra("timeAtPresent", time);
                intent.putExtra("detail",detail);
                intent.setClass(MainActivity.this, Display.class);
                startActivity(intent);
            }
        });
        browse = (ImageView)findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("allyear",(Serializable)listOfYear);
                intent.putExtra("browse",(Serializable)listData);
                intent.setClass(MainActivity.this,Browse.class);
                startActivity(intent);
            }
        });
    }
    //根据选择的月份和年份刷新列表内容
    private void refresh(String year,String month){
        listOfYear.clear();
        int num1 = allListData.size();
        for(int i = 0;i < num1;i++){
            if(allListData.get(i).getYear().equals(year)){
                listOfYear.add(allListData.get(i));
                if((i < num1-1)&&(!allListData.get(i+1).getYear().equals(year))){
                    break;
                }
            }
        }
        listData.clear();
        int num2 = listOfYear.size();
        for(int i = 0;i < num2;i++){
            if(listOfYear.get(i).getMonth().equals(month)){
                listData.add(listOfYear.get(i));
                if((i < num2-1)&&(!listOfYear.get(i).getMonth().equals(month))){
                    break;
                }
            }
        }
        try{
            ABAdapter adapter = new ABAdapter(this,(ArrayList<Data>)listData);
            list.setAdapter(adapter);
            //设置列表选项监听功能
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("signal","edit");
                    intent.putExtra("data", listData.get(position));
                    String time[] = new String[5];
                    time[0] = listData.get(position).getYear();
                    time[1] = listData.get(position).getMonth();
                    time[2] = listData.get(position).getDay();
                    time[3] = listData.get(position).getWeek();
                    time[4] = listData.get(position).getDate();

                    intent.putExtra("timeAtPresent", time);
                    intent.setClass(MainActivity.this,Display.class);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //将日记数据重新存储到文件中
    private void saveObject(String name,List<Data> data) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //从文件中获得总的日记数据
    private Object getObject(String name) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

