package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.ui.edittask.EditTaskFragment;

public class SpinnerAdapter extends BaseAdapter {
    private String typeData;
    private Context context;
    private TypedArray colorsImage;
    private TypedArray colorsName;
    private List<String> data;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context application, TypedArray colorsImage, TypedArray colorsName) {
        context = application;
        this.inflater = LayoutInflater.from(context);
        this.colorsImage = colorsImage;
        this.colorsName = colorsName;
    }

    public SpinnerAdapter(Context application, List<String> data, String typeData ){
        this.context = application;
        this.inflater = LayoutInflater.from(context);
        this.typeData = typeData;
        this.data = data;
    }

    @Override
    public int getCount() {
        return colorsName!=null?colorsName.length():data.size();
    }

    @Override
    public Object getItem(int position) {
        return colorsName!=null ? colorsName.getResourceId(position,-1) : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //COLOR
        if(colorsImage!=null && colorsImage.length() > 0){
            convertView = inflater.inflate(R.layout.edit_task_color_spinner_item,null);
            ImageView img = convertView.findViewById(R.id.imageColor);
            TextView colorName = convertView.findViewById(R.id.nameColor);
            img.setImageResource(colorsImage.getResourceId(position,-1));
            colorName.setText(colorsName.getResourceId(position,-1));
        }
        if(data!=null && data.size() > 0) {
            //PRIORITY
            if(typeData.contains("PRIORITY")) {
                convertView = inflater.inflate(R.layout.edit_task_priority_spinner_item,null);
                TextView value = convertView.findViewById(R.id.priority_item);
                value.setText(data.get(position));
            }
            //CATEGORY
            else{
                convertView = inflater.inflate(R.layout.edit_task_category_spinner_item,null);
                TextView value = convertView.findViewById(R.id.category_item);
                value.setText(data.get(position));

            }
        }
        return convertView;
    }
}
