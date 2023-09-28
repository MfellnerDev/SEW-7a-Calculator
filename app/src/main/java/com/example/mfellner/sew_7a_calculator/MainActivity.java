package com.example.mfellner.sew_7a_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private char calcOperation;
    private Button calculateButton;

    EditText inputFirstValue;
    EditText inputSecondValue;

    TextView resultView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get all radiobuttons
        Button addButton = findViewById(R.id.radio_addition);
        Button subButton = findViewById(R.id.radio_subtraction);
        Button multButton = findViewById(R.id.radio_multiplication);
        Button divButton = findViewById(R.id.radio_division);

        //add an eventlistener to all buttons
        setupButtonClickListener(addButton, '+');
        setupButtonClickListener(subButton, '-');
        setupButtonClickListener(multButton, '*');
        setupButtonClickListener(divButton, '/');

        resultView = findViewById(R.id.text_result);

        this.calculateButton = findViewById(R.id.button_calculate);
        this.inputFirstValue = findViewById(R.id.edit_text_first);
        this.inputSecondValue = findViewById(R.id.edit_text_second);
        calculateButton.setOnClickListener(new View.OnClickListener()  {
            public void onClick (View v)    {

                double firstValue = extractInputAndConvertToDouble(inputFirstValue);
                double secondValue = extractInputAndConvertToDouble(inputSecondValue);

                //calculate the result
                double result = performOperation(firstValue, secondValue);

                // make result visible
                resultView.setText(String.valueOf(result));
                if (result < 0) {
                    resultView.setTextColor(Color.RED);
                } else {
                    resultView.setTextColor(Color.BLACK);
                }
            }
        });

        Button msButton = findViewById(R.id.button_ms);
        msButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputFirstValue = findViewById(R.id.edit_text_first);
                EditText inputSecondValue = findViewById(R.id.edit_text_second);
                double firstValue = extractInputAndConvertToDouble(inputFirstValue);
                double secondValue = extractInputAndConvertToDouble(inputSecondValue);

                //save the values
                storeInputIntoSharedPreferences(firstValue, secondValue);
            }
        });

        Button mrButton = findViewById(R.id.button_mr);
        mrButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view)  {
                double[] retrievedValues = retrieveStoredValuesFromSharedPreferences();
                inputFirstValue.setText(String.valueOf(retrievedValues[0]));
                inputSecondValue.setText(String.valueOf(retrievedValues[1]));
            }
        });

        resultView.setOnTouchListener(new View.OnTouchListener()  {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    resultView.setText("0");
                    return true;
                }
                return false;
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

    private void storeInputIntoSharedPreferences(double firstValue, double secondValue)  {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);


        SharedPreferences.Editor edit = sharedPreferences.edit();
        // put our input in the editor
        edit.putString("firstValue", String.valueOf(firstValue));
        edit.putString("secondValue", String.valueOf(secondValue));
        //apply the changes -> they'll get saved
        edit.apply();

        //show toast to user
        Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    private double[] retrieveStoredValuesFromSharedPreferences()    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        double[] values = new double[2];
        String firstValueString = sharedPreferences.getString("firstValue", "");
        String secondValueString = sharedPreferences.getString("secondValue", "");
        if (firstValueString.isEmpty() || secondValueString.isEmpty())  {
            // show warning-toast
            Toast toast = Toast.makeText(this, "There are no values saved!", Toast.LENGTH_LONG);
            toast.show();
            values[0] = 0;
            values[1] = 0;
        }   else {
            Toast toast = Toast.makeText(this, "Loaded values.", Toast.LENGTH_SHORT);
            toast.show();
            values[0] = Double.parseDouble(firstValueString);
            values[1] = Double.parseDouble(secondValueString);
        }
        return values;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.calculateButton.setBackgroundColor(Color.GREEN);
    }
}