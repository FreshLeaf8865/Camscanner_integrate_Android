package com.android.lgm.utils;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.Login;

public class GetLogin {
	public Login parse(InputStream input) {
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 try {
			 XMLHandler handler = new XMLHandler();
			 SAXParser parser = factory.newSAXParser(); 
			 parser.parse(input, handler);
			 return handler.getLogin();
		 }
		 catch (Exception Ex) {
			 throw new RuntimeException(Ex);
		 }
   }
	
	private class XMLHandler extends DefaultHandler {
		/* Fields */
		static final String TAG_ITEM = "Item";
		static final String TAG_COMP_ID = "CompanyID";
		static final String TAG_COMP_NAME = "CompanyName";
		static final String TAG_COMP_LOGO = "CompanyLogo";
		static final String TAG_DATA="Data";
		static final String TAG_COMPANY_LIST="CompanyList";
		static final String TAG_STATUS="Status";
		static final String TAG_USER_NAME="UserName";
		static final String TAG_PASSWORD="Password";
		static final String TAG_EMAIL="Email";
		static final String TAG_NAME="Name";
		static final String TAG_MESSAGE="Message";

		
		private StringBuilder builder;
		
		private LGMCompany comp;
		private ArrayList<LGMCompany> comps;
		private Login login;
		
		
		/* Public Methods */
		
		
		/* Private Methods */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			builder.delete(0, builder.length());
			builder.append(ch, start, length);
		}
		
		public Login getLogin() {
			return login;
		}

		public void setLogin(Login login) {
			this.login = login;
		}

		@Override
		public void startDocument() throws SAXException {
			builder = new StringBuilder();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (localName.equalsIgnoreCase(TAG_ITEM)){
				comp = new LGMCompany();
			}else if (localName.equalsIgnoreCase(TAG_COMPANY_LIST)){
				comps = new ArrayList<LGMCompany>();
			}else if (localName.equalsIgnoreCase(TAG_DATA)){
				login=new Login();
			}		
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(login!=null){
				if (comp != null && localName.equalsIgnoreCase(TAG_ITEM)) {
					comps.add(comp);
				}else if (comp != null && localName.equalsIgnoreCase(TAG_COMP_ID)) {
					comp.setID(builder.toString());
				}else if (comp != null && localName.equalsIgnoreCase(TAG_COMP_NAME)) {
					comp.setCompName(builder.toString());
				}else if (comp != null && localName.equalsIgnoreCase(TAG_COMP_LOGO)) {
					comp.setLogo(builder.toString());
				}else if (localName.equalsIgnoreCase(TAG_STATUS)) {
					try{
						login.setStatus(Boolean.parseBoolean(builder.toString()));
					}catch (Exception e) {}
				}else if (localName.equalsIgnoreCase(TAG_USER_NAME)) {
					login.setUserName(builder.toString());
				}else if (localName.equalsIgnoreCase(TAG_PASSWORD)) {
					login.setPassword(builder.toString());
				}else if (localName.equalsIgnoreCase(TAG_EMAIL)) {
					login.setEmail(builder.toString());
				}else if (localName.equalsIgnoreCase(TAG_NAME)) {
					login.setName(builder.toString());
				}else if (localName.equalsIgnoreCase(TAG_COMPANY_LIST)) {
					login.setLstCompany(comps);
				}else if (localName.equalsIgnoreCase(TAG_MESSAGE)) {
					login.setMessage(builder.toString());
				}

			}
		}
	}
}
