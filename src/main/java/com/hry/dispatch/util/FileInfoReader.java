package com.hry.dispatch.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileInfoReader {
	
	File localFile = null;
	
	public FileInfoReader(String filePath) {
		localFile = new File(filePath);
	}
	
	public ArrayList<String> readAll() {
		FileReader fr = null;
		BufferedReader bufferReader = null;
		InputStreamReader isr = null;
		String info = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
//			fr = new FileReader(localFile);
			isr = new InputStreamReader(new FileInputStream(localFile),"UTF-8");
			bufferReader = new BufferedReader(isr);
			while (true) {
				info = bufferReader.readLine();
				if (info != null && info.length() > 0) {
					list.add(info);
				}
				if (info == null) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception allE) {
			allE.printStackTrace();
		} finally {
			try {
				bufferReader.close();
//				fr.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public String readln() {
		FileReader fr = null;
		BufferedReader bufferReader = null;
		String info = null;
		try {
			fr = new FileReader(localFile);
			bufferReader = new BufferedReader(fr);
			info = bufferReader.readLine();
			return info;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception allE) {
			allE.printStackTrace();
		} finally {
			try {
				bufferReader.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
