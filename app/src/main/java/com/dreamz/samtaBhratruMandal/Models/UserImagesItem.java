package com.dreamz.samtaBhratruMandal.Models;

import com.google.gson.annotations.SerializedName;

public class UserImagesItem{

	@SerializedName("fileName")
	private String fileName;

	@SerializedName("fileExtension")
	private Object fileExtension;

	@SerializedName("docCategory")
	private String docCategory;

	@SerializedName("filePath")
	private String filePath;

	@SerializedName("fileData")
	private Object fileData;

	@SerializedName("id")
	private int id;

	@SerializedName("userId")
	private int userId;

	@SerializedName("status")
	private String status;

	@SerializedName("createDate")
	private String createDate;

	public String getFileName(){
		return fileName;
	}

	public Object getFileExtension(){
		return fileExtension;
	}

	public String getDocCategory(){
		return docCategory;
	}

	public String getFilePath(){
		return filePath;
	}

	public Object getFileData(){
		return fileData;
	}

	public int getId(){
		return id;
	}

	public int getUserId(){
		return userId;
	}

	public String getStatus(){
		return status;
	}

	public String getCreateDate(){
		return createDate;
	}
}