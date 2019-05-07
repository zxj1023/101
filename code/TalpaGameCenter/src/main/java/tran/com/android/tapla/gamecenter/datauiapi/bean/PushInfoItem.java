package tran.com.android.tapla.gamecenter.datauiapi.bean;

import java.util.Date;

/**
 * Created by suwei.tang on 2017/9/1.
 */

public class PushInfoItem {
    private int id; //	应用ID，在服务器端数据库中存储的ID
    private String theme;		//消息主题
    private String summary;
    private int action;		//1跳游戏详情，2跳转专题，3跳WAP页面
    private String to;	//包名或者专题ID，或者wap url
    private String starttime;	//开始时间时间
    private String endtime;		//结束时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
