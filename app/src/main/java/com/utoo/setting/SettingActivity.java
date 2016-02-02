package com.utoo.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.utoo.ezgotool.MainActivity;
import com.utoo.ezgotool.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, Object>> listItems;
    private SimpleAdapter simpleAdapter;

    private String curr_rate;
    private String exc_rate;
    private String express_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_shadow);

        listItems = new ArrayList<Map<String, Object>>();
        Map<String, Object> listItem = new HashMap<String, Object>();
        listItem.put("title", getResources().getString(R.string.exchange_rate_now));
        listItem.put("sub_title", curr_rate);
        listItems.add(listItem);
        listItem = new HashMap<String, Object>();
        listItem.put("title", getResources().getString(R.string.exchange_rate));
        listItem.put("sub_title", exc_rate);
        listItems.add(listItem);
        listItem = new HashMap<String, Object>();
        listItem.put("title", getResources().getString(R.string.express_fee));
        listItem.put("sub_title", express_fee);
        listItems.add(listItem);


        simpleAdapter = new SimpleAdapter(this, listItems, R.layout.setting_list_item,
                new String[]{"title", "sub_title"}, new int[]{R.id.title, R.id.sub_title});
        listView = (ListView) findViewById(R.id.settingList);

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:  //language
                        languageSet(position);
                        break;

                    case 1:
                        languageSet(position);
                        break;

                    case 2:
                        languageSet(position);
                        break;
                }
            }
        });
    }

    private void languageSet(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.input_layout, null);
        if (position == 0)
            dialog.setTitle(R.string.exchange_rate_now);
        else if (position == 1)
            dialog.setTitle(R.string.exchange_rate);
        else if (position == 2)
            dialog.setTitle(R.string.express_fee);
        dialog.setView(view);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) view.findViewById(R.id.input_edit);
                String s = editText.getText().toString();
                if (TextUtils.isEmpty(s)){
                    s = "0";
                }
                String str = String.format("%.2f", Float.parseFloat(s));

                SharedPreferences sp = getSharedPreferences("default", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (position == 0)
                    editor.putString("curr_rate", str);
                else if (position == 1)
                    editor.putString("exc_rate", str);
                else if (position == 2)
                    editor.putString("express_fee", str);
                editor.commit();

                Map<String, Object> map = listItems.get(position);
                map.put("sub_title", str);
                listItems.set(position, map);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton(R.string.no, null);
        dialog.create().show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("default", MODE_PRIVATE);
        curr_rate = sp.getString("curr_rate", "4.70");
        exc_rate = sp.getString("exc_rate", "5.20");
        express_fee = sp.getString("express_fee", "6.50");
    }

}
