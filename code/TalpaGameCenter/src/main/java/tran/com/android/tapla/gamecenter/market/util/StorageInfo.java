package tran.com.android.tapla.gamecenter.market.util;


public class StorageInfo {
    public String path;
    public String state;
    boolean isRemoveable;

    StorageInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    boolean isMounted() {
        return "mounted".equals(state);
    }

    @Override
    public String toString() {
        return "StorageInfo{" +
                "path='" + path + '\'' +
                ", state='" + state + '\'' +
                ", isRemoveable=" + isRemoveable +
                '}';
    }
}
