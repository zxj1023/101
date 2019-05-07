package tran.com.android.tapla.gamecenter.datauiapi;

import android.content.Context;
import android.content.SharedPreferences;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

import tran.com.android.tapla.gamecenter.datauiapi.bean.Push;
import tran.com.android.tapla.gamecenter.datauiapi.bean.PushInfoItem;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.market.db.CacheDataAdapter;
import tran.com.android.tapla.gamecenter.market.db.PushDataAdapter;
import tran.com.android.tapla.gamecenter.market.http.data.HttpRequestGetMarketData;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.TimeUtils;

/**
 * Created by suwei.tang on 2017/9/1.
 */

public class PushManager implements  Runnable{

    private Context mContext = null;

    public static final String PUSHACTION = "tran.com.talpagame.action.PUSH";

    public PushManager(Context context){
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            String result = new String();
            PushDataAdapter ldb = new PushDataAdapter(mContext);
            ldb.open();

            result = HttpRequestGetMarketData.getAppPush(mContext);

            if (result != null && result.length() > 0) {
                //删除之前的老数据 ，不会立马删除数据，删除数据唯一的判断就是是否过了有效期。
//                ldb.deleteAll();

                //保存最新的数据
                Push  push = parseResponse(result, Push.class);
                if(push != null && push.getPushInfoes().size()>0){
                    ldb.insert(push.getPushInfoes());
                    updatePreferencePush(mContext,0);
                }
            }
            ldb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePreferencePush(Context context,int type)
    {
        SharedPreferences sp1 = context.getSharedPreferences(
                Globals.SHARED_DATA_UPDATE, context.MODE_APPEND);
        SharedPreferences.Editor ed = sp1.edit();
        if(type == 0)
            ed.putString(Globals.SEARCH_PUSH_DATA_CACHE_UPDATETIME, TimeUtils.getStringDateShort());
        else
            ed.putString(Globals.SEARCH_PUSH_DATA_CACHE_UPDATETIME, "0");
        ed.commit();
    }

    public <T> T parseResponse(String result, Class<? extends T> cl)  {
        T t = null;
        ObjectMapper mapper = new ObjectMapper();
        result = result.replaceAll("\t", "");
        try {
            mapper.configure(
                    DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                    true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            mapper.getDeserializationConfig()
                    .set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                            false);
            t =  (T) mapper.readValue(result, cl);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
}
