package com.example.chart_0405;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.auth.User;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jackmego.com.jieba_android.JiebaSegmenter;
//import jackmego.com.jieba_android.JiebaSegmenter;

public class HomeFragment extends Fragment {
    private int SPEECH_REQUEST_CODE = 0;

    public HomeFragment(){}

    TextClock textClock;
    String totalFee;
    TextView todayFee;
    Button btn_talk;
    ImageView btnSearch;
    ArrayList<String> wordList = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> notName = new ArrayList<>();
    String spokenText;
    String date = getDate();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notName.add("吃");
        notName.add("元");
        notName.add("塊");
        notName.add("買");
        notName.add("了");
        notName.add("交通");
        notName.add("服飾");
        notName.add("居家");
        notName.add("學習");
        notName.add("今天");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        //btn_talk = rootView.findViewById(R.id.talktomimi);
        btnSearch = rootView.findViewById(R.id.talktomimi);

        btnSearch.setOnClickListener(v -> {
                arrayList.clear();
                Toast.makeText(getView().getContext(), "正在跟Mimi講話", Toast.LENGTH_SHORT).show();
                displaySpeechRecognizer();
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
        todayFee.setText("今日花費" + totalFee + " 元");
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
            wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(spokenText);
            Log.i("home","wordList = " +wordList);
            String Type = convertType(wordList);
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

    public String convertType(ArrayList a){
        Log.i("home","arraylist : " + a);
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
        return type;
    }

    public String getDate(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}