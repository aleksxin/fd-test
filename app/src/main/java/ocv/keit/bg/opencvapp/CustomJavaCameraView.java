package ocv.keit.bg.opencvapp;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.widget.ListView;

import org.opencv.android.JavaCameraView;

import java.util.ArrayList;
import java.util.List;

public class CustomJavaCameraView extends JavaCameraView {


    public CustomJavaCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public CustomJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<Camera.Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Camera.Size resolution) {
        disconnectCamera();
        connectCamera((int) resolution.width, (int) resolution.height);
    }

    public Camera.Size getResolution() {
        Camera.Parameters params = mCamera.getParameters();
        Camera.Size s = params.getPreviewSize();
        return s;
    }

    public ArrayList<String> getResolutionListString() {
        List<String> lst=new ArrayList<String>();
        List<Camera.Size> bb=mCamera.getParameters().getSupportedPictureSizes();
        for (int i=0; i<bb.size(); i++) {
            lst.add("["+Integer.toString(bb.get(i).height)+","+Integer.toString(bb.get(i).width)+"]");
        }
        return (ArrayList<String>) lst;
    }

    public void takePicture(Camera.PictureCallback jpegCallback){
        mCamera.takePicture(null,null,jpegCallback);
    }
}
