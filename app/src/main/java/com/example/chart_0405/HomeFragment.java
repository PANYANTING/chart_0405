package com.example.chart_0405;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import jackmego.com.jieba_android.JiebaSegmenter;

public class HomeFragment extends Fragment {
    private int SPEECH_REQUEST_CODE = 0;

    public HomeFragment(){}

    TextClock textClock;
    String totalFee;
    TextView todayFee;
    Button btn_talk;
    ArrayList<String> wordList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        btn_talk = rootView.findViewById(R.id.talktomimi);

        btn_talk.setOnClickListener(v -> {
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
            String spokenText = results.get(0);
            Toast.makeText(getView().getContext(), spokenText, Toast.LENGTH_SHORT).show();
            Log.i("home","talktomimi : " + spokenText);
//            wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(spokenText);
//            Log.i("main","wordList = "+wordList);

        }
    }
}