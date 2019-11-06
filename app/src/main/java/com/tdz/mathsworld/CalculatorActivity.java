package com.tdz.mathsworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {
    EditText firstNum, secondNum;
    TextView firstN, secondN, tvAns;

    int num1, num2, ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        firstN = findViewById(R.id.firstN);
        firstNum = findViewById(R.id.firstNum);
        secondN = findViewById(R.id.secondN);
        secondNum = findViewById(R.id.secondNum);
        tvAns = findViewById(R.id.tvAns);

    }

    public void plusNum(View view) {
        num1 = Integer.parseInt(firstNum.getText().toString());
        num2 = Integer.parseInt(firstNum.getText().toString());
        ans = num1 + num2;
        System.out.println(num1 + " ########################## " + num2 + " is " + ans);
        String a = Integer.toString(ans);
        tvAns.setText(a);
        ans = 0;
    }

    public void minusNum(View view) {
        num1 = Integer.parseInt(firstNum.getText().toString());
        num2 = Integer.parseInt(firstNum.getText().toString());
        ans = num1 - num2;
        System.out.println(num1 + " ########################## " + num2);
        String a = Integer.toString(ans);
        tvAns.setText(a);
        ans = 0;
    }

    public void mulNum(View view) {
        num1 = Integer.parseInt(firstNum.getText().toString());
        num2 = Integer.parseInt(firstNum.getText().toString());
        ans = num1 * num2;
        System.out.println(num1 + " ########################## " + num2);
        String a = Integer.toString(ans);
        tvAns.setText(a);
        ans = 0;
    }

    public void divNum(View view) {
        num1 = Integer.parseInt(firstNum.getText().toString());
        num2 = Integer.parseInt(firstNum.getText().toString());
        ans = num1 / num2;
        System.out.println(num1 + " ########################## " + num2);
        String a = Integer.toString(ans);
        tvAns.setText(a);
        ans = 0;
    }
}