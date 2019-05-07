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

import java.util.ArrayList;
import java.util.List;

public class Data {

	    private List<AppsItem> apps = new ArrayList<AppsItem>();

	public List<AppsItem> getApps() {
		return apps;
	}

	public void setApps(List<AppsItem> apps) {
		this.apps = apps;
	}
}
