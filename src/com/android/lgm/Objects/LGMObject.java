package com.android.lgm.Objects;

import java.util.ArrayList;

public class LGMObject {

	private String type;
	private String label;
	private String replaceLabel;
	private String name;
	private String value;
	private String validate;
	private String alt;
	private String checked;
	private String onChosen;
	private String isEmpty;

	private ArrayList<LGMObject> subObjects;
	private ArrayList<LGMOption> options;	

	public static String YES = "yes";
	public static String NO = "no";

	public LGMObject() {
	}

	public LGMObject(String type, String label, String replace, String name,
			String value, String validate, String alt, String checked,
			String onChosen, String isEmpty) {
		this.type = type;
		this.label = label;
		this.replaceLabel = replace;
		this.name = name;
		this.value = value;
		this.validate = validate;
		this.alt = alt;
		this.checked = checked;
		this.onChosen = onChosen;
		this.isEmpty = isEmpty;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the validate
	 */
	public String getValidate() {
		return validate;
	}

	/**
	 * @param validate
	 *            the validate to set
	 */
	public void setValidate(String validate) {
		this.validate = validate;
	}

	/**
	 * @return the alt
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * @param alt
	 *            the alt to set
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * @return the checked
	 */
	public String getChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            the checked to set
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}

	/**
	 * @return the onChosen
	 */
	public String getOnChosen() {
		return onChosen;
	}

	/**
	 * @param onChosen
	 *            the onChosen to set
	 */
	public void setOnChosen(String onChosen) {
		this.onChosen = onChosen;
	}

	/**
	 * @return the isEmpty
	 */
	public String getIsEmpty() {
		return isEmpty;
	}

	/**
	 * @param isEmpty
	 *            the isEmpty to set
	 */
	public void setIsEmpty(String isEmpty) {
		this.isEmpty = isEmpty;
	}

	/**
	 * @return the subObjects
	 */
	public ArrayList<LGMObject> getSubObjects() {
		return subObjects;
	}

	/**
	 * @param subObjects
	 *            the subObjects to set
	 */
	public void setSubObjects(ArrayList<LGMObject> subObjects) {
		this.subObjects = subObjects;
	}

	/**
	 * @return the options
	 */
	public ArrayList<LGMOption> getOptions() {
		return options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(ArrayList<LGMOption> options) {
		this.options = options;
	}

	/**
	 * @return the replaceLabel
	 */
	public String getReplaceLabel() {
		return replaceLabel;
	}

	/**
	 * @param replaceLabel
	 *            the replaceLabel to set
	 */
	public void setReplaceLabel(String replaceLabel) {
		this.replaceLabel = replaceLabel;
	}

}
