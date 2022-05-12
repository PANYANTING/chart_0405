package com.example.chart_0405;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DetailFragment extends Fragment {
    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    static SQLiteDataBaseHelper dDB;
    String date;

    private RecyclerView myrecyclerview;
    private List<Detail> lstDetail;
    ArrayList<HashMap<String, String>> getNowArray = new ArrayList<>();//取得被選中的項目資料

    public DetailFragment() {
    }
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        date = getDate();

        //日曆按鈕
        Button btn_date = rootView.findViewById(R.id.date);

        //按鈕監聽器
        class MyOnClickListener implements View.OnClickListener{
            @Override
            public  void onClick(View view){
                if(view.getId()==R.id.date){
                    //彈出日曆視窗
                    DatePickerDialog pickerDialog = new DatePickerDialog(requireActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                //@Override
                                String cho_date;
                                public  void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    cho_date = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                                    Button btn_date = rootView.findViewById(R.id.date);
                                    btn_date.setText(cho_date);
                                    date = cho_date;
                                    onStart();
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

    @Override
    public void onStart() {
        super.onStart();
        dDB = new SQLiteDataBaseHelper(getActivity(), DB_NAME, null, DB_VERSION, TABLE_NAME);//初始化資料庫
        Log.i("D","SHOW ALL : " +dDB.showAll() );
        Log.i("d","date:" + date);
        getNowArray = dDB.searchByDate(date);
        Log.i("DetailFragment", "getNowArray" + getNowArray);
        lstDetail = new ArrayList<>();
        //拆解getNowArray
        for (int i = getNowArray.size()-1; i >= 0; i--) {
            ArrayList<String> aa = new ArrayList<>();
            aa.add(getNowArray.get(i).get("id"));
            aa.add(getNowArray.get(i).get("date"));
            aa.add(getNowArray.get(i).get("name"));
            aa.add(getNowArray.get(i).get("type"));
            aa.add(getNowArray.get(i).get("fee"));
            aa.add(getNowArray.get(i).get("status"));
            lstDetail.add(new Detail(aa));
            Log.i("detail", "aa" + aa);
        }
        Log.i("d","date:" + date);
        myrecyclerview = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        MyAdapter myAdapter = new MyAdapter(getContext(),lstDetail);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(myAdapter);
        myrecyclerview.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    public String getDate(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    public int getyear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy");
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public int getmonth() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("MM");
        int month = calendar.get(Calendar.MONTH);
        return month;
    }

    public int getday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("dd");
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }


}