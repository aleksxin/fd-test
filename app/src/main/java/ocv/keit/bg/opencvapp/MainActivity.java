package ocv.keit.bg.opencvapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.FisherFaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2,DialogInterface.OnDismissListener {

    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    private static final Scalar FACE_REC     = new Scalar(255, 0, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private MenuItem               mItemFace50;
    private MenuItem               mItemFace40;
    private MenuItem               mItemFace30;
    private MenuItem               mItemFace20;
    private MenuItem               mItemType;

    private Mat                    mRgba;
    private Mat                    mGray;
    private File mCascadeFile;
    public CascadeClassifier mJavaDetector;
    public DetectionBasedTracker  mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    public float                  mRelativeFaceSize   = 0.2f;
    public int                    mAbsoluteFaceSize   = 0;
    private CameraBridgeViewBase mOpenCvCameraView;

    // Used in Camera selection from menu (when implemented)
    private boolean              mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private Mat lastImage;
    private Mat mLastGrey;
    private Rect[] lastFaces;

    private ArrayList<String> mPeople;

    boolean mTrained=false;

    Bitmap mBitmap=null;

    FaceRecognizer mFaceRecognizer;
    Size mRecSize=new Size(0,0);



    // These variables are used (at the moment) to fix camera orientation from 270degree to 0degree
   // Mat mRgba;
  //  Mat mRgbaF;
   // Mat mRgbaT;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
        System.loadLibrary("detection_based_tracker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
      //  mOpenCvCameraView.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.face_detect_surface_view);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.fd_activity_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        ((Button) findViewById(R.id.button_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mOpenCvCameraView.tak
                if ((getLastFaces()!=null)&&(getLastFaces().length>0)) {
                    lastImage=mRgba.clone();
                    mLastGrey=mGray.clone();
                    mOpenCvCameraView.disableView();
                    FragmentManager fm = getFragmentManager();
                    TrainImagesDialog editNameDialogFragment = TrainImagesDialog.newInstance("Some Title");
                    editNameDialogFragment.show(fm, "fragment_edit_name");
                }
                /*Intent intent=new Intent(MainActivity.this,TrainActivity.class);
                Bundle extras = intent.getExtras();

                MainActivity.this.startActivity(intent);
*/
            }
        });

        ((Button) findViewById(R.id.button_test)).setOnClickListener(new MyOnClickListener1());
        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        //if (!OpenCVLoader.initDebug()){
      //      tv.setText(tv.getText()+"\n OpenCVLoader not working");
      //  } else {
      //      tv.setText(tv.getText()+"\n OpenCVLoader WORKING");
      //      tv.setText(tv.getText()+"\n"+validate(0L, 0L));
      //  }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
   // public native String stringFromJNI();
   // public native String validate(long matAddrGr, long matAddrRdba);

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
                    System.loadLibrary("detection_based_tracker");
                 //   System.loadLibrary("face");
                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);



                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();
                    mFaceRecognizer= LBPHFaceRecognizer.create();//1,8,8,8,0.7);
                    //((CustomJavaCameraView)mOpenCvCameraView).initCameraParams();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity(){
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";
        setPeople(new ArrayList<String>());

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        MatOfRect faces = getMatOfRectFaces(mGray);

       // Rect[] facesArray = faces.toArray();
        lastFaces=faces.toArray();
     //   lastImage=mRgba.clone();
        for (int i = 0; i < lastFaces.length; i++) {
            if ((mTrained)&&(mFaceRecognizer!=null)){
                int[] fa=new int[]{-1};
                double[] conf=new double[]{0.0};
                Mat mt=new Mat();
                Imgproc.resize(new Mat(mGray,lastFaces[i]),mt,mRecSize);
               // Imgproc.cvtColor(mt,mt,Imgproc.COLOR_RGBA2GRAY);
               // int pred = mFaceRecognizer.predict_label(mt);
               // Log.d(TAG,Integer.toString(pred));
                mFaceRecognizer.predict(mt,fa,conf);

                if ((fa.length>0)/*&&(conf[0]>20)*/){
                    Imgproc.rectangle(mRgba, lastFaces[i].tl(), lastFaces[i].br(), FACE_REC, 3);
                    Imgproc.putText(mRgba, mPeople.get(fa[0])+ "~"+String.format ("%.1f", conf[0]), new Point(lastFaces[i].x,lastFaces[i].y+lastFaces[i].height+25), Core.FONT_HERSHEY_SIMPLEX ,      // front face
                            1, FACE_REC,2);
                }
                else
                        Imgproc.rectangle(mRgba, lastFaces[i].tl(), lastFaces[i].br(), FACE_RECT_COLOR  , 3);
            }
            else

            Imgproc.rectangle(mRgba, lastFaces[i].tl(), lastFaces[i].br(), FACE_RECT_COLOR, 3);
        }

        return mRgba;
    }

    @NonNull
    private MatOfRect getMatOfRectFaces(Mat grey) {
        if ((grey!=null)&&(!grey.empty())) {
            if (mAbsoluteFaceSize == 0) {
                int height = grey.rows();
                if (Math.round(height * mRelativeFaceSize) > 0) {
                    mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                }
                mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
            }

            MatOfRect faces = new MatOfRect();

            if (mDetectorType == JAVA_DETECTOR) {
                if (mJavaDetector != null)
                    mJavaDetector.detectMultiScale(grey, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                            new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
            } else if (mDetectorType == NATIVE_DETECTOR) {
                if (mNativeDetector != null)
                    mNativeDetector.detect(grey, faces);
            } else {
                Log.e(TAG, "Detection method is not selected!");
            }
            return faces;
        }
        else
            return null;
    }


    public void MainActivity_show_camera() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemFace50 = menu.add("Face size 50%");
        mItemFace40 = menu.add("Face size 40%");
        mItemFace30 = menu.add("Face size 30%");
        mItemFace20 = menu.add("Face size 20%");
        mItemType   = menu.add(mDetectorName[mDetectorType]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemFace50)
            setMinFaceSize(0.5f);
        else if (item == mItemFace40)
            setMinFaceSize(0.4f);
        else if (item == mItemFace30)
            setMinFaceSize(0.3f);
        else if (item == mItemFace20)
            setMinFaceSize(0.2f);
        else if (item == mItemType) {
            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[tmpDetectorType]);
            setDetectorType(tmpDetectorType);
        }
        return true;
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }


    public Mat getLastImage() {
        return lastImage;
    }

    public Rect[] getLastFaces() {
        return lastFaces;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (lastImage!=null) lastImage.release();
        mOpenCvCameraView.enableView();
    }

    public ArrayList<String> getmPeople() {
        return mPeople;
    }

    public void setPeople(ArrayList<String> mPeople) {
        this.mPeople = mPeople;
    }

    public ArrayList<String> getPeople() {
        return mPeople;
    }

    public void trainRecognizer(String person,Rect rectFace){
        if (!person.isEmpty()){
            Log.d(TAG,"Training "+person);
            int inx = mPeople.indexOf(person);
            if (inx<0) {
                    inx=mPeople.size();
                    mPeople.add(person);
                }
            ArrayList<Mat> srcImages=new ArrayList<Mat>();
            Mat mt = new Mat(mLastGrey,rectFace);
          //  Imgproc.cvtColor(mt,mt,Imgproc.COLOR_RGBA2GRAY);
            if ((mRecSize.height==0)||(mRecSize.width==0))
                mRecSize=mt.size();
            else
                Imgproc.resize(mt,mt,mRecSize);
            srcImages.add(mt);
            MatOfInt matOfInt = new MatOfInt();
            ArrayList<Integer> lInx=new ArrayList<Integer>();
            lInx.add(inx);
            matOfInt.fromList(lInx);
            try {
                if (mTrained)  mFaceRecognizer.update(srcImages,matOfInt);
                        else

                mFaceRecognizer.train(srcImages, matOfInt);
                Toast.makeText(this,"Successfully trained "+person,Toast.LENGTH_LONG).show();
                Log.d(TAG,"ppl: "+mPeople.toString()+" |size:"+mRecSize.toString()+" |added:"+Integer.toString(inx)+","+matOfInt.toString()+" |imageindex:"+rectFace.toString());
                mTrained=true;
               // lastFaces = ArrayUtils.removeElement(lastFaces, element);
            }
            catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
           // lastFaces.remove(imageIndex);



        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Pick a color");
            List<String> lst = ((CustomJavaCameraView) mOpenCvCameraView).getResolutionListString();
            builder.setItems(lst.toArray(new CharSequence[lst.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                }
            });
            builder.show();
        }
    }

    private class MyOnClickListener1 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ((CustomJavaCameraView)mOpenCvCameraView).initCameraParams();
            ((CustomJavaCameraView)mOpenCvCameraView).takePicture(jpegCallback);

        }
    }
    /*
    Bitmap bmp = null;
        try {
        bmp = Bitmap.createBitmap(subimg.cols(), subimg.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(subimg, bmp);
    } catch (CvException e) {
        Log.d(TAG, e.getMessage());
    }

        subimg.release();


    FileOutputStream out = null;

    String filename = "frame.png";


    File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
    boolean success = true;
        if (!sd.exists()) {
        success = sd.mkdir();
    }
        if (success) {
        File dest = new File(sd, filename);

        try {
            out = new FileOutputStream(dest);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                    Log.d(TAG, "OK!!");
                }
            } catch (IOException e) {
                Log.d(TAG, e.getMessage() + "Error");
                e.printStackTrace();
            }
        }
    }*/

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Camera.Size picSize=camera.getParameters().getPictureSize();
           // camera.getParameters().getPictureFormat()
            System.out.println(camera.getParameters().getPictureFormat()+ "[ "+camera.getParameters().getPictureSize().width+","+camera.getParameters().getPictureSize().height+" ]");//data.length);
            Mat photoMat = new Mat(picSize.width, picSize.height, CvType.CV_8UC3);

            photoMat.put(0, 0, data);
            Mat greyPhoto=new Mat(picSize.width,picSize.height,CvType.CV_8UC1);
            Imgproc.cvtColor(photoMat,greyPhoto,Imgproc.COLOR_RGB2GRAY);
            Imgproc.resize(greyPhoto,greyPhoto,new Size(2096,1572));
            mBitmap = Bitmap.createBitmap(greyPhoto.width(), greyPhoto.height(), ARGB_8888);
            Utils.matToBitmap(greyPhoto, mBitmap);
          /*  MatOfRect faces=getMatOfRectFaces(greyPhoto);
            if (faces.toArray().length>0) {
                mBitmap = Bitmap.createBitmap(faces.toArray()[0].width, faces.toArray()[0].height, ARGB_8888);
                Utils.matToBitmap(new Mat(photoMat,faces.toArray()[0]), mBitmap);
                mOpenCvCameraView.disableView();
*/
                FragmentManager fm = getFragmentManager();
                TestFragmentDialog testFragmentDialog = TestFragmentDialog.newInstance();
                testFragmentDialog.show(fm, "tag");
          //  }
        }
    };
}
