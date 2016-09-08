package com.android.lgm.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.LGMObject;
import com.android.lgm.Objects.LGMOption;

/**
 * 
 * @author nvn-itpro
 *
 */
public class XMLHandler extends DefaultHandler {
	/* Fields */
	static final String TAG_HEAD = "Head";
	static final String TAG_CUSTOMERID = "CustomerID";
	static final String TAG_COMP_NAME = "CompanyName";
	static final String TAG_MAIL_SUBJECT = "MailSubject";
	static final String TAG_MAIL_ADDRESS = "MailAddress";
	static final String TAG_CONDITION_ELEMENT = "ConditionElement";
	static final String TAG_GROUP_ELEMENT = "GroupElement";

	static final String TAG_FORM = "Form";
	static final String TAG_ELEMENT = "Element";
	static final String TAG_LISVIEW_FIELD = "ListViewField";
	static final String TAG_RADIO = "Radio";
	static final String TAG_OPTION = "Option";

	static final String ATTR_TYPE = "type";
	static final String ATTR_LABEL = "label";
	static final String ATTR_NAME = "name";
	static final String ATTR_VALUE = "value";
	static final String ATTR_VALIDATE = "validate";
	static final String ATTR_ALT = "alt";
	static final String ATTR_CHECKED = "checked";
	static final String ATTR_ONCHECKED = "onChecked";
	static final String ATTR_ONCHOSEN = "onChosen";
	static final String ATTR_ISEMPTY = "isEmpty";
	static final String ATTR_ANDROID = "android";

	private StringBuilder builder;

	private HashMap<String, ArrayList<LGMObject>> expands;
	private ArrayList<LGMObject> objs, subobjs, groups;
	private ArrayList<LGMOption> options;
	private LGMCompany infor;
	private LGMObject object, subobject;
	private LGMOption option;
	private String GROUP_ELEMENT_KEY = "";

	/* Public Methods */
	public ArrayList<LGMObject> getObjs() {
		return objs;
	}

	public LGMCompany getInfor() {
		return infor;
	}

	public HashMap<String, ArrayList<LGMObject>> getConditions() {
		return expands;
	}

	/* Private Methods */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		builder.delete(0, builder.length());
		builder.append(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
		Log.i("startDocument", "startDocument");
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equalsIgnoreCase(TAG_HEAD)) {
			infor = new LGMCompany();
		} else if (localName.equalsIgnoreCase(TAG_FORM)) {
			objs = new ArrayList<LGMObject>();
		} else if (localName.equalsIgnoreCase(TAG_ELEMENT)) {
			object = new LGMObject(attributes.getValue(ATTR_TYPE),
					attributes.getValue(ATTR_LABEL),
					attributes.getValue(ATTR_ANDROID),
					attributes.getValue(ATTR_NAME),
					attributes.getValue(ATTR_VALUE),
					attributes.getValue(ATTR_VALIDATE),
					attributes.getValue(ATTR_ALT),
					attributes.getValue(ATTR_CHECKED),
					attributes.getValue(ATTR_ONCHOSEN),
					attributes.getValue(ATTR_ISEMPTY));			
			subobjs = new ArrayList<LGMObject>();
			options = new ArrayList<LGMOption>();
		} else if (localName.equalsIgnoreCase(TAG_LISVIEW_FIELD)
				|| localName.equalsIgnoreCase(TAG_RADIO)) { // Subobject
			subobject = new LGMObject(attributes.getValue(ATTR_TYPE),
					attributes.getValue(ATTR_LABEL),
					attributes.getValue(ATTR_ANDROID),
					attributes.getValue(ATTR_NAME),
					attributes.getValue(ATTR_VALUE),
					attributes.getValue(ATTR_VALIDATE),
					attributes.getValue(ATTR_ALT),
					attributes.getValue(ATTR_CHECKED),
					attributes.getValue(ATTR_ONCHOSEN),
					attributes.getValue(ATTR_ISEMPTY));
			options = new ArrayList<LGMOption>();
		} else if (localName.equalsIgnoreCase(TAG_OPTION)) {
			option = new LGMOption();
			option.setValue(attributes.getValue(ATTR_VALUE));
			option.setOnChosen(attributes.getValue(ATTR_ONCHOSEN));
		} else if (localName.equalsIgnoreCase(TAG_CONDITION_ELEMENT)) {
			expands = new HashMap<String, ArrayList<LGMObject>>();
		} else if (localName.equalsIgnoreCase(TAG_GROUP_ELEMENT)) {
			groups = new ArrayList<LGMObject>();
			GROUP_ELEMENT_KEY = attributes.getValue(ATTR_NAME);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (infor != null && localName.equalsIgnoreCase(TAG_CUSTOMERID)) {
			infor.setCustomID(builder.toString());
		} else if (infor != null && localName.equalsIgnoreCase(TAG_COMP_NAME)) {
			infor.setCompName(builder.toString());
		} else if (infor != null
				&& localName.equalsIgnoreCase(TAG_MAIL_SUBJECT)) {
			infor.setMailSubject(builder.toString());
		} else if (infor != null
				&& localName.equalsIgnoreCase(TAG_MAIL_ADDRESS)) {
			infor.setMailAddress(builder.toString());
		} else if (object != null && localName.equalsIgnoreCase(TAG_ELEMENT)) {

			if (null != subobjs && subobjs.size() > 0) {
				object.setSubObjects(subobjs);
			}
			if (null != options && options.size() > 0) {
				object.setOptions(options);
			}

			if (null != groups) {
				groups.add(object);
			} else {
				objs.add(object);
			}
		} else if (subobject != null
				&& (localName.equalsIgnoreCase(TAG_LISVIEW_FIELD) || localName
						.equalsIgnoreCase(TAG_RADIO))) {
			if (null != options && options.size() > 0) {
				subobject.setOptions(options);
			}
			subobjs.add(subobject);
		} else if (options != null && option != null
				&& localName.equalsIgnoreCase(TAG_OPTION)) {
			option.setText(builder.toString());
			options.add(option);
		} else if (localName.equalsIgnoreCase(TAG_GROUP_ELEMENT)) {
			expands.put(GROUP_ELEMENT_KEY, groups);
		}

	}
}
