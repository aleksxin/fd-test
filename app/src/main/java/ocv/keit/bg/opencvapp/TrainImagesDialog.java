package ocv.keit.bg.opencvapp;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainImagesDialog extends DialogFragment {
    private EditText mEditText;

    public TrainImagesDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    public static TrainImagesDialog newInstance(String title) {
        TrainImagesDialog frag = new TrainImagesDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());

        ListView DynamicListView = new ListView(getActivity());

        DynamicListView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.WRAP_CONTENT));
        DynamicListView.setId(R.id.imageslistview);
       // linearLayout.setIm

        //   ArrayAdapter<String> adapter = new ArrayAdapter<String>()
        //           (TrainActivity.this, android.R.layout.simple_list_item_1, DynamicListElements);

       // DynamicListView.setAdapter(adapter);



        //mOpenCvCameraView.setLayoutParams(new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.MATCH_PARENT,
        //        10));

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(DynamicListView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                return linearLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();

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

        FacesListAdapter adapter = new FacesListAdapter((MainActivity)getActivity(), new ArrayList<String>(Arrays.asList(DynamicListElements)));
        ((ListView)view.findViewById(R.id.imageslistview)).setAdapter(adapter);
       // getDialog().getWindow().setSoftInputMode(
         //       WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
