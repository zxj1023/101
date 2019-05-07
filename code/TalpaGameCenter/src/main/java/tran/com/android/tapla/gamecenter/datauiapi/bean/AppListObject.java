package tran.com.android.tapla.gamecenter.datauiapi.bean;

import java.util.ArrayList;
import java.util.List;

public class AppListObject {

	private String code;
	private String desc;
	private List<appListtem> apps =  new ArrayList<appListtem>();
	private String picURL;

	private String description;
	private String specialName;

    //9APPS property
	private Search9AppsData data =  new Search9AppsData();

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}

	public String getPicURL() {
		return picURL;
	}

	public String getDescription() {
		return description;
	}

	public String getSpecialName() {
		return specialName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<appListtem> getApps() {
		return apps;
	}
	public void setApps(List<appListtem> apps) {
		this.apps = apps;
	}

	public Search9AppsData getData() {
		return data;
	}

	public void setData(Search9AppsData data) {
		this.data = data;
	}


}
