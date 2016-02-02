package com.utoo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.utoo.ezgotool.R;

/**
 * Created by wangdi on 2016/2/1.
 */
public class DetailDialog extends Dialog{
    private Context context;
    private String product;

    public DetailDialog(Context context, String product) {
        super(context);
        this.context = context;
        this.product = product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_detail_layout, null);
        setContentView(layout);
        TextView productText = (TextView) layout.findViewById(R.id.trade_name);

    }


}
