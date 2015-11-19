package de.inces.hackathonrweapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnUpdate;
    private ListView listItems;
    private TextView lblEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BatteryUpdateReceiver.registerUpdate(this.getApplicationContext());

        this.btnUpdate = (Button) this.findViewById(R.id.btnUpdate);
        this.listItems = (ListView) this.findViewById(R.id.listItems);
        this.lblEmpty = (TextView) this.findViewById(R.id.lblEmpty);

        this.listItems.setEmptyView(this.lblEmpty);

        this.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = (new BatteryDB(MainActivity.this.getApplicationContext())).getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM " + BatteryDB.UPDATE_TABLE, null);
                List<String> entries = new ArrayList<String>();
                if (c.moveToFirst()) {
                    do {
                        entries.add(c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2) + " - " + c.getString(3) + " - " + c.getString(4));
                    } while (c.moveToNext());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1, entries);
                listItems.setAdapter(adapter);
            }
        });
    }
}
