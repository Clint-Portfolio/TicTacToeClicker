package com.example.clintnieuwendijk.tictactoeclicker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UpgradeActivity extends AppCompatActivity {

    UpgradeDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        db = UpgradeDatabase.getInstance(getApplicationContext());

        updateData();
    }

    private void updateData() {
        Cursor allEntries = db.selectAll();
        ListView lv = findViewById(R.id.upgradeListView);
        UpgradeAdapter upgradeAdapter = new UpgradeAdapter(getApplicationContext(), allEntries);

        lv.setAdapter(upgradeAdapter);
        lv.setOnItemClickListener(new OnItemClickListener());
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            db.updateUpgrade(l);
            updateData();
        }
    }

}


