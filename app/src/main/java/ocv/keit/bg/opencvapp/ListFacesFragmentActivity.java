package ocv.keit.bg.opencvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListFacesFragmentActivity extends Fragment {
    LinearLayout linearLayout;
    CameraViewsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

         adapter = new CameraViewsAdapter(getActivity(), new ArrayList<String>(Arrays.asList(DynamicListElements)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        linearLayout = new LinearLayout(getActivity());

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        View view=inflater.inflate(R.layout.list_fragmetnt_layout,container,false);
        ListView DynamicListView = view.findViewById(R.id.listview);
              //  new ListView(getActivity());



        DynamicListView.setAdapter(adapter);

        //   ArrayAdapter<String> adapter = new ArrayAdapter<String>()
        //           (TrainActivity.this, android.R.layout.simple_list_item_1, DynamicListElements);

       // DynamicListView.setAdapter(adapter);



        //mOpenCvCameraView.setLayoutParams(new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.MATCH_PARENT,
        //        10));

       // linearLayout.setOrientation(LinearLayout.VERTICAL);
        //linearLayout.addView(DynamicListView);
        return  inflater.inflate(R.layout.activity_main,container,false);
        //return view;//super.onCreateView(inflater, container, savedInstanceState);
    }
}
