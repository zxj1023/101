package tran.com.android.tapla.gamecenter.entity;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/7 14:15
 */

public class PramJson {


    public PramJson(int p,int c){
        this.page = p;
        this.count = c;
    }
    private int page = 0;
    private int count = 10;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
