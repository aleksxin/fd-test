package ocv.keit.bg.opencvapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.zip.Inflater;

public class OpencvCameraFragmentActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.face_detect_surface_view,container,false);
        ((MainActivity)getActivity()).mOpenCvCameraView = (JavaCameraView) view.findViewById(R.id.fd_activity_surface_view);

        ((MainActivity)getActivity()).mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        ((MainActivity)getActivity()).mOpenCvCameraView.setCvCameraViewListener(((MainActivity)getActivity()));
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(((MainActivity)getActivity()).TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getActivity(), ((MainActivity)getActivity()).mLoaderCallback);
        } else {
            Log.d(((MainActivity)getActivity()).TAG, "OpenCV library found inside package. Using it!");
            ((MainActivity)getActivity()).mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (((MainActivity)getActivity()).mOpenCvCameraView != null)
            ((MainActivity)getActivity()).mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (((MainActivity)getActivity()).mOpenCvCameraView != null)
        ((MainActivity)getActivity()).mOpenCvCameraView.disableView();
    }
}
