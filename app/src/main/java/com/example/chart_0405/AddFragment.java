package com.example.chart_0405;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddFragment extends Fragment {

    EditText edName, edType, edFee;
    Button btCreate;
    String edDate;
    TextClock textClock;
    ArrayList<String> arrayList = new ArrayList<>();//取得新增資料

    public AddFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);


        //連接所有元件
        btCreate = rootView.findViewById(R.id.button_Create);
        edName = rootView.findViewById(R.id.editText_Name);
        edType = rootView.findViewById(R.id.editText_Type);
        edFee = rootView.findViewById(R.id.editText_Fee);

        btCreate.setOnClickListener(v -> {
            arrayList = getValues();
            clearAll();
            Toast.makeText(getView().getContext(), "已新增紀錄", Toast.LENGTH_SHORT).show();
            Log.i("AddFragment", "arrayList: " + arrayList);
            //new一個intent物件，指定切換的class
            Intent intent = new Intent();
            intent.setClass(getActivity().getBaseContext(), MainActivity.class);

            //new一個bundle，傳送指定的物件
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("arrayList1", arrayList);

            //將bundle物件傳給intent
            intent.putExtras(bundle);
            startActivity(intent);
        });

        //收入支出紀錄切換
        Switch swBelow = rootView.findViewById(R.id.switch_Below);
        swBelow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getView().getContext(), "Sw2: " + isChecked, Toast.LENGTH_SHORT).show();
        });

        //預設日期:讀取系統時間
        edDate = getDate();
        Log.i("AddFragment","today is " + edDate);

        //日曆按鈕
        Button btn_date = rootView.findViewById(R.id.date);
        //按鈕監聽器
        class MyOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.date) {
                    //彈出日曆視窗
                    DatePickerDialog pickerDialog = new DatePickerDialog(requireActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String date = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                                    Button btn_date = rootView.findViewById(R.id.date);
                                    btn_date.setText(date);
                                    edDate = date;
                                }
                            }, getyear(), getmonth(), getday());
                    pickerDialog.show();
                }

            }
        }

        //日曆按鈕監聽器
        btn_date.setOnClickListener(new MyOnClickListener());

        // Inflate the layout for this fragment
        return rootView;
    }

    public int getyear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public int getmonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        return month;
    }

    public int getday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public ArrayList<String> getValues() {
        String date = edDate;
        String name = edName.getText().toString();
        String type = edType.getText().toString();
        String fee = edFee.getText().toString();
        ArrayList<String> values = new ArrayList<>();
        values.add(date);
        values.add(name);
        values.add(type);
        values.add(fee);
        return values;
    }

    private void clearAll() {//清空目前所選以及所有editText
        edName.setText("");
        edType.setText("");
        edFee.setText("");
    }

    public String getDate(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
}

