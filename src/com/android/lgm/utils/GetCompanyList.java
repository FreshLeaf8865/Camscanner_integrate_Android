package com.android.lgm.utils;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.android.lgm.Objects.LGMCompany;

public class GetCompanyList {
	public ArrayList<LGMCompany> parse(InputStream input) {
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 try {
			 XMLHandler handler = new XMLHandler();
			 SAXParser parser = factory.newSAXParser(); 
			 parser.parse(input, handler);
			 return handler.getCompanies();
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
		
		private StringBuilder builder;
		
		private LGMCompany comp;
		private ArrayList<LGMCompany> comps;
		
		
		/* Public Methods */
		public ArrayList<LGMCompany> getCompanies() {return comps;}
		
		
		/* Private Methods */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			builder.delete(0, builder.length());
			builder.append(ch, start, length);
		}
		
		@Override
		public void startDocument() throws SAXException {
			builder = new StringBuilder();
			comps = new ArrayList<LGMCompany>();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (localName.equalsIgnoreCase(TAG_ITEM)){
				comp = new LGMCompany();
			}	
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {	
			if (comp != null && localName.equalsIgnoreCase(TAG_ITEM)) {
				comps.add(comp);
			}else if (comp != null && localName.equalsIgnoreCase(TAG_COMP_ID)) {
				comp.setID(builder.toString());
			}else if (comp != null && localName.equalsIgnoreCase(TAG_COMP_NAME)) {
				comp.setCompName(builder.toString());
			}else if (comp != null && localName.equalsIgnoreCase(TAG_COMP_LOGO)) {
				comp.setLogo(builder.toString());
			}
		}
	}
}
