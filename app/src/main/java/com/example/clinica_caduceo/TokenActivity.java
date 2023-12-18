// TokenActivity.java

package com.example.clinica_caduceo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        // Obtén el token FCM
        String fcmToken = PushService.getFcmToken();

        // Muestra el token en un TextView
        TextView tokenTextView = findViewById(R.id.tokenTextView);
        tokenTextView.setText("FCM Token: " + fcmToken);
    }
}
