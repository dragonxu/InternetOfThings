package com.tyloo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class FileUtil {
	/**
	 * @param filepath
	 *            Ҫ�������ļ�·��
	 * @return �õ��ļ�ȫ��
	 */
	public static String getFileName(String filepath) {
		String fileNameLong = filepath;
		fileNameLong = fileNameLong.replace('\\', '/');
		String[] pathParts = fileNameLong.split("/");
		String fileName = pathParts[pathParts.length - 1];
		return fileName;
	}

	/**
	 * �õ�fileurl��ַ
	 * 
	 * @param filepath
	 * @return
	 */
	public static String getFileUrl(String filepath) {
		String fileNameLong = filepath;
		fileNameLong = fileNameLong.replace('\\', '/');
		String[] pathParts = fileNameLong.split("/");
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < pathParts.length; i++) {
			if ((i + 1) < pathParts.length) {
				ret.append(pathParts[i]);
				ret.append("/");
			} else {
				ret.append(StringUtil.urlencoder(pathParts[i]));
			}
		}
		return ret.toString();
	}

	/**
	 * @param fileName
	 *            Ҫ�������ļ���
	 * @return �õ��ļ���
	 */
	public static String getName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * @param fileName
	 *            Ҫ�������ļ�ȫ��
	 * @return �õ��ļ���չ��
	 */
	public static String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * @param fileType
	 *            ����ʹ�õ���չ��
	 * @param ext
	 *            ���ڵ���չ��
	 * @return �Ƿ���ȷ
	 */
	public static boolean extIsAllowed(String fileType, String ext) {
		if (fileType.length() == 0)
			return true;
		fileType = fileType.toLowerCase();
		String[] AllowList = fileType.split(",");
		for (int i = 0; i < AllowList.length; i++) {
			if (AllowList[i].equals(ext)) {
				return true;
			}
		}
		return false;
	}

	public static long getLastModifedTime(String fileName) {
		File file = new File(fileName);
		long modifiedTime = file.lastModified();
		return modifiedTime;
	}

	/**
	 * @param filepath
	 *            ��ѯ���ļ�
	 * @return �ļ��Ƿ����
	 */
	public static boolean exists(String filepath) {
		if (new File(filepath).exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param filepath
	 *            ��ѯ���ļ�
	 * @return �ļ��Ĵ�С
	 */
	public static long getSize(String filepath) {
		File file = new File(filepath);
		return file.length();
	}

	/**
	 * @param path
	 *            ��ѯ��Ŀ¼
	 * @return �ǲ���Ŀ¼�����Ƿ�������Ŀ¼
	 */
	public static boolean isDir(String path) {
		if (new File(path).isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param path
	 *            Ҫ��Ŀ¼�ĵ�ַ
	 */
	public static void mkDir(String path) {
		if (!new File(path).isDirectory()) {
			new File(path).mkdirs();
		}
	}

	/**
	 * ���ļ���
	 * 
	 * @param filepath
	 * @param topath
	 * @return
	 */
	public static boolean renameFile(String filepath, String topath) {
		return new File(filepath).renameTo(new File(topath));
	}

	/**
	 * @param filepath
	 *            ��ȡ�ļ�
	 * @return �ļ�����
	 */
	public static String readFile(String filepath, String str) {
		File file = new File(filepath);
		if (!file.exists()) {
			return "";
		} else if (file != null) {
			try {
				return FileUtils.readFileToString(file, str);
			} catch (IOException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public static String readFile(String filepath) {
		return FileUtil.readFile(filepath, "GBK");
	}

	/**
	 * @param filepath
	 *            Ҫд���ļ�
	 * @param value
	 *            Ҫд���ֵ
	 * @throws IOException
	 */
	public static void writeFile(String filepath, String value)
			throws IOException {
		File f = new File(filepath);
		PrintWriter out = new PrintWriter(new FileWriter(f));
		out.print(value);
		out.close();
	}

	/**
	 * @param path
	 *            Ҫ�г���Ŀ¼
	 * @return
	 */
	public static ArrayList<String> getFileList(String path) {
		if (FileUtil.isDir(path)) {
			File f = new File(path);
			File[] fs = f.listFiles();
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < fs.length; i++) {
				if (!fs[i].isDirectory()) {
					list.add(fs[i].getName());
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * @param path
	 *            Ҫ�г���Ŀ¼
	 * @return
	 */
	public static ArrayList<String> getDirList(String path) {
		if (FileUtil.isDir(path)) {
			File f = new File(path);
			File[] fs = f.listFiles();
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					if (!fs[i].getName().equals(".")) {
						list.add(fs[i].getName());
					}
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * @param path
	 *            Ҫɾ�����ļ�·��
	 * @return
	 */
	public static boolean delFile(String path) {
		if (FileUtil.exists(path)) {
			File f = new File(path);
			return f.delete();
		} else {
			return true;
		}
	}

	/**
	 * @param path1
	 *            Ҫ�������ļ�
	 * @param path2
	 *            ������Ŀ�ĵ�
	 * @throws IOException
	 */
	public static void copyFile(String path1, String path2) {
		if (FileUtil.exists(path1)) {
			File in = new File(path1);
			File out = new File(path2);
			FileInputStream fis = null;
			FileOutputStream fos = null;

			try {
				fis = new FileInputStream(in);
				fos = new FileOutputStream(out);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] buf = new byte[1024];
			int i = 0;
			try {
				while ((i = fis.read(buf)) != -1) {
					fos.write(buf, 0, i);
				}
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean creatFile(String filenameTemp) throws IOException {
		boolean flag = false;
		File filename = new File(filenameTemp);
		if (!filename.exists()) {
			filename.createNewFile();
			flag = true;
		}
		return flag;
	}
}
