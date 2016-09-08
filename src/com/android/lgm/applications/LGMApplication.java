package com.android.lgm.applications;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.net.Uri;
import android.widget.EditText;

import com.android.lgm.Objects.LGMObject;
import com.android.lgm.views.LGMBarcode;
import com.android.lgm.views.LGMCamera;
import com.android.lgm.views.LGMSignature;

public class LGMApplication extends Application {

	private HashMap<String, ArrayList<LGMObject>> expands;
	private LGMCamera camera;
	private LGMSignature signature;
	private LGMBarcode barcode;
	private Uri fileUri;
	private EditText edt;

	/**
	 * @return the edt
	 */
	public EditText getEdt() {
		return edt;
	}

	/**
	 * @param edt the edt to set
	 */
	public void setEdt(EditText edt) {
		this.edt = edt;
	}

	/**
	 * @return the barcode
	 */
	public LGMBarcode getBarcode() {
		return barcode;
	}

	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(LGMBarcode barcode) {
		this.barcode = barcode;
	}

	/**
	 * @return the fileUri
	 */
	public Uri getFileUri() {
		return fileUri;
	}

	/**
	 * @param fileUri the fileUri to set
	 */
	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}

	/**
	 * @return the camera
	 */
	public LGMCamera getCamera() {
		return camera;
	}

	/**
	 * @param camera the camera to set
	 */
	public void setCamera(LGMCamera camera) {
		this.camera = camera;
	}

	/**
	 * @return the signature
	 */
	public LGMSignature getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(LGMSignature signature) {
		this.signature = signature;
	}

	/**
	 * @return the expands
	 */
	public HashMap<String, ArrayList<LGMObject>> getExpands() {
		return expands;
	}

	/**
	 * @param expands the expands to set
	 */
	public void setExpands(HashMap<String, ArrayList<LGMObject>> expands) {
		this.expands = expands;
	}

	
}
