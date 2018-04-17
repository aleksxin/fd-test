package ocv.keit.bg.opencvapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FacesListAdapter extends BaseAdapter {

    private MainActivity mContext;
   // private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;

    public FacesListAdapter(MainActivity context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mContext.getLastFaces().length;
    }

    @Override
    public Object getItem(int i) {
        return .get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.cameralist_layout, viewGroup, false);
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.autoCompleteTextView);
        Button mibut=(Button) rowView.findViewById(R.id.recipe_list_detail);
        mibut.setText("Da duhash!");
        titleTextView.setText((String) getItem(i));

        return rowView;
    }
}
