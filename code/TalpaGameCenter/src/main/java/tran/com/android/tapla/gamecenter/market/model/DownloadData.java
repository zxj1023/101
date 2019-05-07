package tran.com.android.tapla.gamecenter.market.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadData implements Parcelable{

	private int apkId; // 软件ID，可用在发送通知图标用
	private String apkName; // 软件名字，显示在UI上
	private String apkDownloadPath; // 下载地址
	private String versionName; // 版本，用于对比手机上的
	private int versionCode; // 版本码，用于对比手机上的
	private String packageName; // 包名，用于检查手机是否已安装
	private String apkLogoPath; // 图标位置

	//banner
	private String type;
	private String to;
	private String picURL;
    private int action;

	// 以下字段只有在数据库查找时才会有
	private int status; // 状态
	private String fileDir; // 文件存放目录
	private String fileName; // 文件名称
	private long finishTime; // 任务完成时间


	//9Apps
	private String icon;
	private int rate;
	private int downloadTotal;
	private int fileSize;
	private String downloadAddress;
	private int publishId;
	private String fileMd5;
	private int categoryId;
	private int categoryType;
	private int downloadAmount;

	//为了区分是tran还是9apps返回的查询结果
	private String serverTag;



	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

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

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}

	public int getPublishId() {
		return publishId;
	}

	public void setPublishId(int publishId) {
		this.publishId = publishId;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
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

	public int getDownloadAmount() {
		return downloadAmount;
	}

	public void setDownloadAmount(int downloadAmount) {
		this.downloadAmount = downloadAmount;
	}

	public String getServerTag() {
		return serverTag;
	}

	public void setServerTag(String serverTag) {
		this.serverTag = serverTag;
	}

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getApkDownloadPath() {
		return apkDownloadPath;
	}

	public void setApkDownloadPath(String apkDownloadPath) {
		this.apkDownloadPath = apkDownloadPath;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getApkLogoPath() {
		return apkLogoPath;
	}

	public void setApkLogoPath(String apkLogoPath) {
		this.apkLogoPath = apkLogoPath;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getPicURL() {
		return picURL;
	}

	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public  static final Parcelable.Creator<DownloadData> CREATOR = new Creator<DownloadData>() {  
        @Override  
        public DownloadData createFromParcel(Parcel source) {  
        	return new DownloadData(source);
        }  
        @Override  
        public DownloadData[] newArray(int size) {  
            return new DownloadData[size];  
        }  
    }; 
    
    public DownloadData() {
		
	}
	public DownloadData(Parcel in) {
		apkId = in.readInt();
		apkName = in.readString();
		apkDownloadPath = in.readString();
		versionName = in.readString();
		versionCode = in.readInt();
		packageName = in.readString();
		apkLogoPath = in.readString();
		
		status = in.readInt();
		fileDir = in.readString();
		fileName = in.readString();
		finishTime = in.readLong();

		serverTag = in.readString();
		downloadAddress = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(this.apkId);
		dest.writeString(this.apkName);
		dest.writeString(this.apkDownloadPath);
		dest.writeString(this.versionName);
		dest.writeInt(this.versionCode);
		dest.writeString(this.packageName);
		dest.writeString(this.apkLogoPath);
		
		dest.writeInt(this.status);
		dest.writeString(this.fileDir);
		dest.writeString(this.fileName);
		dest.writeLong(this.finishTime);

		dest.writeString(this.serverTag);
		dest.writeString(this.downloadAddress);
	}
	@Override
	public String toString() {
		return "DownloadData [apkId=" + apkId + ", apkName=" + apkName
				+ ", apkDownloadPath=" + apkDownloadPath + ", versionName="
				+ versionName + ", versionCode=" + versionCode
				+ ", packageName=" + packageName + ", apkLogoPath="
				+ apkLogoPath + ", status=" + status + ", fileDir=" + fileDir
				+ ", fileName=" + fileName + ", finishTime=" + finishTime + "]";
	}

}
