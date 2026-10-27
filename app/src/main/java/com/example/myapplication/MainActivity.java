package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;

    private String current = "0";
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        // One listener shared by ALL buttons
        View.OnClickListener listener = v -> onButtonClick(((Button) v).getText().toString());

        int[] ids = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnComma, R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply,
                R.id.btnDivide, R.id.btnEquals, R.id.btnC, R.id.btnCE,
                R.id.btnBackspace, R.id.btnPercent, R.id.btnReciprocal,
                R.id.btnSquare, R.id.btnSqrt
        };
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void onButtonClick(String text) {
        switch (text) {
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                if (startNew || current.equals("0"))
                    current = text;
                else
                    current += text;
                startNew = false;
                break;

            case ",":
                if (startNew) {
                    current = "0.";
                    startNew = false;
                }
                else if (!current.contains(".")) current += ".";
                break;

            case "+": case "\u2212": case "\u00D7": case "\u00F7":
                calculate();
                firstNumber = Double.parseDouble(current);
                operator = text;
                startNew = true;
                break;

            case "=":
                calculate();
                operator = "";
                startNew = true;
                break;

            case "C":
                current = "0"; firstNumber = 0; operator = ""; startNew = true;
                break;

            case "CE":
                current = "0"; startNew = true;
                break;

            case "\u232B": // backspace
                if (!startNew && current.length() > 1) {
                    current = current.substring(0, current.length() - 1);
                } else {
                    current = "0";
                }
                break;

            case "%":
                current = format(Double.parseDouble(current) / 100);
                break;

            case "1/x":
                double v = Double.parseDouble(current);
                if (v == 0) { current = "Error"; startNew = true; }
                else current = format(1 / v);
                break;

            case "x\u00B2":
                current = format(Math.pow(Double.parseDouble(current), 2));
                startNew = true;
                break;

            case "\u00B2\u221Ax":
                double n = Double.parseDouble(current);
                if (n < 0) { current = "Error"; }
                else current = format(Math.sqrt(n));
                startNew = true;
                break;
        }
        tvDisplay.setText(current.replace('.', ','));
    }

    private void calculate() {
        if (operator.isEmpty() || startNew) return;
        double second = Double.parseDouble(current);
        double result = 0;
        switch (operator) {
            case "+":      result = firstNumber + second; break;
            case "\u2212": result = firstNumber - second; break;
            case "\u00D7": result = firstNumber * second; break;
            case "\u00F7":
                if (second == 0) { current = "Error"; operator = ""; return; }
                result = firstNumber / second;
                break;
        }
        current = format(result);
    }


    private String format(double value) {
        if (value == (long) value) return String.valueOf((long) value);
        return String.valueOf(value);
    }
}