package com.example.lab15_restapinoteskashitsin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity { //Кашицын,393
    Activity la;
    String session;
    ListView notesList;
    ArrayAdapter<Note> adp;
    ArrayList<Note> lst = new ArrayList<Note>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        notesList = findViewById(R.id.lv_notes);
        Intent intent = getIntent();
        session = intent.getStringExtra("Session");
        adp = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, lst);
        notesList.setAdapter(adp);
        la = this;
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note n = adp.getItem(position);
                Intent i = new Intent(ListActivity.this, EditNoteActivity.class);
                i.putExtra("Session", session);
                i.putExtra("Id", n.id);
                i.putExtra("Name", n.name);
                i.putExtra("Text", n.text);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        requestGetList();
    }

    void requestGetList()//Кашицын, 393
    {
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("[]"))
                {
                    Toast.makeText(la, "Заметок не найдено", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    try {
                        updateList(new JSONArray(res));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.e("result", res);
            }
        };

        abc.sendWithStop("http://v1.fxnode.ru:8084/rpc/" + "list_notes",
                "{ \"skey\": \"" + session + "\"}");
    }

    void updateList(JSONArray noteArray)//Кашицын,393
    {
        lst.clear();
        for (int i = 0; i < noteArray.length(); i++)
        {
            JSONObject noteJSON = null;
            try {
                noteJSON = new JSONObject(String.valueOf(noteArray.getJSONObject(i)));
                getTextNote(noteJSON.getInt("nid"), noteJSON.getString("ntitle"), i);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    void getTextNote(int id, String name, int place)//Кашицын,393
    {
        ApiHelper abcd = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("null"))
                {

                }
                else
                {
                    res = res.replace("\"", "");
                    g.notes = new Note();
                    g.notes.id = id;
                    g.notes.name = name;
                    g.notes.text = res;
                    lst.add(g.notes);
                    adp.notifyDataSetChanged();
                }
            }
        };
        abcd.send("http://v1.fxnode.ru:8084/rpc/" + "get_contents",
                "{ \"nid\": \"" + String.valueOf(id) + "\", \"skey\": \"" + session + "\"}");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)//Кашицын,393
    {
        switch (item.getItemId())
        {
            case R.id.item_add:
            {
                ApiHelper abc = new ApiHelper(this)
                {
                    @Override
                    public void on_ready(String res) {
                        if (res.equals("null"))
                        {
                            Toast.makeText(la, "Не удалось добавить заметку", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else
                        {
                            String idAdded = res.replace("\"", "");

                            Intent intent = new Intent(ListActivity.this, EditNoteActivity.class);
                            intent.putExtra("Session", session);
                            intent.putExtra("Id", Integer.valueOf(idAdded));
                            intent.putExtra("Name", "Название");
                            intent.putExtra("Text", "Текст");
                            startActivity(intent);

                        }
                    }
                };
                abc.send("http://v1.fxnode.ru:8084/rpc/" + "add_note",
                        "{ \"skey\": \"" + session + "\"}");break;
            }
            case R.id.item_logout:
            {
                finish();break; //Допилить
            }
        }
        return super.onOptionsItemSelected(item);
    }



}