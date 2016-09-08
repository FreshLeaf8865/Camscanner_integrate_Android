package com.android.lgm.Objects;

public class LGMOption {
	private String value;
	private String text;
	private String onChosen;
	
	public LGMOption() {}

	public LGMOption(String value, String text, String onChosen) {
		this.value = value;
		this.text = text;
		this.onChosen = onChosen;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the onChosen
	 */
	public String getOnChosen() {
		return onChosen;
	}

	/**
	 * @param onChosen the onChosen to set
	 */
	public void setOnChosen(String onChosen) {
		this.onChosen = onChosen;
	}
	
	
	
}
