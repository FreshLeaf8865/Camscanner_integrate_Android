package com.android.lgm.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class FlashingLine extends LinearLayout {
	private int normalColor;
	private int focusColor;
	private int lineHeight;
	private Handler mHandler;
	private Runnable mRnFlash;
	private View mView;
	private boolean isFlash = false;
	private long timer = 500;// 1s

	public FlashingLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FlashingLine(Context context) {
		super(context);
		init();
	}

	public void init() {
		this.lineHeight = (int) (2 * getContext().getResources()
				.getDisplayMetrics().density);
		this.normalColor = Color.parseColor("#009900");
		this.focusColor = Color.parseColor("#9ccf00");
		mView = new View(getContext());
		mView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, lineHeight));
		this.addView(mView);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.mHandler = new Handler();
		this.mRnFlash = new Runnable() {
			@Override
			public void run() {
				if (isFlash) {
					mView.setBackgroundColor(focusColor);
				} else {
					mView.setBackgroundColor(normalColor);
				}
				isFlash = !isFlash;
				mHandler.postDelayed(this, timer);
			}
		};
	}

	public void start() {
		mHandler.removeCallbacks(mRnFlash);
		mHandler.post(mRnFlash);
	}

	public void stop() {
		mHandler.removeCallbacks(mRnFlash);
	}

}
