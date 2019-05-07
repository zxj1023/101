/*
 * @author zw
 */
package com.tran.com.android.gc.update.util;

import android.content.Context;

import com.tran.com.android.gc.update.http.data.HttpRequestGetMarketData;
import com.tran.com.android.gc.update.model.DownloadData;
import com.tran.com.android.gc.update.datauiapi.bean.UpgradeListObject;
import com.tran.com.android.gc.update.datauiapi.bean.upappListtem;
import com.tran.com.android.gc.update.datauiapi.bean.upcountinfo;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataFromUtils {

	public ArrayList<DownloadData> getUpdateData(Context context) {
		String result = HttpRequestGetMarketData.getUpAppListObject(context);

		ObjectMapper mapper = new ObjectMapper();
		result = result.replaceAll("\t", "");

		UpgradeListObject obj = new UpgradeListObject();
		try {
			mapper.configure(
					DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
					true);
			mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			mapper.getDeserializationConfig()
					.set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
							false);

			obj = (UpgradeListObject) mapper.readValue(result,
					UpgradeListObject.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;

		} catch (JsonMappingException e) {
			e.printStackTrace();
			return null;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<DownloadData> down_data = new ArrayList<DownloadData>();
		upDownLoadData(obj, down_data);

		return down_data;
	}

	
	public int getUpdateSum(Context context) {
		String result = HttpRequestGetMarketData.getUpdateCountObject(context);

		ObjectMapper mapper = new ObjectMapper();
		result = result.replaceAll("\t", "");

		upcountinfo obj = new upcountinfo();
		try {
			mapper.configure(
					DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
					true);
			mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			mapper.getDeserializationConfig()
					.set(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
							false);

			obj = (upcountinfo) mapper.readValue(result,
					upcountinfo.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return 0;

		} catch (JsonMappingException e) {
			e.printStackTrace();
			return 0;

		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	

		return obj.getCount();
	}

	public void upDownLoadData(UpgradeListObject up_obj,
			List<DownloadData> down_data) {
		for (int i = 0; i < up_obj.getUpgradeApps().size(); i++) {
			DownloadData tmp_data = new DownloadData();
			upappListtem list = up_obj.getUpgradeApps().get(i);
			tmp_data.setApkId(list.getId());
			tmp_data.setApkDownloadPath(list.getDownloadURL());
			tmp_data.setApkLogoPath(list.getIcons());
			tmp_data.setApkName(list.getTitle());
			tmp_data.setPackageName(list.getPackageName());
			tmp_data.setVersionCode(list.getVersionCode());
			tmp_data.setVersionName(list.getVersionName());
            tmp_data.setForceUpFlag(list.getForceUpFlag());
            down_data.add(tmp_data);
        }
    }

}
