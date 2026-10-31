package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText ed_text, ed_password;
    Button btn_value;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ed_text = findViewById(R.id.ed_Text);
        ed_password = findViewById(R.id.ed_password);
        btn_value = findViewById(R.id.btn_value);
        //textLogin=ed_text.getText().toString();

        btn_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_text.getText().toString().equals("") || ed_text.getText().toString().equals(null)) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir le login", Toast.LENGTH_LONG).show();
                } else if (ed_text.getText().toString().equals("admin")) {
                    startActivity(new Intent(MainActivity.this, MainActivity2.class
                    ));
                } else {
                    Toast.makeText(MainActivity.this, "bonjour", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.calc_standart){
            Toast.makeText(this,"Standard",Toast.LENGTH_SHORT).show();
        } else if (id==R.id.calc_scientifique) {
            Snackbar.make(findViewById(R.id.main),"Sciantifique",Snackbar.LENGTH_SHORT).setAction("annuler", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,"Annule",Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.GRAY)
                            .setTextColor(Color.BLUE)
                            .show();
                }
            })
                    .setBackgroundTint(Color.YELLOW)
                    .setTextColor(Color.GREEN)
                    .setActionTextColor(Color.RED)
                    .show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}