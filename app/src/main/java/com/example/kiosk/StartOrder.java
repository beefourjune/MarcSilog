package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StartOrder extends AppCompatActivity {

    Button backtostartBtn, dineinBtn;

    // ✅ Create cart here
    ArrayList<String> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_order);

        backtostartBtn = findViewById(R.id.backtostartbtn);
        dineinBtn = findViewById(R.id.dineinbtn);

        // ✅ Initialize cart
        cartList = new ArrayList<>();

        dineinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartOrder.this, MainMenu.class);

                // ✅ Pass cart to next screen
                intent.putStringArrayListExtra("cart", cartList);

                startActivity(intent);
            }
        });

        backtostartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}