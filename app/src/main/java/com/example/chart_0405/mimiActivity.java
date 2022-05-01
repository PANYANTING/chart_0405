package com.example.chart_0405;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class mimiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mimi);

        //取得介面元件
        ImageButton button_home = (ImageButton) findViewById(R.id.button_home);
        //設定 Button 的 Listner
        button_home.setOnClickListener(button_homeLister);
    }

    private View.OnClickListener button_homeLister = new ImageButton.OnClickListener(){
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setClass(mimiActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };
}