package it.mirea.mypolitopia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.mirea.mypolitopia.Map.Field;
import it.mirea.mypolitopia.Map.Map;
import it.mirea.mypolitopia.Map.Npc;

public class MainActivity extends AppCompatActivity {

    private TextView editText;
    private Button button;
    private Map letsplay;
    private Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (TextView) findViewById(R.id.array);
        button = (Button) findViewById(R.id.button);
        letsplay = new Map(Map.MAP_SIZE.MEDIUM, 2, Npc.Species.SNIGGERS);
        letsplay.generateRivers();
        letsplay.generateMountains();
        letsplay.generateTrees();
        field = new Field();
        field.setInstance(letsplay.getField());
        letsplay.generateCity(2);
        field.setInstance(letsplay.getField());
        editText.setText(letsplay.show());
    }


    public void onClick(View view) {
        letsplay.generateRivers();
        letsplay.generateMountains();
        letsplay.generateTrees();
        field.setInstance(letsplay.getField());
        letsplay.generateCity(2);
        editText.setText(letsplay.show());
        field.setInstance(letsplay.getField());
    }


    public void onShow(View view) {
        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        MainActivity.this.startActivity(intent);
    }
}