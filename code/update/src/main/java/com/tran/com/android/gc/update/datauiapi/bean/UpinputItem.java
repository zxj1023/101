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
package com.tran.com.android.gc.update.datauiapi.bean;

public class UpinputItem {

    // 应用的包名
    private String packageName;
    // 版本号
    private int versionCode;
    // 版本名
    private String versionName;
    // 系统编译版本
    private String buildVersion;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

}
