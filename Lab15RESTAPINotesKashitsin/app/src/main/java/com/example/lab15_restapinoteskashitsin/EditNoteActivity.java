package com.example.lab15_restapinoteskashitsin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {
    String session;
    Integer idNote;
    String nameNote;
    String textNote;
    EditText inNameNote;
    EditText inTextNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        inNameNote = findViewById(R.id.input_name);
        inTextNote = findViewById(R.id.input_body);

        Intent intent = getIntent();
        session = intent.getStringExtra("Session");
        idNote = intent.getIntExtra("Id", 0);
        if (idNote == 0)
            finish();
        nameNote = intent.getStringExtra("Name");
        textNote = intent.getStringExtra("Text");
        inNameNote.setText(nameNote);
        inTextNote.setText(textNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_editnote, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_save:
            {
                ApiHelper abc = new ApiHelper(this)
                {
                    @Override
                    public void on_ready(String res) {
                        if (res.equals("null"))
                        {
                            Toast.makeText(EditNoteActivity.this, "Не удалось сохранить заметку", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            finish();
                        }
                    }
                };
                abc.send("http://v1.fxnode.ru:8084/rpc/" + "update_note",
                        "{ \"nid\": \"" + idNote + "\", \"nbody\": \"" + inTextNote.getText().toString() + "\"," +
                                " \"skey\": \"" + session + "\", \"ntitle\": \"" + inNameNote.getText().toString() + "\"}");break;
            }
            case R.id.item_notsave:
            {
                finish();break; //Допилить
            }

            case R.id.item_delete:
            {
                ApiHelper abc = new ApiHelper(this)
                {
                    @Override
                    public void on_ready(String res) {
                        if (res.equals("null"))
                        {
                            Toast.makeText(EditNoteActivity.this, "Не удалось удалить заметку", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            finish();
                        }
                    }
                };
                abc.send("http://v1.fxnode.ru:8084/rpc/" + "delete_note",
                        "{ \"nid\": \"" + idNote + "\", \"skey\": \"" + session + "\"}");break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

}