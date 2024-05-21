package com.example.formatif2021partie1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText numberInput;
    private Button sendButton;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberInput = findViewById(R.id.numberInput);
        sendButton = findViewById(R.id.sendButton);
        client = new OkHttpClient();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberStr = numberInput.getText().toString();
                if (!numberStr.isEmpty()) {
                    int number = Integer.parseInt(numberStr);
                    sendRequest(number);
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendRequest(int number) {
        String url = "http://10.0.2.2:8080/api/final/1/" + number;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int result = Integer.parseInt(responseBody);
                            if (number % 10 == 0) {
                                Toast.makeText(MainActivity.this, "Résultat: " + result, Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(findViewById(R.id.sendButton), "Le nombre doit être un multiple de 10", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(findViewById(R.id.sendButton), "Le nombre doit être un multiple de 10", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
