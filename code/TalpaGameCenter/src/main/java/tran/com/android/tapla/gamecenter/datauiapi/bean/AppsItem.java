/*
 * Copyright (C) 2010-2012 TENCENT Inc.All Rights Reserved.
 *
 * FileName: BannerItem
 *
 * Description:  海报图中每项数据
 *
 * History:
 *  1.0   kodywu (kodytx@gmail.com) 2010-11-30   Create
 */
package tran.com.android.tapla.gamecenter.datauiapi.bean;

public class AppsItem {
	private String bigIcon;
	private String bigScreenshots;
	private String category;
	private String 	categoryIcon;
	private String 	changeLog;
	private String 	 description;
	private String 	downloadAddress;
	private String 			extraDescription;
	private String 			extraFile;
	private int 			extraFileSize;
	private String 			fileMd5;
	private int			fileSize;
	private String 			icon;
	private int				minSystemVersion;
	private String 			packageName;
	private int				publishId;
	private String 			publishTime;
	private String 			requirements;
	private  String 	screenshots;
	private String 			title;
	private String 			type;
	private String 			updateTime;
	private int				versionCode;
	private String 			versionName;
	private int				categoryId;
	private int				categoryType;
	private int				rate;
	private int				downloadTotal;
	private int downloadAmount;

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getDownloadTotal() {
		return downloadTotal;
	}

	public void setDownloadTotal(int downloadTotal) {
		this.downloadTotal = downloadTotal;
	}

	public int getDownloadAmount() {
		return downloadAmount;
	}

	public void setDownloadAmount(int downloadAmount) {
		this.downloadAmount = downloadAmount;
	}

	public String getBigIcon() {
		return bigIcon;
	}

	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}

	public String getBigScreenshots() {
		return bigScreenshots;
	}

	public void setBigScreenshots(String bigScreenshots) {
		this.bigScreenshots = bigScreenshots;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryIcon() {
		return categoryIcon;
	}

	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}

	public String getChangeLog() {
		return changeLog;
	}

	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}

	public String getExtraDescription() {
		return extraDescription;
	}

	public void setExtraDescription(String extraDescription) {
		this.extraDescription = extraDescription;
	}

	public String getExtraFile() {
		return extraFile;
	}

	public void setExtraFile(String extraFile) {
		this.extraFile = extraFile;
	}

	public int getExtraFileSize() {
		return extraFileSize;
	}

	public void setExtraFileSize(int extraFileSize) {
		this.extraFileSize = extraFileSize;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getMinSystemVersion() {
		return minSystemVersion;
	}

	public void setMinSystemVersion(int minSystemVersion) {
		this.minSystemVersion = minSystemVersion;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getPublishId() {
		return publishId;
	}

	public void setPublishId(int publishId) {
		this.publishId = publishId;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public  String getScreenshots() {
		return screenshots;
	}

	public void setScreenshots( String screenshots) {
		this.screenshots = screenshots;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}
}
