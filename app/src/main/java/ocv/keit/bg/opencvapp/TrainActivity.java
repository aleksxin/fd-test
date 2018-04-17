package ocv.keit.bg.opencvapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainActivity extends Activity  implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String    TAG                 = "OCVSample::TrnActivity";
    private JavaCameraView mOpenCvCameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);

        ListView DynamicListView = new ListView(this);

        DynamicListView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.WRAP_CONTENT));
        final String[] DynamicListElements = new String[] {
                "Android",
                "PHP",
                "Android Studio",
               "Android Stfsdaio",
                "Android Studfsadio"
       //         "Android Studfsado",
       //         "Android Stufasddio",
          //      "Android Stufdsadio",
          //      "Android Studidsf",
          //      "PhpMyAdmin"
        };

        FacesListAdapter adapter = new FacesListAdapter(this, new ArrayList<String>(Arrays.asList(DynamicListElements)));
        DynamicListView.setAdapter(adapter);

     //   ArrayAdapter<String> adapter = new ArrayAdapter<String>()
     //           (TrainActivity.this, android.R.layout.simple_list_item_1, DynamicListElements);

        DynamicListView.setAdapter(adapter);

        mOpenCvCameraView=new JavaCameraView(this,CameraBridgeViewBase.CAMERA_ID_BACK);

        //mOpenCvCameraView.setLayoutParams(new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.MATCH_PARENT,
        //        10));

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(DynamicListView);
        linearLayout.addView(mOpenCvCameraView);

        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

       // this.setContentView(mOpenCvCameraView);
        DynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(TrainActivity.this, DynamicListElements[position], Toast.LENGTH_SHORT).show();
            }


        });

       // JavaCameraView jvcmv=new JavaCameraView(this)


        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
       // mOpenCvCameraView.enableView();

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
     //   Log.i(TAG,"Started");
    }

    @Override
    public void onCameraViewStopped() {
      //  Log.i(TAG,"Stopped");
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
      Log.i(TAG,"Frame");

        return null;
    }

    @Override
    public void onResume() {

        super.onResume();
        mOpenCvCameraView.enableView();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
}
