package com.example.chart_0405;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment(){}

    TextClock textClock;
    String totalFee;
    TextView todayFee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        textClock = (TextClock) rootView.findViewById(R.id.dateinhome);
        textClock.setFormat12Hour("yyyy-MM-dd");
        //接收今日總花費
        Bundle bundle = getActivity().getIntent().getExtras();
        totalFee = bundle.getString("totalFee");
        Log.i("HomeFragment","totalFee from Main:" + totalFee);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //顯示在螢幕上
        todayFee = getActivity().findViewById(R.id.totalFee);
        todayFee.setText("今日花費" + totalFee + " 元");
    }
}