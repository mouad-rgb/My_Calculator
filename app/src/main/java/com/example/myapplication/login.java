package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class login extends AppCompatActivity {

    EditText ed_text, ed_password;
    Button btn_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ed_text = findViewById(R.id.ed_Text);
        ed_password = findViewById(R.id.ed_password);
        btn_value = findViewById(R.id.btn_value);

        btn_value.setOnClickListener(view -> {
            String login = ed_text.getText().toString().trim();
            String password = ed_password.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir le login et le mot de passe", Toast.LENGTH_LONG).show();
            } else if (login.equals("admin") && password.equals("1234")) {
                startActivity(new Intent(login.this,MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login ou mot de passe incorrect", Toast.LENGTH_LONG).show();
            }
        });
    }
}