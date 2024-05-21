package com.example.formatif2022partie2;

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

    private EditText etPassword;
    private Button btnCheckPassword;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPassword = findViewById(R.id.et_password);
        btnCheckPassword = findViewById(R.id.btn_check_password);
        client = new OkHttpClient();

        btnCheckPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                if (!password.isEmpty()) {
                    sendRequest(password);
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un mot de passe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendRequest(String password) {
        String url = "https://4n6.azurewebsites.net/exam2022/motdepasse/" + password;

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
                            Toast.makeText(MainActivity.this, "Résultat : " + responseBody, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    final String errorResponse = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(findViewById(android.R.id.content), "Mot de passe incorrect : " + errorResponse, Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
