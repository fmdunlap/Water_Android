package itp341.dunlap.forrest.water.activities;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by FDUNLAP on 5/1/2017.
 */

public class App extends Application {

    private static Context mContext;

    //Used exclusively to get the context of the app and to set up the Facebook SDK

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

}
