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

public class appiteminfo {
	// 应用ID
	private int id;
	// 应用名称
	private String title;
	// 应用的包名
	private String packageName;
	// APK最新的更新时间
	private String createTime;
	// 版本号
	private int versionCode;
	// 版本名
	private String versionName;
	// 开发者信息
	private String developer;
	// 这个应用所属的分类 ,最多有2个分类
	private String category;
	// 好评率，比如：75表示75%的人给了好评
	private int likesRate;
	// 应用的下载量
	private int downloadCount;
	// 应用下载量的文字表示，便于人们阅读
	private String downloadCountStr;
	//更新的描述
	private String changelog;
	// 文件大小
	private int appSize;
	// 文件大小的字符串表示，便于人们阅读
	private String appSizeStr;
	// 应用的下载地址
	private String downloadURL;
	// 应用的一组图标地址，px256表示256像素
	private iconItem icons = new iconItem();
	
	
	//应用截图
	private String[] screenshots;
	//应用描述
	private String description;
	//应用来源
	private String market;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getLikesRate() {
		return likesRate;
	}

	public void setLikesRate(int likesRate) {
		this.likesRate = likesRate;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getDownloadCountStr() {
		return downloadCountStr;
	}

	public void setDownloadCountStr(String downloadCountStr) {
		this.downloadCountStr = downloadCountStr;
	}

	public int getAppSize() {
		return appSize;
	}

	public void setAppSize(int appSize) {
		this.appSize = appSize;
	}

	public String getAppSizeStr() {
		return appSizeStr;
	}

	public void setAppSizeStr(String appSizeStr) {
		this.appSizeStr = appSizeStr;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public iconItem getIcons() {
		return icons;
	}

	public void setIcons(iconItem icons) {
		this.icons = icons;
	}

	public String[] getScreenshots() {
		return screenshots;
	}

	public void setScreenshots(String[] screenshots) {
		this.screenshots = screenshots;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}


}
