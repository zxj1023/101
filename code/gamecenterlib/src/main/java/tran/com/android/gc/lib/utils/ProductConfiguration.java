/*******************************************************************************
 * Filename:
 * ---------
 *  ProductConfiguration.java
 *
 * Project:
 * --------
 *   Browser
 *
 * Description:
 * ------------
 *  get UA infor
 *
 * Author:
 * -------
 *  2012.07.19  
 *
 ****************************************************************************/
package tran.com.android.gc.lib.utils;

import java.util.Locale;

import android.os.Build;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ProductConfiguration {

	public static String getUAString(){
		/*
		UA: Mozilla/5.0 (Linux; U; Android 4.0.3; zh-cn;GiONEE-GN868/Phone

		Build/IMM76D) AppleWebKit534.30(KHTML,like Gecko)Version/4.0 Mobile

		Safari/534.30 Id/54D9886DF90D58715DFFFA2306D30075

		*/
        String brand = SystemProperties.get("ro.product.brand", "GiONEE");
        String model = SystemProperties.get("ro.product.model", "Phone");
        String extModel = SystemProperties.get("ro.gn.extmodel", "Phone");
        String romVer = SystemProperties.get("ro.gn.gnromvernumber", "GiONEE ROM4.0.1");
        String Ver = romVer.substring(romVer.indexOf("M") == -1 ? 0 : (romVer.indexOf("M")+1));
	
        //Date date = new Date(Build.TIME);
        //String strTime = new SimpleDateFormat("MM.dd.yyyy").format(date);
        String language = Locale.getDefault().getLanguage();	//zh
        String country = Locale.getDefault().getCountry().toLowerCase();	//cn
	
        String strImei = TelephonyManager.getDefault().getDeviceId();
        String decodeImei = DecodeUtils.get(strImei);
        //gionee: dengll 20121114 modify for CR00729527 begin
        /*
        String uaString = "Mozilla/5.0 (Linux; U; Android " + Build.VERSION.RELEASE +"; " 
        + language + "-" + country + ";" + brand + "-" + model + "/" + extModel 
        + " Build/IMM76D) AppleWebKit534.30(KHTML,like Gecko)Version/4.0 Mobile Safari/534.30 Id/"
        + decodeImei + " RV/" + Ver; 
        */
        String uaString = null;        
        String optr = SystemProperties.get("ro.operator.optr");
        if(null != optr && optr.equals("OP02")){
            uaString = "Mozilla/5.0 (Linux; U; Android " + Build.VERSION.RELEASE +"; " 
               + language + "-" + country + ";" + brand + "-" + model + "/" + extModel 
               + " Build/IMM76D) AppleWebKit534.30(KHTML,like Gecko)Version/4.0 Mobile Safari/534.30 Id/"
                + decodeImei + " RV/" + Ver
                    + " "
                    + "GNBR/"
                    + "v1.5.1.h"
                    + " "
                    + "(securitypay,securityinstalled)";
        }else{
            uaString = "Mozilla/5.0 (Linux; U; Android " + Build.VERSION.RELEASE +"; " 
                + language + "-" + country + ";" + brand + "-" + model + "/" + extModel 
                + " Build/IMM76D) AppleWebKit534.30(KHTML,like Gecko)Version/4.0 Mobile Safari/534.30 Id/"
                + decodeImei + " RV/" + Ver; 
        }
        //gionee: dengll 20121114 modify for CR00729527 end

	    Log.d("ProductConfiguration", "ua:"+uaString);
		return uaString; 
	}
	
	public static String getUAString(String strImei){
        String brand = SystemProperties.get("ro.product.brand", "GiONEE");
        String model = SystemProperties.get("ro.product.model", "Phone");
        String extModel = SystemProperties.get("ro.gn.extmodel", "Phone");
        String romVer = SystemProperties.get("ro.gn.gnromvernumber", "GiONEE ROM4.0.1");
        String Ver = romVer.substring(romVer.indexOf("M") == -1 ? 0 : (romVer.indexOf("M")+1));
        String language = Locale.getDefault().getLanguage();	//zh
        String country = Locale.getDefault().getCountry().toLowerCase();	//cn
	
        String decodeImei = DecodeUtils.get(strImei);
        
        String uaString = "Mozilla/5.0 (Linux; U; Android " + Build.VERSION.RELEASE +"; " 
        + language + "-" + country + ";" + brand + "-" + model + "/" + extModel 
        + " Build/IMM76D) AppleWebKit534.30(KHTML,like Gecko)Version/4.0 Mobile Safari/534.30 Id/"
        + decodeImei  + " RV/" + Ver;
        Log.d("ProductConfiguration", "ua:"+uaString);
		return uaString; 
	}
}

