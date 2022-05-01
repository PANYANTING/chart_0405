package com.example.chart_0405;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.chart_0405.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    String TAG = MainActivity.class.getSimpleName() + "My";
    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    SQLiteDataBaseHelper mDB;
    ArrayList<String> arrayList = new ArrayList<>();//取得新增資料
    ArrayList<HashMap<String, String>> getNowArray = new ArrayList<>();//取得被選中的項目資料
    String date;
    String totalFee;

    ActivityMainBinding binding;


    @Override
    public void onBackPressed(){
        mDB.close();
        finish();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDB = new SQLiteDataBaseHelper(this, DB_NAME, null, DB_VERSION, TABLE_NAME);//初始化資料庫
        Log.d(TAG, "onCreate: "+mDB.showAll());
        mDB.chickTable();//確認是否存在資料表，沒有則新增
        arrayList.clear();//清空要新增資料的陣列

        //接收新增的資料
        Intent intent = getIntent();
        arrayList = intent.getStringArrayListExtra("arrayList");
        Log.i("MainActivity","From Add : " + arrayList);
        if(arrayList!=null){
            mDB.addData(arrayList);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());//設定一點進來的畫面

        //取得今天的日期
        date = getDate();
        Log.i("MainActivity", "date : "+date);

        //統計今天的總花費
        totalFee = mDB.getTotalFee(date);
        Log.i("MainActivity","totalFee : " + totalFee );

        //傳送今天總花費給home
        Intent sintent = new Intent();
        sintent.setClass(MainActivity.this,HomeFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("totalFee",totalFee);
        intent.putExtras(bundle);

        //取得今天的花費明細
        getNowArray = mDB.searchByDate(date);



        /*切換分頁*/
        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId()){

                case R.id.chart:
                    replaceFragment(new ChartFragment());
                    break;
                case R.id.add:
                    replaceFragment(new AddFragment());
                    break;
                case R.id.detail:
                    replaceFragment(new DetailFragment());
                    break;
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public String getDate(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }


}