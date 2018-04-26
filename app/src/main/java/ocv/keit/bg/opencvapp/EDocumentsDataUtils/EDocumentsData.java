package ocv.keit.bg.opencvapp.EDocumentsDataUtils;
import android.graphics.Bitmap;
import android.os.Parcelable;

import org.jmrtd.lds.icao.MRZInfo;


import java.io.Serializable;
import java.util.List;



public class EDocumentsData implements Serializable {

    private MRZInfo capturedMRZ;
    private MRZInfo chipMRZ;
    private Bitmap capturedFace;
    private Bitmap chipFace;

    //TODO: fingerprints
    //TODO: Additional info if needed
    //private List<String> addititionalInfo;


    public EDocumentsData() {
    }

    public EDocumentsData(MRZInfo capturedMRZ,MRZInfo chipMRZ,Bitmap capturedFace,Bitmap chipFace){
        this.capturedMRZ = capturedMRZ;
        this.chipMRZ = chipMRZ;
        this.capturedFace = capturedFace.copy(capturedFace.getConfig(),true);
        this.chipFace = chipFace.copy(chipFace.getConfig(),true);

    }

    /*
     *gets the MRZ captured by the camera
     * @return capturedMRZ
     */
    public MRZInfo getCapturedMRZ() {
        return capturedMRZ;
    }

    /*
     * sets the MRZ captured by the camera
     * @param capturedMRZ new captured MRZ
     */
    public void setCapturedMRZ(MRZInfo capturedMRZ) {
        this.capturedMRZ = capturedMRZ;
    }
    /*
     * gets the MRZ retrieved from the chip
     * @return chipMRZ
     */
    public MRZInfo getChipMRZ() {
        return chipMRZ;
    }

    /*
     * sets the MRZ retrieved from the chip
     * @param chipMRZ new chip MRZ
     */
    public void setChipMRZ(MRZInfo chipMRZ) {
        this.chipMRZ = chipMRZ;
    }

    /*
     * gets the face image captured by the camera
     * @return capturedFace image from the camera
     */
    public Bitmap getCapturedFace() {
        return capturedFace;
    }

    /*
     * sets the image captured by the camera
     * @param capturedFace image captured by the camera
     */
    public void setCapturedFace(Bitmap capturedFace) {
        this.capturedFace = capturedFace.copy(capturedFace.getConfig(),true);
    }

    /*
     * gets the image retrieved from the chip
     * @return chipFace
     */
    public Bitmap getChipFace() {
        return chipFace;
    }

    /*
     * sets the image retrieved from the chip
     * @param chipFace the image as Bitmap
     */
    public void setChipFace(Bitmap chipFace) {
        this.chipFace = chipFace.copy(chipFace.getConfig(),true);
    }
}
