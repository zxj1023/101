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

import java.util.List;

public class topVideoItem {

	//应用packagename
	private String packageName;
	//应用id
	private String id;
	//应用图片
	private String picURL;

	private String to;
	private int action;

	// 类别
	private int bannerType;

	private List<String> datas;

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getBannerType() {
		return bannerType;
	}

	public void setBannerType(int bannerType) {
		this.bannerType = bannerType;
	}

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
