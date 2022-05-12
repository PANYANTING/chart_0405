package com.example.chart_0405;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChartFragment extends Fragment {
    private PieChart pieChart;
    TextView tv_in;
    TextView tv_out;
    TextView fee;
    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    static SQLiteDataBaseHelper cDB;
    ArrayList<HashMap<String,String>>TF = new ArrayList<>();
    String Y = getYear();
    String M = getMonth();



    public ChartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        cDB = new SQLiteDataBaseHelper(getActivity(), DB_NAME, null, DB_VERSION, TABLE_NAME);//初始化資料庫

        int IN = Integer.valueOf(cDB.getMonthFee(Y,M,"in"));
        int OUT = Integer.valueOf(cDB.getMonthFee(Y,M,"out"));
        Log.i("chart","in" + (IN-OUT));
        pieChart = (PieChart) rootView.findViewById(R.id.chart);
        tv_in = (TextView)rootView.findViewById(R.id.MFeein);
        tv_out = (TextView) rootView.findViewById(R.id.MFeeout);
        fee = (TextView) rootView.findViewById(R.id.MFee);
        tv_in.setText("收入 $ " + IN);
        tv_out.setText("支出 $ " + OUT);
        fee.setText("損益 $ "+ (IN-OUT));
        setupPieChart();
        loadPieChartData();

        return rootView;
    }

    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("分類報告");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setEnabled(true);
    }

    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        //讀取單月份總花費
        TF = cDB.getTypeFee(Y,M);
        for(int i = 0 ; i<TF.size();i++){
            ArrayList<String> data = new ArrayList();
            data.add(TF.get(i).get("type"));
            data.add(TF.get(i).get("fee"));
            entries.add(new PieEntry(Float.parseFloat(data.get(1)),data.get(0)));
        }

        ArrayList<Integer>colors = new ArrayList<>();
        for(int color: ColorTemplate.JOYFUL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries,"圖例");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

    }

    public String getYear(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy");
        java.util.Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
    public String getMonth(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("MM");
        java.util.Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

}