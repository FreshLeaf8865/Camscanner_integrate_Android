package com.android.lgm.views;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.martin.lgmsolutions.R;
import com.android.lgm.ZBarScannerActivity;
import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.applications.LGMApplication;
import com.android.lgm.utils.Statics;
import com.android.lgm.utils.ValidateEditText;
import com.android.lgm.utils.ZBarConstants;

public class LGMBarcode extends LGMCommon {
	private TextView tv;
	private EditText edt;
	private Button btn;

	public LGMBarcode(Activity context, String label, String alt,
			String isEmpty, int layout) {
		super(context, label, alt, isEmpty, layout);

		init(layout);

		tv.setText(label);
		this.addView(childView);
	}

	@Override
	protected void init(int layout) {
		super.init(layout);
		tv = (TextView) childView.findViewById(R.id.tv_barcode);
		edt = (EditText) childView.findViewById(R.id.edt_barcode);
		btn = (Button) childView.findViewById(R.id.btn_barcode);
		btn.setOnClickListener(this);
	}

	@Override
	public LGMPairValue getValue() {
		if (isEmpty.toLowerCase().equals("no")
				&& !ValidateEditText.validateEditText(edt)) {
			return null;
		}
		return new LGMPairValue(label, edt.getText().toString().trim());
	}

	@Override
	public String getHtmlValue() {
		String val = edt.getText().toString().trim();
		if (val.length() == 0) {
			if (isEmpty.toLowerCase().equals("no")) {
				application.setEdt(edt);
				return Statics.KEY_RETURN + label + " is missing!";
			} else
				return null;
		}
		return "<b>" + label + " : </b>" + val;
	}

	public void setValue(String value) {
		edt.setText(value);
	}

	@Override
	public void onClick(View arg0) {
		application.setBarcode(this);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		// Intent intent = new Intent(context, CaptureActivity.class);
		// intent.putExtra(Intents.Scan.WIDTH, (width*70)/100);
		// intent.putExtra(Intents.Scan.HEIGHT, (height*70)/100);
		// context.startActivityForResult(intent, Statics.BARCODE_REQUEST);

		Intent intent = new Intent(context, ZBarScannerActivity.class);
		intent.putExtra(ZBarConstants.SCAN_MODES, new int[] { Symbol.CODABAR, Symbol.CODE128,
				Symbol.CODE39,Symbol.CODE93, Symbol.DATABAR_EXP, Symbol.DATABAR, Symbol.EAN13, Symbol.EAN8, Symbol.I25,
				Symbol.ISBN10, Symbol.ISBN13, Symbol.PARTIAL, Symbol.PDF417, Symbol.UPCA, Symbol.UPCE,
				Symbol.QRCODE });
		context.startActivityForResult(intent, Statics.BARCODE_REQUEST);
	}
}
