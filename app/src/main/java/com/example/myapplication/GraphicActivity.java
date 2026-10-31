package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GraphicActivity extends AppCompatActivity {

    private EditText edFormula;
    private CurveView curveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        edFormula = findViewById(R.id.edFormula);
        curveView = findViewById(R.id.curveView);
        Button btnDraw = findViewById(R.id.btnDraw);
        Button btnZoomIn = findViewById(R.id.btnZoomIn);
        Button btnZoomOut = findViewById(R.id.btnZoomOut);

        btnDraw.setOnClickListener(v -> draw());
        btnZoomIn.setOnClickListener(v -> curveView.zoomIn());
        btnZoomOut.setOnClickListener(v -> curveView.zoomOut());

        draw();
    }

    private void draw() {
        String formula = edFormula.getText().toString().trim();
        if (formula.isEmpty()) {
            Toast.makeText(this, "Entrez une formule", Toast.LENGTH_SHORT).show();
            return;
        }

        ExpressionParser parser = new ExpressionParser(formula);
        try {
            parser.eval(1);
        } catch (RuntimeException e) {
            Toast.makeText(this, "Formule invalide", Toast.LENGTH_LONG).show();
            return;
        }
        curveView.setFunction(parser);
    }
}