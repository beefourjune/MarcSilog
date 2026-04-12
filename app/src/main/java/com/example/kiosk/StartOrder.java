package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class StartOrder extends AppCompatActivity {

    MaterialButton dineinBtn;
    MaterialButton takeoutBtn;
    MaterialButton adminAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_order);

        dineinBtn = findViewById(R.id.dineinbtn);
        takeoutBtn = findViewById(R.id.takeoutbtn);
        adminAcc = findViewById(R.id.adminaccount);

        // ================= DINE IN =================
        dineinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOrder.this, MainMenu.class);
                intent.putExtra("order_type", "DINE IN"); // 🔥 IMPORTANT
                startActivity(intent);
            }
        });

        // ================= TAKE OUT =================
        takeoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOrder.this, MainMenu.class);
                intent.putExtra("order_type", "TAKE OUT"); // 🔥 IMPORTANT
                startActivity(intent);
            }
        });

        adminAcc.setOnClickListener(v -> {
            Intent intent = new Intent(StartOrder.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}