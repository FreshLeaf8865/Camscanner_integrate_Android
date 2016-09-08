package com.android.lgm.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.android.lgm.Objects.LGMCompany;
import com.android.lgm.Objects.LGMObject;
/**
 * 
 * @author nvn-itpro
 *
 */
public class XMLParser {
	private XMLHandler handler = new XMLHandler();
	/* Ctors */
    public void parse(InputStream input) {
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 try {
			 SAXParser parser = factory.newSAXParser(); 
			 parser.parse(input, handler);
		 }
		 catch (Exception Ex) {
			 throw new RuntimeException(Ex);
		 }
    }
    
    public ArrayList<LGMObject> getObjs(){
    	return handler.getObjs();
    }
    
    public LGMCompany getInfor(){
    	return handler.getInfor();
    }
    
    public HashMap<String, ArrayList<LGMObject>> getConditions() {
    	return handler.getConditions();
    }
}
