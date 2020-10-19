package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.view.View;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView=(ListView)findViewById(R.id.listView);
        Intent in = getIntent();
        final Bundle b = in.getExtras();
        int total = b.getInt("total");
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();
        for(int i=0;i<total;i++)
        {
            temp.add(b.getStringArrayList(Integer.toString(i)));
            temp2.add(b.getStringArrayList(Integer.toString(i)).get(0).toUpperCase()+"  is in the list:  "+b.getStringArrayList(Integer.toString(i)).get(1).toUpperCase());
        }
        final ArrayList<ArrayList<String>> finalTemp = temp;

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,temp2);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle b = new Bundle();
                b.putString("value",finalTemp.get(position).get(0));
                b.putString("value2",finalTemp.get(position).get(1));
                b.putString("value3",finalTemp.get(position).get(2));
                b.putString("value4",finalTemp.get(position).get(3));
                b.putString("value5",finalTemp.get(position).get(4));
                Intent in = new Intent(getApplicationContext(), reminder.class);
                in.putExtras(b);
                startActivity(in);

            }
        });


    }

    public void goMenu(View view) {
        Intent intent = new Intent(this, user.class);
        startActivity(intent);
    }
}
