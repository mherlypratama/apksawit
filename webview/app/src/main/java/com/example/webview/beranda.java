package com.example.webview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class beranda extends AppCompatActivity {
    ImageView pindah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_beranda);

        pindah = findViewById(R.id.pindah);
        pindah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buka();
            }

        });
    }
    public void buka(){
        Intent intent= new Intent(this, info.class);
        startActivity(intent);
    }
}