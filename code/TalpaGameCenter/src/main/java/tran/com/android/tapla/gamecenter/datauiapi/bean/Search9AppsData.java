
package tran.com.android.tapla.gamecenter.datauiapi.bean;

import java.util.ArrayList;
import java.util.List;

public class Search9AppsData {

    private String correctKeyword;
    private List<appListtem> apps =  new ArrayList<appListtem>();

    public String getCorrectKeyword() {
        return correctKeyword;
    }

    public void setCorrectKeyword(String correctKeyword) {
        this.correctKeyword = correctKeyword;
    }

    public List<appListtem> getApps() {
        return apps;
    }

    public void setApps(List<appListtem> apps) {
        this.apps = apps;
    }
}
