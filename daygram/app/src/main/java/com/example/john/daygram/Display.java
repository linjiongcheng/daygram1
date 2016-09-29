package com.example.john.daygram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Display extends AppCompatActivity {
    private TextView time;
    private ImageView ret;
    private Button done;
    private EditText edit;
    private Data data = new Data();
    private List<Data> allListData = null;                                       //记录所有数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        //调用全局变量,从文件中读取数据，赋给全局变量allData
        final AllData allData = (AllData)getApplication();
        allListData = allData.getData();

        edit = (EditText)findViewById(R.id.diary);

        //设置不可编辑状态
        edit.setFocusable(false);
        edit.setFocusableInTouchMode(false);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置可编辑状态
                edit.setFocusableInTouchMode(true);
                edit.setFocusable(true);
            }
        });

        String signal = (String)this.getIntent().getExtras().get("signal");
        String detail = "";
        if(signal.equals("add")){
            detail = (String)this.getIntent().getExtras().get("detail");
        }

        time = (TextView)findViewById(R.id.date);
        String timeAtPresent[] = (String[]) this.getIntent().getExtras().get("timeAtPresent");
        data.setYear(timeAtPresent[0]);
        data.setMonth(timeAtPresent[1]);
        data.setDay(timeAtPresent[2]);
        data.setWeek(timeAtPresent[3]);
        data.setDate(timeAtPresent[4]);
        data.setDetail(detail);
        edit.setText(data.getDetail());
        //如果是星期天则显示红色
        if(timeAtPresent[3].equals("SUNDAY")){
            timeAtPresent[3] = "<font color='#FF0000'>" + timeAtPresent[3] + "</font>";
        }
        // 在标题栏位置显示当前时间
        time.setText(Html.fromHtml(timeAtPresent[3]));
        time.append("/"+timeAtPresent[1]+" "+timeAtPresent[2]+"/"+timeAtPresent[0]);
        // 如果是点击列表选项，则接收data数据
        if(signal.equals("edit")){
            data = (Data)this.getIntent().getExtras().get("data");
            edit.setText(data.getDetail());                               //在编辑位置显示之前保存的日记内容
        }
        Log.i("查看数据",data.getYear()+data.getMonth()+data.getDay()+data.getWeek());

        done = (Button)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将编辑区的内容写入日记
                data.setDetail(edit.getText().toString());
                for(int i = 0;i < allListData.size();i++){
                    if(allListData.get(i).getDate().equals(data.getDate())){
                        allListData.get(i).setDetail(data.getDetail());
                        allData.setData(allListData);
                        saveObject("daygram",allData.getData());
                        break;
                    }
                }
                Intent intent = new Intent();
                intent.setClass(Display.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ret = (ImageView)findViewById(R.id.home);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Display.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
}
