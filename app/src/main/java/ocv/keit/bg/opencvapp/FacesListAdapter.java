package ocv.keit.bg.opencvapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

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
         if(mContext.getLastFaces()!=null)
            return mContext.getLastFaces().length;
         else
             return 0;
    }

    @Override
    public Object getItem(int i) {
        if (mContext.getLastImage()!=null)
            return new Mat(mContext.getLastImage(),mContext.getLastFaces()[i]);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cameralist_layout, viewGroup, false);
        final AutoCompleteTextView titleTextView =
                (AutoCompleteTextView) rowView.findViewById(R.id.autoCompleteTextView);
        Button mibut=(Button) rowView.findViewById(R.id.recipe_list_detail);
        mibut.setText("Train >");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_dropdown_item_1line, mContext.getmPeople());

        titleTextView.setAdapter(adapter);



        Mat mat=(Mat) getItem(i);
        if (mat!=null) {
            Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(mat, bm);


            // find the imageview and draw it!
            ImageView iv = (ImageView) rowView.findViewById(R.id.recipe_list_thumbnail);
            iv.setImageBitmap(bm);
        }
        mibut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!titleTextView.getText().toString().isEmpty()){
                    mContext.trainRecognizer(titleTextView.getText().toString(),mContext.getLastFaces()[i]);
                    mDataSource.remove(i);
                }
            //    Toast.makeText(mContext, titleTextView.getText(),Toast.LENGTH_LONG).show();
            }
        });
      //  titleTextView.setText((String) getItem(i));

        return rowView;
    }

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
}
