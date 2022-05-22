package com.example.chart_0405;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.auth.User;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jackmego.com.jieba_android.JiebaSegmenter;

public class HomeFragment extends Fragment{
    private int SPEECH_REQUEST_CODE = 0;

    public HomeFragment(){}

    TextClock textClock;
    String totalFee;
    TextView todayFee;
    TextView mimisay;
    ImageView btnCallmimi;
    ImageView btnSearch;
    LinearLayout layout;
    ArrayList<String> wordList = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> notName = new ArrayList<>();
    ArrayList<String> clothes = new ArrayList<>();
    ArrayList<String> home = new ArrayList<>();
    ArrayList<String> trans = new ArrayList<>();
    ArrayList<String> mimi = new ArrayList<>();
    String spokenText;
    String date = getDate();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotName();
        setClothes();
        setHome();
        setTrans();
        mimi.add("主人好，開啟網路才能語音記帳喔");
        mimi.add("我是蜜蜜瓜，你要不要吃哈密瓜?");
        mimi.add("主人今天記帳了嗎？養成記帳的習慣月底才不會吃泡麵喔");
        mimi.add("語音紀錄「食」類別的時候，一定要說到關鍵字「吃」喔!例如：今天午餐吃牛肉麵90元。");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        mimisay = rootView.findViewById(R.id.mimisay);
        layout = rootView.findViewById(R.id.linearlayout);
        mimispeak();

        btnCallmimi = rootView.findViewById(R.id.callmimi);
        btnSearch = rootView.findViewById(R.id.talktomimi);

        btnSearch.setOnClickListener(v -> {
                arrayList.clear();
                Toast.makeText(getView().getContext(), "正在跟Mimi講話", Toast.LENGTH_SHORT).show();
                displaySpeechRecognizer();
        });

        btnCallmimi.setOnClickListener(v -> {
            if(!Settings.canDrawOverlays(getContext())){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("！蜜蜜瓜提醒！");
                alertDialog.setMessage("召喚蜜蜜瓜之前要到設定先開啟懸浮式窗的權限喔(｡•̀ᴗ-)✧");
                alertDialog.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getBaseContext(),"確定",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }else{
                getActivity().startService(new Intent(getActivity(),FloatingViewService.class));
            }

        });

        layout.setOnClickListener(v ->{
            Log.i("Home","mimi is touched！");
            mimispeak();
        });


        textClock = (TextClock) rootView.findViewById(R.id.dateinhome);
        textClock.setFormat12Hour("yyyy-MM-dd");
        //接收今日總花費
        Bundle bundle = getActivity().getIntent().getExtras();
        totalFee = bundle.getString("totalFee");
        Log.i("HomeFragment","totalFee from Main:" + totalFee);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //顯示在螢幕上
        todayFee = getActivity().findViewById(R.id.totalFee);
        todayFee.setText("$" + totalFee );
    }

    public void mimispeak(){
        final int min = 0;
        final int max = 3;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String mimisays = mimi.get(random);
        mimisay.setText(mimisays);
        Log.i("Home","mimi says : " + mimisays);
    }


    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenText = results.get(0);
            Log.i("home","talktomimi : " + spokenText);
            String Type = convertType(spokenText);
            wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(spokenText);
            Log.i("home","wordList = " +wordList);

            String Fee = convertFee(wordList);
            String Name = convertName(wordList);

            arrayList.add(date);
            arrayList.add(Name);
            arrayList.add(Type);
            arrayList.add(Fee);
            arrayList.add("out");

            Log.i("home","arraylist : " + arrayList);
            //new一個intent物件，指定切換的class
            Intent intent = new Intent();
            intent.setClass(getActivity().getBaseContext(), MainActivity.class);
            //new一個bundle，傳送指定的物件
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("arrayList1", arrayList);
            //將bundle物件傳給intent
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public String convertName(ArrayList<String> a){
        String name="";
        String fee = convertFee(a);
        a.remove(fee);
        for(int f = 0 ; f<notName.size() ; f++){
            a.remove(notName.get(f));
        }
        for(int i = 0; i <a.size();i++){
            name = name + a.get(i);
        }
        return name;
    }

    public String convertFee(ArrayList<String> a){
        String fee="";
        for(int i = 0; i<a.size();i++) {
            if(isNumeric(a.get(i))){
                fee = a.get(i);
            }
        }
        return fee;
    }

    public String convertType(String a){
        Log.i("home","spokenText : " + a);
        String type ="";
        if(a.indexOf("吃")!=-1 || a.indexOf("喝")!=-1){
            type = "食";
        }
        else if(isClothes(a)){
            type = "衣";
        }
        else if(isHome(a)){
            type = "住";
        }
        else if(isTrans(a)){
            type = "行";
        }
        else{
            type = "其他";
        }

        return type;
    }

    public String getDate(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }



    public void setNotName(){
        notName.add("吃");
        notName.add("喝");
        notName.add("元");
        notName.add("塊");
        notName.add("買");
        notName.add("繳");
        notName.add("搭");
        notName.add("了");
        notName.add("早餐");
        notName.add("午餐");
        notName.add("中餐");
        notName.add("晚餐");
        notName.add("宵夜");
        notName.add("早上");
        notName.add("中午");
        notName.add("晚上");
        notName.add("今天");
    }

    public void setClothes(){
        clothes.add("衣服");
        clothes.add("t-shirt");
        clothes.add("短褲");
        clothes.add("褲");
        clothes.add("裙");
        clothes.add("裙子");
        clothes.add("襪");
        clothes.add("襪子");
        clothes.add("褲襪");
        clothes.add("絲襪");
        clothes.add("長襪");
        clothes.add("短裙");
        clothes.add("牛仔");
        clothes.add("領帶");
        clothes.add("襯衫");
        clothes.add("西裝");
        clothes.add("外套");
        clothes.add("羽絨衣");
    }

    public void setHome() {
        home.add("房租");
        home.add("水電");
    }

    public void setTrans() {
        trans.add("火車");
        trans.add("台鐵");
        trans.add("高鐵");
        trans.add("公車");
        trans.add("客運");
        trans.add("機票");
        trans.add("船票");
        trans.add("叫車");
        trans.add("叫uber");
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public boolean isClothes(String str){
        boolean isC = false;
        for(int i = 0 ; i < clothes.size() ; i++){
            if( str.contains(clothes.get(i))){
                isC = true;
                break;
            }
            else isC = false;
            continue;
        }
        return isC;
    }

    public boolean isHome(String str){
        boolean ishome = false;
        for(int i = 0 ; i < home.size() ; i++){
            if(str.contains(home.get(i))){
                ishome = true;
                break;
            }
            else ishome = false;
        }

        return ishome;
    }

    public boolean isTrans(String str){
        boolean istrans = false;
        for(int i = 0 ; i < trans.size() ; i++){
            if(str.contains(trans.get(i))){
                istrans = true;
                break;
            }
            else istrans = false;
        }
        return istrans;
    }
}