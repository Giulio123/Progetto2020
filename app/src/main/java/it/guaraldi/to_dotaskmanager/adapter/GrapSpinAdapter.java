package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.guaraldi.to_dotaskmanager.R;

public class GrapSpinAdapter extends BaseAdapter {

    private Context mContext;
    private List<String>  mCategories;


    public GrapSpinAdapter(Context context, List<String> data){
        mContext = context;
        mCategories = data;
    }
    @Override
    public int getCount() {
        return mCategories.size();
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
        String category = mCategories.get(i);
        if(view == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.item_spinner_graphic_view, null);
        }
        final TextView categoryTV = (TextView)view.findViewById(R.id.item_spinner_graphic);
        categoryTV.setText(category);
        return view;
    }
}
