package edu.qc.seclass.glm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class MyExListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> titles;
    private Map<String, ArrayList<String>> internals;

    public MyExListAdapter(Context context, ArrayList<String> titles, HashMap<String, ArrayList<String>> internals ){
        this.context = context;
        this.titles = titles;
        this.internals = internals;
    }

    @Override
    public int getGroupCount(){
        return titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return internals.get(titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return internals.get(titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String title = (String) getGroup(groupPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listparent,null);
        }


        TextView txtParent = (TextView) convertView.findViewById(R.id.txtParent);
        txtParent.setText(capitalize(title));


        ImageView optionsMenu = (ImageView) convertView.findViewById(R.id.optionsMenu);
        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(v.getId()) {
                    case R.id.optionsMenu:

                        PopupMenu popup = new PopupMenu(context.getApplicationContext(), v);
                        popup.getMenuInflater().inflate(R.menu.reminderlist_options,
                                popup.getMenu());
                        popup.show();

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent refresh = new Intent(context, user.class);

                                switch (item.getItemId()) {
                                    //Cursor res;
                                    case R.id.checkOffAll:
                                        Cursor res = user.db.getAllReminderData();
                                        while(res.moveToNext())
                                        {
                                            if(res.getString(2).equals(title))
                                            {
                                                user.db.updateStatus(res.getInt(0),"TRUE");
                                            }
                                        }

                                        context.startActivity(refresh);
                                        break;
                                    case R.id.uncheckAll:
                                        Cursor res2 = user.db.getAllReminderData();
                                        while(res2.moveToNext())
                                        {
                                            if(res2.getString(2).equals(title))
                                            {
                                                user.db.updateStatus(res2.getInt(0),"FALSE");
                                            }
                                        }
                                        context.startActivity(refresh);
                                        break;
                                    case R.id.deleteList:
                                        user.db.deleteFromList(title);
                                        refresh();
                                        break;

                                    default:
                                        break;
                                }

                                return true;
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listchild, null);
        }

        boolean flag=false;
        final String reminder =(String) getChild(groupPosition,childPosition);
        final String desc = internals.get(titles.get(groupPosition)).get(childPosition);
        final int childPosition2 = childPosition;
        final int groupPosition2 = groupPosition;

        Button btnEdit = (Button)convertView.findViewById(R.id.editButton);
        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Cursor res = user.db.getAllReminderData();
                Bundle b = new Bundle();
                while (res.moveToNext()) {
                    if (res.getString(1).equals(desc)&&res.getString(2).equals(titles.get(groupPosition2))) {
                        b.putString("value3",res.getString(6));
                        b.putString("value4",res.getString(7));
                        b.putString("value5",res.getString(3));
                        break;
                    }
                }
                b.putString("value", desc);
                b.putString("value2",titles.get(groupPosition2));
                Intent in = new Intent(context, reminder.class);
                in.putExtras(b);
                context.startActivity(in);
            }
        });

        Button btnDelete = (Button)convertView.findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                user.db.delete(reminder);
                if(getChildrenCount(groupPosition2)-1<1)
                    user.db.deleteFromList(titles.get(groupPosition2));
                    refresh();

            }
        });

        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        Cursor res = user.db.getAllReminderData();
        int id=-1;
        while(res.moveToNext())
        {
            if(res.getString(1).equals(internals.get(titles.get(groupPosition2)).get(childPosition2))&& res.getString(2).equals(titles.get(groupPosition2))&&res.getString(3).equals("TRUE"))
            {
                checkBox.setChecked(true);
                flag=true;
                id=res.getInt(0);
                break;
            }
            else if(res.getString(1).equals(internals.get(titles.get(groupPosition2)).get(childPosition2))&& res.getString(2).equals(titles.get(groupPosition2))&& res.getString(3).equals("FALSE"))
            {
                checkBox.setChecked(false);
                flag=false;
                id=res.getInt(0);
                break;
            }
        }
        final boolean flag2=flag;
        final int id2 = id;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(flag2==true)
                {
                    user.db.updateStatus(id2,"FALSE");
                }
                else
                {
                    user.db.updateStatus(id2,"TRUE");
                }
                refresh();
            }
        });

        TextView txtChild = (TextView) convertView.findViewById(R.id.txtChild);
        txtChild.setText(capitalize(reminder));

        if(flag2) {
            txtChild.setPaintFlags(txtChild.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtChild.setTextColor(Color.GRAY);
        }
        else {
            txtChild.setPaintFlags(txtChild.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            txtChild.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private String capitalize(String str) {
        return  (str.substring(0, 1).toUpperCase() + str.substring(1));
    }

    private void refresh() {
        titles = user.db.getValues();
        internals = user.db.getInternals();
        notifyDataSetChanged();
    }
}
