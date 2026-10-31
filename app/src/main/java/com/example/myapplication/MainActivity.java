package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay, tvOperation;
    private LinearLayout rowSci1, rowSci2;

    private String current = "0";
    private String expression = "";
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvOperation = findViewById(R.id.tvOperation);
        rowSci1 = findViewById(R.id.sci1);
        rowSci2 = findViewById(R.id.sci2);

        View.OnClickListener listener = v -> onButtonClick(((Button) v).getText().toString());

        int[] ids = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnComma, R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply,
                R.id.btnDivide, R.id.btnEquals, R.id.btnC, R.id.btnCE,
                R.id.btnBackspace, R.id.btnPercent, R.id.btnReciprocal,
                R.id.btnSquare, R.id.btnSqrt,
                R.id.btnSin, R.id.btnCos, R.id.btnTan, R.id.btnLog, R.id.btnLn,
                R.id.btnPi, R.id.btnE, R.id.btnPower, R.id.btnFact, R.id.btnAbs
        };
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void onButtonClick(String text) {
        if (current.equals("Error")) {
            current = "0";
            firstNumber = 0;
            operator = "";
            startNew = true;
            expression = "";
        }

        String before = current;

        switch (text) {
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                if (startNew && operator.isEmpty()) expression = "";
                if (startNew || current.equals("0"))
                    current = text;
                else
                    current += text;
                startNew = false;
                break;

            case ",":
                if (startNew && operator.isEmpty()) expression = "";
                if (startNew) {
                    current = "0.";
                    startNew = false;
                } else if (!current.contains(".")) {
                    current += ".";
                }
                break;

            case "+": case "\u2212": case "\u00D7": case "\u00F7": case "x^y":
                calculate();
                if (current.equals("Error")) break;
                firstNumber = Double.parseDouble(current);
                operator = text;
                startNew = true;
                expression = format(firstNumber) + " " + operator;
                break;

            case "=":
                if (!operator.isEmpty() && !startNew) {
                    String shownExpression = format(firstNumber) + " " + operator + " " + current + " =";
                    calculate();
                    expression = shownExpression;
                }
                operator = "";
                startNew = true;
                break;

            case "C":
                current = "0";
                firstNumber = 0;
                operator = "";
                startNew = true;
                expression = "";
                break;

            case "CE":
                current = "0";
                startNew = true;
                break;

            case "\u232B":
                if (!startNew && current.length() > 1) {
                    current = current.substring(0, current.length() - 1);
                } else {
                    current = "0";
                }
                break;

            case "%":
                expression = before + "%";
                current = format(Double.parseDouble(current) / 100);
                startNew = true;
                break;

            case "1/x": {
                expression = "1/(" + before + ")";
                double v = Double.parseDouble(current);
                if (v == 0) current = "Error";
                else current = format(1 / v);
                startNew = true;
                break;
            }

            case "x\u00B2":
                expression = "sqr(" + before + ")";
                current = format(Math.pow(Double.parseDouble(current), 2));
                startNew = true;
                break;

            case "\u00B2\u221Ax": {
                expression = "\u221A(" + before + ")";
                double n = Double.parseDouble(current);
                if (n < 0) current = "Error";
                else current = format(Math.sqrt(n));
                startNew = true;
                break;
            }

            case "sin":
                expression = "sin(" + before + ")";
                current = format(Math.sin(Math.toRadians(Double.parseDouble(current))));
                startNew = true;
                break;

            case "cos":
                expression = "cos(" + before + ")";
                current = format(Math.cos(Math.toRadians(Double.parseDouble(current))));
                startNew = true;
                break;

            case "tan":
                expression = "tan(" + before + ")";
                current = format(Math.tan(Math.toRadians(Double.parseDouble(current))));
                startNew = true;
                break;

            case "log": {
                expression = "log(" + before + ")";
                double n = Double.parseDouble(current);
                current = (n <= 0) ? "Error" : format(Math.log10(n));
                startNew = true;
                break;
            }

            case "ln": {
                expression = "ln(" + before + ")";
                double n = Double.parseDouble(current);
                current = (n <= 0) ? "Error" : format(Math.log(n));
                startNew = true;
                break;
            }

            case "\u03C0":
                expression = "";
                current = String.valueOf(Math.PI);
                startNew = true;
                break;

            case "e":
                expression = "";
                current = String.valueOf(Math.E);
                startNew = true;
                break;

            case "abs":
                expression = "abs(" + before + ")";
                current = format(Math.abs(Double.parseDouble(current)));
                startNew = true;
                break;

            case "n!": {
                expression = before + "!";
                double n = Double.parseDouble(current);
                if (n < 0 || n != Math.floor(n) || n > 20) {
                    current = "Error";
                } else {
                    long f = 1;
                    for (int i = 2; i <= (int) n; i++) f *= i;
                    current = String.valueOf(f);
                }
                startNew = true;
                break;
            }
        }

        if (current.equals("Error")) expression = "";

        tvDisplay.setText(current.replace('.', ','));
        tvOperation.setText(expression.replace('.', ','));
    }

    private void calculate() {
        if (operator.isEmpty() || startNew) return;
        double second = Double.parseDouble(current);
        double result = 0;
        switch (operator) {
            case "+":
                result = firstNumber + second;
                break;
            case "\u2212":
                result = firstNumber - second;
                break;
            case "\u00D7":
                result = firstNumber * second;
                break;
            case "\u00F7":
                if (second == 0) {
                    current = "Error";
                    operator = "";
                    return;
                }
                result = firstNumber / second;
                break;
            case "x^y":
                result = Math.pow(firstNumber, second);
                break;
        }
        current = format(result);
    }

    private String format(double value) {
        if (value == (long) value) return String.valueOf((long) value);
        return String.valueOf(value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calc_standart) {
            rowSci1.setVisibility(View.GONE);
            rowSci2.setVisibility(View.GONE);
            Toast.makeText(this, "Standard", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.calc_scientifique) {
            rowSci1.setVisibility(View.VISIBLE);
            rowSci2.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Scientifique", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.calc_graphique) {
            startActivity(new Intent(MainActivity.this, GraphicActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}