package ocv.keit.bg.opencvapp;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
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

    public void setMaxResolution() {
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size myBestPrevSize = getBestPrevSize(parameters);
        disconnectCamera();
        connectCamera(myBestPrevSize.width, myBestPrevSize.height);
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
        parameters.setJpegQuality(100);

        //Camera.Size myBestSize =  getBestPreviewSize(surfaceView.getWidth() , surfaceView.getHeight(),parameters);

        //parameters.setPreviewSize(myBestSize.width, myBestSize.height);


        Camera.Size myBestSize = getBestImageSize(parameters);

        parameters.setPictureSize(myBestSize.width, myBestSize.height);

        Camera.Size myBestPrevSize = getBestPrevSize(parameters);

        parameters.setPreviewSize(myBestPrevSize.width, myBestPrevSize.height);


        mCamera.setParameters(parameters);
    }

    public static Camera.Size getBestPrevSize(Camera.Parameters parameters) {
        // Camera.Size result=null;
        //float dr = Float.MAX_VALUE;
        // float ratio = (float)width/(float)height;
        int max_val = 0;
        Camera.Size ResSize = null;
        Log.d(CustomJavaCameraView.class.getName(),parameters.getSupportedPreviewFormats().toString());
        List<Camera.Size> jbg = parameters.getSupportedPreviewSizes();
        StringBuilder sb=new StringBuilder();
        for (Camera.Size size:jbg) {
            sb.append("(").append(size.width).append(",").append(size.height).append(")");
            
        }
                Log.d(CustomJavaCameraView.class.getName(),"[ "+sb.toString()+" ]");

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {

            int this_size = size.height * size.width;
            if (this_size > max_val) {
                ResSize = size;
                max_val = this_size;
            }

        }

        return ResSize;
    }

    public void startFocus(Rect focusArea, Camera.AutoFocusCallback autoFocusCallback){

        final String focusMode=Camera.Parameters.FOCUS_MODE_AUTO;
        Rect targetRect=null;
        if (focusArea!=null) {
            int left = (2000 * focusArea.left - 1000 * getWidth()) / getWidth();
            int top = (2000 * focusArea.top - 1000 * getHeight()) / getHeight();
            int right = (2000 * focusArea.right - 1000 * getWidth()) / getWidth();
            int bottom = (2000 * focusArea.bottom - 1000 * getHeight()) / getHeight();
            targetRect = new Rect(left, top, right, bottom);
        }
        Camera.Parameters parameters = mCamera.getParameters();

        if ((parameters.getFocusMode() != focusMode)&&(parameters.getSupportedFocusModes().contains(focusMode))) {
            parameters.setFocusMode(focusMode);
        }
        if (parameters.getMaxNumFocusAreas() > 0){
            if (targetRect != null){
                List<Camera.Area> mylist = new ArrayList<Camera.Area>();
                mylist.add(new Camera.Area(targetRect, 1000));
                parameters.setFocusAreas(mylist);
            }
            else
            {
                parameters.setFocusAreas(null);
            }
        }

        stopFocus();
        mCamera.setParameters(parameters);
        mCamera.autoFocus(autoFocusCallback);

    }

    public void stopFocus(){
        mCamera.cancelAutoFocus();
    }


}
