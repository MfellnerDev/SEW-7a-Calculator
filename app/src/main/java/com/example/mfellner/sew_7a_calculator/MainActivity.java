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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private char calcOperation;
    private Button calculateButton;

    private EditText inputFirstValue;
    private EditText inputSecondValue;

    private TextView resultView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make the radio buttons ready for use
        setupRadioButtons();

        this.resultView = findViewById(R.id.text_result);
        this.calculateButton = findViewById(R.id.button_calculate);
        this.inputFirstValue = findViewById(R.id.edit_text_first);
        this.inputSecondValue = findViewById(R.id.edit_text_second);

        // event listener for the calculate button - execute the main event
        calculateButton.setOnClickListener(new View.OnClickListener()  {
            public void onClick (View v)    {
                // get and extract the numeric value out of the field
                double firstValue = parseStringToDouble(inputFirstValue.getText().toString());
                double secondValue = parseStringToDouble(inputSecondValue.getText().toString());

                //calculate the result
                double result = performCalculation(firstValue, secondValue);

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
                double firstValue = parseStringToDouble(inputFirstValue.getText().toString());
                double secondValue = parseStringToDouble(inputSecondValue.getText().toString());

                //save the values
                saveInputToSharedPreferences(firstValue, secondValue);
            }
        });

        Button mrButton = findViewById(R.id.button_mr);
        mrButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view)  {
                // retrieve the stored values and update the EditText view components
                Map<String, Double> retrievedValues = retrieveStoredValuesFromSharedPreferences();
                inputFirstValue.setText(String.valueOf(retrievedValues.get("firstValue")));
                inputSecondValue.setText(String.valueOf(retrievedValues.get("secondValue")));
            }
        });

        resultView.setOnTouchListener(new View.OnTouchListener()  {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    resultView.setText("0");
                    resultView.setTextColor(Color.BLACK);
                    return true;
                }
                return false;
            }
        });
    }

    private void setupRadioButtons()    {
        //get all radiobuttons
        Button addButton = findViewById(R.id.radio_addition);
        Button subButton = findViewById(R.id.radio_subtraction);
        Button multButton = findViewById(R.id.radio_multiplication);
        Button divButton = findViewById(R.id.radio_division);

        //add an eventlistener to all buttons
        setupRadioButtonClickListener(addButton, '+');
        setupRadioButtonClickListener(subButton, '-');
        setupRadioButtonClickListener(multButton, '*');
        setupRadioButtonClickListener(divButton, '/');
    }

    /**
     * Parse a given String into a double variable
     * @param textToParse the text that should get parsed
     * @return a double containing the text
     */
    private double parseStringToDouble(String textToParse)  {
        try {
            return Double.parseDouble(textToParse);
        }   catch (NumberFormatException numFoEx)    {
            System.err.println("Error! " + numFoEx.getMessage());
            return 0;
        }
    }

    /**
     * Sets the clicklistener for our radio button - also decides, which operation (char)
     * is selected by the user
     * @param radioButton radio button
     * @param selectedCalcOperation the operation that should be set
     */
    private void setupRadioButtonClickListener(Button radioButton, char selectedCalcOperation) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcOperation = selectedCalcOperation;
                Log.i("HAIII", String.valueOf(calcOperation));
            }
        });
    }



    /**
     * Takes the values and the calc operation and performs the calculation
     * @param firstValue first double value
     * @param secondValue second double value
     * @return the result or 0
     */
    private double performCalculation(double firstValue, double secondValue)    {
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

    /**
     * Saves the user input to the (private) shared preferences
     * @param firstValue first double value
     * @param secondValue second double value
     */
    private void saveInputToSharedPreferences(double firstValue, double secondValue)  {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        // put our input in the editor
        edit.putString("firstValue", String.valueOf(firstValue));
        edit.putString("secondValue", String.valueOf(secondValue));
        //apply the changes -> they'll get saved
        edit.apply();

        //show toast to user
        this.showShortToast("Saved");
    }

    /**
     * Retrieves stored values from (private) shared preferences and stores them into a map
     * @return a map containing the retrieved first and second value
     */
    private Map<String, Double> retrieveStoredValuesFromSharedPreferences()    {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        // get values out of shared preferences
        String firstValueString = sharedPreferences.getString("firstValue", "");
        String secondValueString = sharedPreferences.getString("secondValue", "");

        Map<String, Double> retrievedValues = new HashMap<>();

        // check if there are values stored
        if (firstValueString.isEmpty() || secondValueString.isEmpty())  {
            // show warning-toast
            this.showShortToast("There are no values saved!");
            retrievedValues.put("firstValue", 0.0);
            retrievedValues.put("secondValue", 0.0);
        }   else {
            retrievedValues.put("firstValue", parseStringToDouble(firstValueString));
            retrievedValues.put("secondValue", parseStringToDouble(secondValueString));
            this.showShortToast("Loaded values.");
        }
        return retrievedValues;
    }

    private void showShortToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * If the app is in focus -> set the calculate button color to green
     */
    @Override
    public void onResume() {
        super.onResume();
        this.calculateButton.setBackgroundColor(Color.GREEN);
    }
}