package tran.com.android.tapla.gamecenter.datauiapi.bean;

import java.util.ArrayList;

/**
 * Created by suwei.tang on 2017/9/1.
 */

public class Push {
    private int code;		//操作结果代码，0表示失败，1表示成功
    private String desc;		//对操作结果的描述，如果操作失败，描述失败原因
    private ArrayList<PushInfoItem> pushInfoes;		//要推送的消息列表

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

    public ArrayList<PushInfoItem> getPushInfoes() {
        return pushInfoes;
    }

    public void setPushInfoes(ArrayList<PushInfoItem> pushInfoes) {
        this.pushInfoes = pushInfoes;
    }
}
