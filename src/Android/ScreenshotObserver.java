package com.xmexe.exe.ScreenShot;

import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;

import java.io.File;

/**
 * Created by Abay Zhuang on 2015/8/13.
 */
public class ScreenshotObserver extends FileObserver {
    private static final String TAG = "ScreenshotObserver";
//    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/Pictures/Screenshots/";
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Screenshots/";
    private OnScreenShotTakenListener listener;
    private String lastTakenPath;

    public ScreenshotObserver(OnScreenShotTakenListener listener) {
        super(PATH, FileObserver.CLOSE_WRITE);
        this.listener = listener;
    }

    @Override
    public void onEvent(int event, String path) {
        Log.i(TAG, "ScreenshotO to onEvent. " + path);
        if (path==null || event!=FileObserver.CLOSE_WRITE){
            //dont care
        }
        else if (lastTakenPath!=null && path.equalsIgnoreCase(lastTakenPath)){
            //had observer,ignore this
        }
        else {
            lastTakenPath = path;
            File file = new File(PATH+path);
            if (listener != null)
               listener.onScreenshotTaken(Uri.fromFile(file));
        }
    }

    public void start() {
        super.startWatching();
    }

    public void stop() {
        super.stopWatching();
    }
}