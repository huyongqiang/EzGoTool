package com.utoo.ezgotool;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.utoo.database.MyDatabaseHelper;
import com.utoo.ui.DetailDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdi on 2016/1/28.
 */
public class HistoryListFragment extends Fragment {
    private MyDatabaseHelper dbHelper;
    private ListView listView;
    private List<Map<String, String>> listItems;
    private SimpleAdapter simpleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_historylist, container, false);


        dbHelper = new MyDatabaseHelper(getContext(), "product_info", null, 1);
        Cursor cursor = dbHelper.findAll(dbHelper.getReadableDatabase());

        listItems = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()){
            Map<String, String> listItem = new HashMap<String, String>();
            listItem.put("product", cursor.getString(1));
            listItem.put("sale", cursor.getString(3));
            listItem.put("profit", cursor.getString(8));
            listItem.put("final_price", cursor.getString(7));
            listItems.add(listItem);
        }

        simpleAdapter = new SimpleAdapter(getContext(), listItems, R.layout.history_list_item,
                new String[]{"product", "sale", "profit", "final_price"}, new int[]{R.id.product_name, R.id.sale_text, R.id.profit_text, R.id.final_price_text});
        listView = (ListView) rootView.findViewById(R.id.history_list);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("%d", position+"");
                showDetail(position+1);

            }
        });


        return rootView;
    }

    public void updateListView(){
       // simpleAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(),R.string.tips_add_data, Toast.LENGTH_SHORT).show();
    }

    public void showDetail(int position){
        Cursor cursor = dbHelper.findById(dbHelper.getReadableDatabase(), position);
//        Map<String, String> listItem = new HashMap<String, String>();
//        listItem.put("product", cursor.getString(1));
//        listItem.put("sale", cursor.getString(3));
//        listItem.put("profit", cursor.getString(8));
//        listItem.put("final_price", cursor.getString(7));

        AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        builder.show();
        builder.setTitle(R.string.detail);
        Window window = builder.getWindow();
        window.setContentView(R.layout.dialog_detail_layout);
        TextView productText = (TextView) window.findViewById(R.id.trade_name);
        productText.setText(cursor.getString(1));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
