package com.example.mfellner.sew_7a_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private char calcOperation;
    private Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get all radiobuttons
        Button addButton = (Button) findViewById(R.id.radio_addition);
        Button subButton = (Button) findViewById(R.id.radio_subtraction);
        Button multButton = (Button) findViewById(R.id.radio_multiplication);
        Button divButton = (Button) findViewById(R.id.radio_division);

        //add an eventlistener to all buttons
        setupButtonClickListener(addButton, '+');
        setupButtonClickListener(subButton, '-');
        setupButtonClickListener(multButton, '*');
        setupButtonClickListener(divButton, '/');

        this.calculateButton = (Button) findViewById(R.id.button_calculate);
        calculateButton.setOnClickListener(new View.OnClickListener()  {
            public void onClick (View v)    {

                // get all values
                EditText inputFirstValue = (EditText) findViewById(R.id.edit_text_first);
                EditText inputSecondValue = (EditText) findViewById(R.id.edit_text_second);

                double firstValue = extractInputAndConvertToDouble(inputFirstValue);
                double secondValue = extractInputAndConvertToDouble(inputSecondValue);

                double result = performOperation(firstValue, secondValue);

                // make result visible

                TextView resultView = (TextView) findViewById(R.id.text_result);
                resultView.setText(String.valueOf(result));
            }
        });
    }

    private double extractInputAndConvertToDouble(EditText editText)  {
        try {
            return Double.parseDouble(editText.getText().toString());
        }   catch (Exception ex)    {
            System.err.println("Error!" + ex.getMessage());
            return 0;
        }
    }

    private void setupButtonClickListener(Button button, char calcOperation) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalcOperation(calcOperation);
            }
        });
    }

    private double performOperation(double firstValue, double secondValue)    {
        switch (this.calcOperation) {
            case '+':
                return firstValue + secondValue;
            case '-':
                return firstValue - secondValue;
            case '*':
                return firstValue * secondValue;
            case '/':
                return firstValue / secondValue;
            default:
                return 0;
        }
    }

    public void setCalcOperation(char calcOperation) {
        this.calcOperation = calcOperation;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.calculateButton.setBackgroundColor(Color.GREEN);
    }
}