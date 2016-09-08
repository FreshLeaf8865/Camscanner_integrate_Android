package com.android.lgm.gmails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.graphics.Bitmap;

import com.android.lgm.Objects.LGMPairValue;
import com.android.lgm.utils.FileUtil;

public class GMailSender extends javax.mail.Authenticator {
	private String mailhost = "smtp.gmail.com";
	private String user;
	private String password;
	private Session session;

	static {
		Security.addProvider(new JSSEProvider());
	}

	public GMailSender(String user, String password) {
		this.user = user;
		this.password = password;

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	public synchronized String sendMail(String subject, String body,
			String from, String recipients, String recipientsCC,
			ArrayList<LGMPairValue> files, TransportListener lsn) {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setSender(new InternetAddress(user));
			message.setFrom(new InternetAddress(user, from
					+ " via LGM Solution"));
			message.setSubject(subject);
			if (recipients.indexOf(',') > 0)
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipients));
			else
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients));
			if (null != recipientsCC && recipientsCC.length() > 0)
				if (recipientsCC.indexOf(',') > 0)
					message.setRecipients(Message.RecipientType.CC,
							InternetAddress.parse(recipientsCC));
				else
					message.setRecipient(Message.RecipientType.CC,
							new InternetAddress(recipientsCC));

			Multipart mPart = new MimeMultipart();

			// setup message body
			BodyPart mBP1 = new MimeBodyPart();
			DataHandler handler = new DataHandler(new ByteArrayDataSource(
					body.getBytes(), "text/html"));
			mBP1.setDataHandler(handler);
			mPart.addBodyPart(mBP1);

			// attach file
			for (LGMPairValue f : files) {
				if (new File(f.getValue()).exists())
					addAttachFile(mPart, f);
			}

			message.setContent(mPart);
			Transport t = session.getTransport();
			t.connect();
			t.addTransportListener(lsn);
			t.sendMessage(message, message.getAllRecipients());

			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Fail: " + e.getMessage();
		}
	}

	public void addAttachFile(Multipart mPart, LGMPairValue f)
			throws MessagingException {
		BodyPart mBP2 = new MimeBodyPart();
		Bitmap bmp = FileUtil.decodeSampledBitmapFile(f.getValue(), 1208, 720);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		byte[] byteArray = stream.toByteArray();
		DataSource source = new ByteArrayDataSource(byteArray);
		mBP2.setDataHandler(new DataHandler(source));
		mBP2.setFileName(f.getName());
		mPart.addBodyPart(mBP2);
	}

	public class ByteArrayDataSource implements DataSource {
		private byte[] data;
		private String type;

		public ByteArrayDataSource(byte[] data, String type) {
			super();
			this.data = data;
			this.type = type;
		}

		public ByteArrayDataSource(byte[] data) {
			super();
			this.data = data;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getContentType() {
			if (type == null)
				return "application/octet-stream";
			else
				return type;
		}

		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data);
		}

		public String getName() {
			return "ByteArrayDataSource";
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Not Supported");
		}
	}

	// public static Bitmap loadResizedBitmap(String filename, int width,
	// int height, boolean exact) {
	// Bitmap bitmap = null;/*
	// * = BitmapFactory.decodeFile(filename);
	// * if((bitmap.getWidth() - bitmap.getHeight()) *
	// * (width - height) < 0){ int tmp = width; width =
	// * height; height = tmp; }
	// */
	//
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(filename, options);
	// width = options.outWidth;
	// height = options.outHeight;
	//
	// options.inJustDecodeBounds = false;
	// options.inSampleSize = 2;
	// options.inPreferredConfig = Bitmap.Config.RGB_565;
	// while (options.outWidth / options.inSampleSize > width
	// && options.outHeight / options.inSampleSize > height) {
	// options.inSampleSize++;
	// }
	// options.inSampleSize--;
	//
	// bitmap = BitmapFactory.decodeFile(filename, options);
	//
	// if (bitmap != null && bitmap.getWidth() > bitmap.getHeight()
	// && bitmap.getWidth() > width) {
	// bitmap = Bitmap.createScaledBitmap(bitmap, width,
	// (int) (bitmap.getHeight() / (bitmap.getWidth() / width)),
	// false);
	// if (bitmap.getHeight() > height) {
	// bitmap = Bitmap
	// .createScaledBitmap(
	// bitmap,
	// (int) (bitmap.getWidth() / (bitmap.getHeight() / height)),
	// height, false);
	// }
	// } else if (bitmap != null && bitmap.getHeight() > bitmap.getWidth()
	// && bitmap.getHeight() > height) {
	// bitmap = Bitmap.createScaledBitmap(bitmap,
	// (int) (bitmap.getWidth() / (bitmap.getHeight() / height)),
	// height, false);
	// }
	//
	// if (bitmap != null && exact) {
	// bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
	// }
	// if (bitmap != null) {
	// Log.i("BITMAP SIZE", bitmap.getWidth() + " - " + bitmap.getHeight());
	// }
	//
	// return bitmap;
	// }
}
