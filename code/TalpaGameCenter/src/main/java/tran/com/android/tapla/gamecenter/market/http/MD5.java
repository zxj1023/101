package tran.com.android.tapla.gamecenter.market.http;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String getMD5(String val) throws NoSuchAlgorithmException{    
        MessageDigest md5 = MessageDigest.getInstance("MD5");    
        md5.update(val.getBytes());    
        byte[] m = md5.digest();
        return getString(m);    
    }

    private static String getString(byte[] b){    
        StringBuffer buf = new StringBuffer();    
         for(int i = 0; i < b.length; i ++){    
             int a = b[i];  
             if(a<0)  
                 a+=256;  
             if(a<16)  
                 buf.append("0");  
             buf.append(Integer.toHexString(a));
         }            
         //return buf.toString().substring(8,24);  //16
         return buf.toString();  //32
    }
}

