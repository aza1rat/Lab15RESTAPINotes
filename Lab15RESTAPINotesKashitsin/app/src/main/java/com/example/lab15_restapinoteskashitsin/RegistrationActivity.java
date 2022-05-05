package com.example.lab15_restapinoteskashitsin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    EditText inputRegLogin;
    EditText inputRegPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        inputRegLogin = findViewById(R.id.input_reglogin);
        inputRegPass = findViewById(R.id.input_regpass);

    }

    public void onOkClick(View v)
    {
        String inLogin = inputRegLogin.getText().toString();
        String inPass = inputRegPass.getText().toString();
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (!(res.equals("true")))
                {
                    Toast.makeText(RegistrationActivity.this, "Не удалось зарегистрироваться в системе", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = getIntent();
                    intent.putExtra("Login", inputRegLogin.getText().toString());
                    intent.putExtra("Password", inputRegPass.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
        abc.sendWithStop("http://v1.fxnode.ru:8084/rpc/" + "reg_account",
                "{ \"usr\": \"" + inLogin + "\", \"pass\": \"" + inPass + "\"}");
    }

    public void onCancelClick(View v)
    {
        Intent intent = getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}