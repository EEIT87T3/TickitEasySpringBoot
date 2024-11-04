package com.eeit87t3.tickiteasy.image;

import java.io.File;

/**
 * @author Chuan (chuan13)
 */
public enum ImageDirectory {
	TEST("test"),
	MEMBER("member"),
	EVENT("event"),
	PRODUCT("product"),
	CWDFUNDING("cwdfunding"),
	POST("post");
	
	private final File directory;
	private final String fileNamePrefix;
	
	private ImageDirectory(String enumString) {
		File directory = new File(System.getProperty("user.dir") + "/images/" + enumString);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		this.directory = directory;
		
		this.fileNamePrefix = "/images/" + enumString + "/";
	}
	
	public File getDirectory() {
		return directory;
	}
	public String getFileNamePrefix() {
		return fileNamePrefix;
	}
}