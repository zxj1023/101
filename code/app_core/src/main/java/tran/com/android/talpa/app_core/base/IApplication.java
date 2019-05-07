package tran.com.android.talpa.app_core.base;

import android.app.Application;
import android.content.Context;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/6 14:19
 */

public interface IApplication {

    public Application getApplicaitonInstance();

    public Context getApplicationsContext();
}
