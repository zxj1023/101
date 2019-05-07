package tran.com.android.tapla.gamecenter.datauiapi.bean;

import java.util.ArrayList;
import java.util.List;

public class MainListObject {
	
	//返回码  1：成功  其它失败 
	private int code;
	//成功和失败的描述 
	private String desc;
	//主界面banner字段
	private List<topVideoItem> banners = new ArrayList<topVideoItem>();
	//主界面list数据
	private List<MainListItem> apps =  new ArrayList<MainListItem>();

//	code	操作结果代码，0表示失败，1表示成功
//	desc	对操作结果的描述，如果操作失败，描述失败原因
//	banners	用于显示在banners区域的信息
//	to	应用的包名或者专题id
//	picURL	显示在banner上图片的地址
//	action	跳转专题或者游戏详情
//	apps	操作返回的游戏列表



	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<topVideoItem> getBanners() {
		return banners;
	}
	public void setBanners(List<topVideoItem> banners) {
		this.banners = banners;
	}
	public List<MainListItem> getApps() {
		return apps;
	}
	public void setApps(List<MainListItem> apps) {
		this.apps = apps;
	}

}
