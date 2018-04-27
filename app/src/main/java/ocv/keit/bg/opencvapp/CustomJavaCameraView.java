package ocv.keit.bg.opencvapp;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
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
        //disconnectCamera();
        if (mCamera!=null) {

            mCamera.takePicture(null, null, jpegCallback);
        }
        else {

        Log.d("CUSTOM CAMERA","Camera is null!");
        }
    }

    public static Camera.Size getBestImageSize(Camera.Parameters parameters) {
        // Camera.Size result=null;
        //float dr = Float.MAX_VALUE;
        // float ratio = (float)width/(float)height;
        int max_val = 0;
        Camera.Size ResSize = null;
        for (Camera.Size size : parameters.getSupportedPictureSizes()) {

            int this_size = size.height * size.width;
            if (this_size > max_val) {
                ResSize = size;
                max_val = this_size;
            }

        }

        return ResSize;
    }

    public void initCameraParams(){

        Camera.Parameters parameters = mCamera.getParameters();

        parameters.setPictureFormat(ImageFormat.JPEG);

        //Camera.Size myBestSize =  getBestPreviewSize(surfaceView.getWidth() , surfaceView.getHeight(),parameters);

        //parameters.setPreviewSize(myBestSize.width, myBestSize.height);


        Camera.Size myBestSize = getBestImageSize(parameters);

        parameters.setPictureSize(myBestSize.width, myBestSize.height);

        mCamera.setParameters(parameters);
    }


}
