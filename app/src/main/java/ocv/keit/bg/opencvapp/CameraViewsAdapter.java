package ocv.keit.bg.opencvapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;

public class CameraViewsAdapter extends BaseAdapter {

    private Context mContext;
    private MainActivity mAct;
    private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;

    public CameraViewsAdapter(Activity context, ArrayList<String> items) {
        mContext = context;
        mAct = (MainActivity)context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CameraViewsAdapter(Context context,LayoutInflater inflater, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        if (mAct.mFaces!=null) {
            return mAct.mFaces.length;
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return mAct.mFaces[i];
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
        mibut.setText("Train >");
        Mat mat=new Mat (mAct.lastImage,(Rect)getItem(i));
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);

        // find the imageview and draw it!
        ImageView iv = (ImageView) rowView.findViewById(R.id.recipe_list_thumbnail);
        iv.setImageBitmap(bm);
        titleTextView.setText("dsas");

        return rowView;
    }
}
