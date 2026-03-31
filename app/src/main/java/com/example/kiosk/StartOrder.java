package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;

public class StartOrder extends AppCompatActivity {

    Button dineinBtn;
    MaterialButton adminAcc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_order);

        dineinBtn = findViewById(R.id.dineinbtn);
        adminAcc = findViewById(R.id.adminaccount);

        dineinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOrder.this, MainMenu.class);
                startActivity(intent);
            }
        });

        adminAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartOrder.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}