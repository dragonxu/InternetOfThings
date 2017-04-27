package com.tyloo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.tyloo.fw.Config;

public class MailParser {
	private Message mimeMessage = null;

	private String saveAttachPath = ""; // �������غ�Ĵ��Ŀ¼

	private StringBuffer bodytext = new StringBuffer(); // ����ʼ����ݵ�StringBuffer����

	private static final Properties props = new Properties();

	private static final Session session = Session.getDefaultInstance(props,
			null);

	/**
	 * ���캯��,��ʼ��һ��MimeMessage����
	 */
	public MailParser() {
	}

	public MailParser(Message mimeMessage) {
		this.mimeMessage = mimeMessage;
		System.out.println("create a PraseMimeMessage object........");
	}

	public MailParser(File file) {

		MimeMessage msg = null;
		try {
			msg = new MimeMessage(session, new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		this.mimeMessage = msg;
	}

	public void setMimeMessage(Message mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	/**
	 * ��÷����˵ĵ�ַ������
	 */
	public String getFrom() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null)
			from = "";
		String personal = address[0].getPersonal();
		if (personal == null)
			personal = "";
		String fromaddr = personal + "<" + from + ">";
		return fromaddr;
	}

	/**
	 * ��÷����˵ĵ�ַ
	 */
	public String getFromAddress() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		return address[0].getAddress();
	}

	/**
	 * ��÷����˵�����
	 */
	public String getFromName() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		return address[0].getPersonal();
	}

	/**
	 * ����ʼ����ռ��ˣ����ͣ������͵ĵ�ַ�����������������ݵĲ����Ĳ�ͬ "to"----�ռ��� "cc"---�����˵�ַ "bcc"---�����˵�ַ
	 */
	public String getMailAddress(String type) throws Exception {
		String mailaddr = "";
		String addtype = type.toUpperCase();
		InternetAddress[] address = null;
		if (addtype.equals("TO") || addtype.equals("CC")
				|| addtype.equals("BCC")) {
			if (addtype.equals("TO")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.TO);
			} else if (addtype.equals("CC")) {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) mimeMessage
						.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String email = address[i].getAddress();
					if (email == null)
						email = "";
					else {
						email = MimeUtility.decodeText(email);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
						personal = "";
					else {
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + email + ">";
					mailaddr += "," + compositeto;
				}
				mailaddr = mailaddr.substring(1);
			}
		} else {
			throw new Exception("Error emailaddr type!");
		}
		return mailaddr;
	}

	/**
	 * ����ʼ�����
	 */
	public String getSubject() throws MessagingException {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (Exception exce) {
		}
		return subject;
	}

	/**
	 * ����ʼ���������
	 */
	public Date getSentDate() throws Exception {
		Date sentdate = mimeMessage.getSentDate();
		return sentdate;
	}

	/**
	 * ����ʼ���������
	 */
	public String getBodyText() {
		try {
			getMailContent(mimeMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bodytext.toString();
	}

	/**
	 * �����ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ� ��Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ���
	 */
	private void getMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;

		System.out.println("CONTENTTYPE: " + contenttype);
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		} else {
		}
	}

	/**
	 * �жϴ��ʼ��Ƿ���Ҫ��ִ�������Ҫ��ִ����"true",���򷵻�"false"
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage
				.getHeader("Disposition-Notification-To");
		if (needreply != null) {
			replysign = true;
		}
		return replysign;
	}

	// /**
	// * ��ô��ʼ���Message-ID
	// */
	// public String getMessageId() throws MessagingException {
	// return mimeMessage.getMessageID();
	// }

	/**
	 * ���жϴ��ʼ��Ƿ��Ѷ������δ�����ط���false,��֮����true��
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = (mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		System.out.println("flags's length: " + flag.length);
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isnew = true;
				System.out.println("seen Message.......");
				break;
			}
		}
		return isnew;
	}

	/**
	 * �жϴ��ʼ��Ƿ��������
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		// String contentType = part.getContentType();
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE))))
					attachflag = true;
				else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach(mpart);
				} else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}

	/**
	 * �����渽����
	 */
	public void saveAttachMent(Part part) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					if (fileName.toLowerCase().indexOf("gb2312") != -1) {
						fileName = MimeUtility.decodeText(fileName);
					}
					saveFile(fileName, mpart.getInputStream());
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(mpart);
				} else {
					fileName = mpart.getFileName();
					if ((fileName != null)
							&& (fileName.toLowerCase().indexOf("GB2312") != -1)) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream());
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent());
		}
	}

	/**
	 * �����ø������·����
	 */
	public void setAttachPath(String attachpath) {
		this.saveAttachPath = attachpath;
	}

	/**
	 * ����ø������·����
	 */
	public String getAttachPath() {
		return saveAttachPath;
	}

	/**
	 * �������ı��渽����ָ��Ŀ¼�
	 */
	private void saveFile(String fileName, InputStream in) throws Exception {
		String osName = System.getProperty("os.name");
		String storedir = getAttachPath();
		String separator = "";
		if (osName == null)
			osName = "";
		if (osName.toLowerCase().indexOf("win") != -1) {
			separator = "\\";
			if (storedir == null || storedir.equals(""))
				storedir = "c:\\tmp";
		} else {
			separator = "/";
			storedir = "/tmp";
		}
		File storefile = new File(storedir + separator + fileName);
		System.out.println("storefile's path: " + storefile.toString());
		// for(int i=0;storefile.exists();i++){
		// storefile = new File(storedir+separator+fileName+i);
		// }
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(storefile));
			bis = new BufferedInputStream(in);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write(c);
				bos.flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception("�ļ�����ʧ��!");
		} finally {
			bos.close();
			bis.close();
		}
	}

	/**
	 * PraseMimeMessage�����
	 */
	public static void main(String args[]) {
		try {
			// Properties props = new Properties();
			// Session session = Session.getDefaultInstance(props, null);
			String sysbasePath = Config.getString("email.maildir");
			File newDir = new File(sysbasePath);

			Calendar now = Calendar.getInstance();
			String bakDir = FilenameUtils.concat(sysbasePath, "bak");
			bakDir = FilenameUtils.concat(bakDir,
					((now.get(Calendar.YEAR)) + ""));
			bakDir = FilenameUtils.concat(bakDir,
					((now.get(Calendar.MONTH)) + ""));
			bakDir = FilenameUtils.concat(bakDir, ((now
					.get(Calendar.DAY_OF_MONTH)) + ""));
			System.out.println("bakdir " + bakDir);
			File dBakDir = new File(bakDir);
			if (!dBakDir.isDirectory()) {
				dBakDir.mkdirs();
			}

			MailParser pmm = null;
			if (newDir.isDirectory()) {
				File[] fileArr = newDir.listFiles();
				for (int i = 0; i < fileArr.length; i++) {
					// MimeMessage msg = new MimeMessage(session,new
					// FileInputStream(fileArr[i]));
					if (!fileArr[i].isFile()) {
						continue;
					}
					pmm = new MailParser(fileArr[i]);
					System.out.println("Message " + i + " subject: "
							+ pmm.getSubject());
					System.out.println("Message " + i + " sentdate: "
							+ pmm.getSentDate());
					// System.out.println("Message " + i + " replysign: "
					// + pmm.getReplySign());
					// System.out.println("Message " + i + " hasRead: "
					// + pmm.isNew());
					// System.out.println("Message "+i+" containAttachment:
					// "+pmm.isContainAttach((Part)msg));
					System.out.println("Message " + i + " form: "
							+ pmm.getFromAddress());
					System.out.println("Message " + i + " to: "
							+ pmm.getMailAddress("to"));
					// System.out.println("Message " + i + " cc: "
					// + pmm.getMailAddress("cc"));
					// System.out.println("Message " + i + " bcc: "
					// + pmm.getMailAddress("bcc"));

					// System.out.println("Message " + i + " Message-ID: "
					// + pmm.getMessageId());
					// pmm.getMailContent((Part)msg);
					System.out.println("Message " + i + " bodycontent: \r\n"
							+ pmm.getBodyText());

					FileUtils.copyFileToDirectory(fileArr[i], dBakDir);
					fileArr[i].delete();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
}
