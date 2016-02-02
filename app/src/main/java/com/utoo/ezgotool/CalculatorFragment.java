package com.utoo.ezgotool;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utoo.database.MyDatabaseHelper;

/**
 * Created by wangdi on 2016/1/28.
 */
public class CalculatorFragment extends Fragment {
    private EditText importPrice;
    private EditText outletPrice;
    private EditText tradeName;
    private EditText currRate;
    private EditText exchangeRate;
    private EditText expressFee;
    private EditText tradePrice;
    private EditText profit;
    private Button calculate;
    private Button reverseOutlet;
    private Button reverseProfit;
    private FloatingActionButton addData;

    private TextView preOutletPrice;
    private TextView preTradePrice;
    private TextView preProfitPrice;

    private MyDatabaseHelper dbHelper;

    private String tempProfit;
    private String tempTradePrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        preOutletPrice = (TextView) rootView.findViewById(R.id.pre_outlet_text);
        preTradePrice = (TextView) rootView.findViewById(R.id.pre_trade_price_text);
        preProfitPrice = (TextView) rootView.findViewById(R.id.pre_profit_text);
        importPrice = (EditText) rootView.findViewById(R.id.import_price);
        importPrice.setOnFocusChangeListener(new editTextOnFocusListener());
        importPrice.requestFocus();
        outletPrice = (EditText) rootView.findViewById(R.id.outlet_price);
        outletPrice.setOnFocusChangeListener(new editTextOnFocusListener());
        tradeName = (EditText) rootView.findViewById(R.id.trade_edit);
        currRate = (EditText) rootView.findViewById(R.id.exchange_rate_now);
        currRate.setOnFocusChangeListener(new editTextOnFocusListener());
        exchangeRate = (EditText) rootView.findViewById(R.id.exchange_rate);
        exchangeRate.setOnFocusChangeListener(new editTextOnFocusListener());
        expressFee = (EditText) rootView.findViewById(R.id.express_fee);
        expressFee.setOnFocusChangeListener(new editTextOnFocusListener());
        tradePrice = (EditText) rootView.findViewById(R.id.trade_price);
        tradePrice.setOnFocusChangeListener(new editTextOnFocusListener());
        profit = (EditText) rootView.findViewById(R.id.profit);
        profit.setOnFocusChangeListener(new editTextOnFocusListener());
        calculate = (Button) rootView.findViewById(R.id.calculate);
        calculate.setOnClickListener(new calculateOnClickListenr());
        reverseOutlet = (Button) rootView.findViewById(R.id.reverse_outlet);
        reverseOutlet.setOnClickListener(new reverseOutletOnClickListener());
        reverseProfit = (Button) rootView.findViewById(R.id.reverse_profit);
        reverseProfit.setOnClickListener(new reverseProfitOnClickListener());
        addData = (FloatingActionButton) rootView.findViewById(R.id.add_data);
        addData.setOnClickListener(new addDataOnClickListener());

        initData();
        dbHelper = new MyDatabaseHelper(getContext(), "product_info", null, 1);

        return rootView;
    }

    class calculateOnClickListenr implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(importPrice.getText()) || TextUtils.isEmpty(outletPrice.getText()) ||
                    TextUtils.isEmpty(currRate.getText()) || TextUtils.isEmpty(exchangeRate.getText()) || TextUtils.isEmpty(expressFee.getText())) {
                Toast.makeText(getContext(), R.string.tips, Toast.LENGTH_SHORT).show();
                return;
            }

            float importPriceNum = Float.parseFloat(importPrice.getText().toString());
            float outletPriceNum = Float.parseFloat(outletPrice.getText().toString());
            float currRateNum = Float.parseFloat(currRate.getText().toString());
            float exchangeRateNum = Float.parseFloat(exchangeRate.getText().toString());
            float expressFeeNum = Float.parseFloat(expressFee.getText().toString());

            float tradePriceNum = (outletPriceNum + expressFeeNum) * exchangeRateNum;
            float profitNum = (outletPriceNum + expressFeeNum) * exchangeRateNum - (importPriceNum + expressFeeNum) * currRateNum;

            tradePrice.setText(String.format("%.2f", tradePriceNum));
            profit.setText(String.format("%.2f", profitNum));
            tempProfit = String.format("%.2f", profitNum);
            tempTradePrice = String.format("%.2f", tradePriceNum);

            outletPrice.setTextColor(getResources().getColor(R.color.black));
            tradePrice.setTextColor(getResources().getColor(R.color.black));
            profit.setTextColor(getResources().getColor(R.color.black));

            preOutletPrice.setText("");
            preProfitPrice.setText("");
            preTradePrice.setText("");

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    class reverseOutletOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(profit.getText())) {
                Toast.makeText(getContext(), R.string.tips, Toast.LENGTH_SHORT).show();
                return;
            }

            preOutletPrice.setText(outletPrice.getText());
            preTradePrice.setText(tradePrice.getText());
            preProfitPrice.setText(tempProfit+"");
            tempProfit = tradePrice.getText().toString();

            float importPriceNum = Float.parseFloat(importPrice.getText().toString());
            float currRateNum = Float.parseFloat(currRate.getText().toString());
            float exchangeRateNum = Float.parseFloat(exchangeRate.getText().toString());
            float expressFeeNum = Float.parseFloat(expressFee.getText().toString());

            float profitNum = Float.parseFloat(profit.getText().toString());
            float outletPriceNum = (profitNum + (importPriceNum + expressFeeNum) * currRateNum) / exchangeRateNum - expressFeeNum;
            float tradePriceNum = (outletPriceNum + expressFeeNum) * exchangeRateNum;

            outletPrice.setText(String.format("%.2f", outletPriceNum));
            tradePrice.setText(String.format("%.2f", tradePriceNum));
            outletPrice.setTextColor(getResources().getColor(R.color.red));
            tradePrice.setTextColor(getResources().getColor(R.color.red));



            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    class reverseProfitOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(tradePrice.getText())) {
                Toast.makeText(getContext(), R.string.tips, Toast.LENGTH_SHORT).show();
                return;
            }

            preOutletPrice.setText(outletPrice.getText());
            preProfitPrice.setText(profit.getText());
            preTradePrice.setText(tempTradePrice+"");
            tempTradePrice = tradePrice.getText().toString();

            float importPriceNum = Float.parseFloat(importPrice.getText().toString());
            float currRateNum = Float.parseFloat(currRate.getText().toString());
            float exchangeRateNum = Float.parseFloat(exchangeRate.getText().toString());
            float expressFeeNum = Float.parseFloat(expressFee.getText().toString());

            float tradePriceNum = Float.parseFloat(tradePrice.getText().toString());
            float outletPriceNum = tradePriceNum / exchangeRateNum - expressFeeNum;
            float profitNum = (outletPriceNum + expressFeeNum) * exchangeRateNum - (importPriceNum + expressFeeNum) * currRateNum;

            outletPrice.setText(String.format("%.2f", outletPriceNum));
            profit.setText(String.format("%.2f", profitNum));

            outletPrice.setTextColor(getResources().getColor(R.color.red));
            profit.setTextColor(getResources().getColor(R.color.red));

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    class addDataOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(tradeName.getText())) {
                Toast.makeText(getContext(), R.string.tips, Toast.LENGTH_SHORT).show();
                return;
            }
            dbHelper.insert(dbHelper.getReadableDatabase(), tradeName.getText().toString(), importPrice.getText().toString(), outletPrice.getText().toString(),
                    currRate.getText().toString(), exchangeRate.getText().toString(), expressFee.getText().toString(), tradePrice.getText().toString(), profit.getText().toString());

            //update history list
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            HistoryListFragment historyListFragment = (HistoryListFragment) fm.findFragmentById(R.id.fragment_history);
//            historyListFragment.updateListView();


        }
    }

    class editTextOnFocusListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                EditText editText = (EditText)v;
                editText.setText("");
            }
            else{
                EditText editText = (EditText)v;
                String s = editText.getText().toString();
                if(TextUtils.isEmpty(s))
                    s = 0+"";
                editText.setText(String.format("%.2f", Float.parseFloat(s)));
            }
        }
    }

    private void initData() {
        SharedPreferences sp = getActivity().getSharedPreferences("default", getActivity().MODE_PRIVATE);
        currRate.setText(sp.getString("curr_rate", "4.70"));
        exchangeRate.setText(sp.getString("exc_rate", "5.20"));
        expressFee.setText(sp.getString("express_fee", "6.50"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
