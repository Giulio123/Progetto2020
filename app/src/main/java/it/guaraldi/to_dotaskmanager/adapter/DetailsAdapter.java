package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import it.guaraldi.to_dotaskmanager.R;

public class DetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDataTask;

    public DetailsAdapter(Context context, List<String> data){
        mContext = context;
        mDataTask = data;
    }

    @Override
    public int getCount() {
        return mDataTask.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view== null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.item_list_details_view, null);
        }
        final TextView detailValue = (TextView)view.findViewById(R.id.item_list_details);
        detailValue.setText(mDataTask.get(i));
        if(i==0)
            detailValue.setTextSize(20);
        return view;
    }
}
