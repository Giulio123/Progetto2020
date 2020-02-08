package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import it.guaraldi.to_dotaskmanager.R;

public class PeriodAdapter extends BaseAdapter {


    private String[] mData;
    private LayoutInflater inflater;

    public PeriodAdapter(Context context, String[] mData) {
        this.inflater = LayoutInflater.from(context);
        this.mData = mData;
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
        convertView = inflater.inflate(R.layout.form_start_portion_period_type_spinner_item,null);
        TextView periodTypeText = convertView.findViewById(R.id.period_type_text);
        Log.d("PeriodAdapter", "getView: position:"+position);
        periodTypeText.setText(mData[position]);
        return convertView;
    }
}
