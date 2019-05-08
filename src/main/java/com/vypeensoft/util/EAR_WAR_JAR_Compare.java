package com.vypeensoft.util;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;

public class EAR_WAR_JAR_Compare {
    private static List<String> inputFolderArray = new ArrayList<String>();
	private static List<String> exclusionList = new ArrayList<String>();
    
	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			inputFolderArray.add(".");
		}
		if (args != null && args.length >= 1) {
			inputFolderArray.add(args[0]);
		}
		if (args != null && args.length >= 2) {
			inputFolderArray.add(args[1]);
		}
		if (args != null && args.length >= 3) {
			inputFolderArray.add(args[2]);
		}

		Map hm = PropertiesLoader.loadToHashMap("ignore_files.properties");
		exclusionList = (List<String>)hm.get("IGNORE_JAR");
		
		processIt("zip");
		processIt("ear");
		processIt("war");
		processIt("jar");

	}
	public static void processIt(final String extention) throws IOException {

		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < inputFolderArray.size(); i++) {
			String oneFolder = inputFolderArray.get(i);
			fileList.addAll(listFilesForFolder(new File(oneFolder), extention));
			System.out.println("oneFolder size()=" + fileList.size());
		}
		Collections.sort(fileList);

		System.out.println("Final.size()" + fileList.size());
		for (int i = 0; i < fileList.size(); i++) {
			String fileName = fileList.get(i);
			System.out.println("fileName=" + fileName);
			unzip(fileName, fileName+"_extracted");
			File file = new File(fileName);
			File renFile = new File(fileName+".123");
			file.renameTo(renFile);
		}
	}

	/*------------------------------------------------------------------------------*/
	public static List<String> listFilesForFolder(final File folder, final String extention) {
		List<String> returnList = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				returnList.addAll(listFilesForFolder(fileEntry, extention));
			} else {
				String fileName = fileEntry.getAbsolutePath();
				if (fileName.endsWith(extention)) {
					if(exclusionList.contains(fileEntry.getName())) {
						//dont add to list
					} else {
						returnList.add(fileEntry.getAbsolutePath());
					}
				}
			}
		}
		return returnList;
	}

	/*------------------------------------------------------------------------------*/
	public static void unzip(String source, String destination){
		//String password = "password";

		try {
			 ZipFile zipFile = new ZipFile(source);
			 //if (zipFile.isEncrypted()) {
			 //   zipFile.setPassword(password);
			 //}
			 zipFile.extractAll(destination);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}	
	/*------------------------------------------------------------------------------*/
	/*------------------------------------------------------------------------------*/
}
