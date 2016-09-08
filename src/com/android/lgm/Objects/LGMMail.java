package com.android.lgm.Objects;

import java.util.ArrayList;

import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

public class LGMMail implements TransportListener{
	private int ID;

	private String display; // DISPLAY
	private String to; // TO
	private String cc; // CC
	private String subject; // SUBJECT
	private String body; // BODY
	private ArrayList<LGMPairValue> attaches; // ATTACH
	private TransportListener mTransportListener;

	public LGMMail() {
	}

	public LGMMail(int iD, String display, String to, String cc,
			String subject, String body) {
		ID = iD;
		this.display = display;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.body = body;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @return the diplay
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @param diplay
	 *            the diplay to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc
	 *            the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the attaches
	 */
	public ArrayList<LGMPairValue> getAttaches() {
		return attaches;
	}

	/**
	 * @param attaches
	 *            the attaches to set
	 */
	public void setAttaches(ArrayList<LGMPairValue> attaches) {
		this.attaches = attaches;
	}
	
	public TransportListener getTransportListener() {
		return mTransportListener;
	}

	public void setTransportListener(TransportListener mTransportListener) {
		this.mTransportListener = mTransportListener;
	}

	@Override
	public void messageDelivered(TransportEvent arg0) {
		if(mTransportListener!=null) mTransportListener.messageDelivered(arg0);
	}

	@Override
	public void messageNotDelivered(TransportEvent arg0) {
		if(mTransportListener!=null) mTransportListener.messageNotDelivered(arg0);
	}

	@Override
	public void messagePartiallyDelivered(TransportEvent arg0) {
		if(mTransportListener!=null) mTransportListener.messagePartiallyDelivered(arg0);
	}

}
