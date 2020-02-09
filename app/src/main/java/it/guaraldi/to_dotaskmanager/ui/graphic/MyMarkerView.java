package it.guaraldi.to_dotaskmanager.ui.graphic;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;

import it.guaraldi.to_dotaskmanager.R;

public class MyMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
    }
}
