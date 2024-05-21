package com.example.formatif2021partie2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button sendButton;
    private ProgressBar progressBar;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the title of the ActionBar
        getSupportActionBar().setTitle(R.string.action_bar_title);

        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);
        client = new OkHttpClient();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String url = "http://10.0.2.2:8080/api/final/0";

        // Show the progress bar and disable the button
        progressBar.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        sendButton.setEnabled(true);
                        Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        sendButton.setEnabled(true);
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, R.string.success_message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
