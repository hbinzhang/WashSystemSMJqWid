package com.hry.dispatch.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class FileInfoWriter {
	File localFile = null;
	
	public FileInfoWriter(String filePath) {
		localFile = new File(filePath);
	}
	
	public void print(String info) {
		PrintWriter pw = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(localFile, false);
			osw = new OutputStreamWriter(fos, "UTF-8");
			pw = new PrintWriter(osw);
			pw.print(info);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception allE) {
			allE.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
				try {
					osw.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void println(String info) {
		print(info + "\r\n");
	}
	
	public void printLstln(List<String> info) {
		StringBuffer sb = new StringBuffer();
		for (String s : info) {
			sb.append(s);
			sb.append("\r\n");
		}
		print(sb.toString());
	}
	
}
