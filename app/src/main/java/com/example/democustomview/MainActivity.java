package com.example.democustomview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleView circleView = findViewById(R.id.circle_view);
        Slider slider = findViewById(R.id.sld_progress);
        Button button = findViewById(R.id.btn_start);

        slider.addOnChangeListener((slider1, value, fromUser) -> {
            circleView.setProgress(value);
        });

        button.setOnClickListener(v -> {
            circleView.startAnimationProgress(1000);
        });
    }
}