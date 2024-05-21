package com.example.formatif2022partie1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTranslation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTranslation = findViewById(R.id.textViewTranslation);
        Button buttonRock = findViewById(R.id.button_rock);
        Button buttonPaper = findViewById(R.id.button_paper);
        Button buttonScissors = findViewById(R.id.button_scissors);

        buttonRock.setOnClickListener(v -> textViewTranslation.setText(R.string.rock));
        buttonPaper.setOnClickListener(v -> textViewTranslation.setText(R.string.paper));
        buttonScissors.setOnClickListener(v -> textViewTranslation.setText(R.string.scissors));
    }
}

