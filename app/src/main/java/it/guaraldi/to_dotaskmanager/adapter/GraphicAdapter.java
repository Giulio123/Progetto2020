package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.guaraldi.to_dotaskmanager.R;

public class GraphicAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mValue;
    // 1
    public GraphicAdapter(Context context, List<Integer> value) {
        this.mContext = context;
        this.mValue = value;
    }

    // 2
    @Override
    public int getCount() {
        return 12;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int val = mValue.get(position);
        if(convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_list_graphic_view, null);
        }
        final TextView value = (TextView)convertView.findViewById(R.id.item_list_graphic);
        if(position<4)
            value.setText("pending: "+String.valueOf(val));
        else if(position>3&&position<8)
            value.setText("complete: "+String.valueOf(val));
        else
            value.setText("total: "+String.valueOf(val));
        return convertView;
    }

}
