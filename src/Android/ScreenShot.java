package com.xmexe.exe.ScreenShot;
/**
 * Created by Abay on 2015/8/24.
 */
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

public class ScreenShot extends CordovaPlugin {
    private static final String LOG_TAG = "ScreenShot";
    private ScreenshotObserver screenShotObserver;
    private ScreenShot instance;
    public ScreenShot() {
        instance = this;
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//        final CordovaWebView wv = webView;

        Log.d(LOG_TAG, "No Method In This Plugin");
        super.initialize(cordova, webView);
        screenShotObserver = new ScreenshotObserver(mOnScreenShotTakenListener);
        screenShotObserver.startWatching();

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(LOG_TAG, "No Method In This Plugin");
        return false;
    }

    OnScreenShotTakenListener mOnScreenShotTakenListener = new OnScreenShotTakenListener(){
        public void onScreenshotTaken(Uri uri){
            Log.e("MainActivity", "onScreenshotTaken");
            Log.e("MainActivity", "onScreenshotTaken " + uri.getPath());

            String js = String
                    .format("cordova.plugins.ScreetShot.receiveMessageInAndroidCallback('%s');",
                            uri.getPath());


            try {
                if (instance != null){
                    instance.webView.sendJavascript(js);
                }


//			String jsEvent=String
//					.format("cordova.fireDocumentEvent('jpush.receiveMessage',%s)",
//							data.toString());
//			instance.webView.sendJavascript(jsEvent);
            } catch (NullPointerException e) {

            } catch (Exception e) {

            }

        }
    };
}