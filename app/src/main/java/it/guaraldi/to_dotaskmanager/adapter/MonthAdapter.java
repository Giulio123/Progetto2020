package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import it.guaraldi.to_dotaskmanager.R;

public class MonthAdapter extends BaseAdapter {
    private String[] mData;
    private LayoutInflater mInflater;

    public MonthAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.form_mid_portion_month_spinner_item,null);
        TextView tv = convertView.findViewById(R.id.month_spinner_item_tv);
        tv.setText(mData[position]);
        return convertView;
    }
}
