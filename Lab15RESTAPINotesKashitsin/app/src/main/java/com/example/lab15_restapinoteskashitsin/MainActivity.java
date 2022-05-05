package com.example.lab15_restapinoteskashitsin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String key;
    Activity ctx;
    EditText inputLogin;
    EditText inputPass;
    CheckBox isRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputLogin = findViewById(R.id.input_login);
        inputPass = findViewById(R.id.input_password);
        isRemember = findViewById(R.id.cb_remember);
        g.db = new DB(this, "notesCred.db", null, 1);

        ctx = this;
    }

    @Override
    public void onStart(){//Кашицын, 393
        super.onStart();
        String inputLoginMes = g.db.getCredUser();
        inputLogin.setText(inputLoginMes);
        inputPass.setText(g.db.getCredPass());
    }

    public void onLoginClick(View v)
    {
        String user = inputLogin.getText().toString();
        String password = inputPass.getText().toString();

        getSession(user, password);
    }

    public void getSession(String user, String password)//Кашицын,393
    {
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("null"))
                {
                    Toast.makeText(ctx, "Не удалось войти в аккаунт", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    if (isRemember.isChecked())
                        g.db.insertCred(inputLogin.getText().toString(), inputPass.getText().toString());
                    else
                        g.db.deleteCred();
                    key = res.replace("\"", "");
                    Intent intent = new Intent(ctx, ListActivity.class);
                    intent.putExtra("Session", key);
                    startActivity(intent);
                }
            }
        };
        abc.send("http://v1.fxnode.ru:8084/rpc/" + "open_session",
                "{ \"usr\": \"" + user + "\", \"pass\": \"" + password + "\"}");
    }

    public void onSignUpClick(View v)
    {
        Intent intent = new Intent(ctx, RegistrationActivity.class);
        startActivityForResult(intent, 555);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //Кашицын,393
        if (requestCode == 555)
            if (resultCode == RESULT_OK)
                    getSession(data.getStringExtra("Login"), data.getStringExtra("Password"));
    }






}