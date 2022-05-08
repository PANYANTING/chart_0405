package com.example.chart_0405;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Attributes;

public class Content{
    ArrayList<String> arrayList;
    private String Date = getDate();
    private String Name = "";
    private String Type;
    private String Fee;

    public String getDate(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    public String convertType(ArrayList a){
        String type;
        if(a.indexOf("吃")!=-1){
            type = "食";
        }
        else if(a.indexOf("交通")!=-1){
            type = "行";
        }
        else if(a.indexOf("服飾")!=-1){
            type = "衣";
        }
        else if(a.indexOf("居家")!=-1){
            type = "住";
        }
        else if(a.indexOf("學習")!=-1){
            type = "育";
        }
        else if(a.indexOf("娛樂")!=-1){
            type = "樂";
        }
        else{
            type = "其他";
        }
        Log.i("home","type" + type);
        return type;
    }

    public String convertFee(ArrayList<String> a){
        String fee="";
        for(int i = 0; i<a.size();i++) {
            int item = Integer.parseInt(a.get(i));
            if(String.valueOf(item) == a.get(i)){
                fee = a.get(i);
            }
        }
        return fee;
    }

    public String convertName(ArrayList<String> a){
        String name="";
        convertType(a);
        a.remove(Type);
        convertFee(a);
        a.remove(Fee);
        for(int i = 0; i <a.size();i++){
            name = name + a.get(i);
        }
        return name;
    }

    public ArrayList<String> Convert(ArrayList<String> a){
        Date = getDate();
        Name = convertName(a);
        Type = convertType(a);
        Fee = convertFee(a);
        ArrayList<String> values = new ArrayList<>();
        values.add(Date);
        values.add(Name);
        values.add(Type);
        values.add(Fee);
        return values;
    }

}
