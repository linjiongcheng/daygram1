package com.example.john.daygram;

import android.app.Application;

import java.util.List;

public class AllData extends Application{
    private List<Data> data;

    public List<Data> getData(){
        return this.data;
    }
    public void setData(List<Data> c){
        this.data = c;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }
}
