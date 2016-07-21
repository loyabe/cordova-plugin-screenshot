package com.xmexe.exe.ScreenShot;
/**
 * Created by Abay on 2015/8/24.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScreenShot extends CordovaPlugin {
    private static final String LOG_TAG = "ScreenShot";
    private ScreenShot instance = null;
    public ScreenShot() {
        instance = this;
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//        final CordovaWebView wv = webView;

        Log.d(LOG_TAG, "No Method In This Plugin");
        super.initialize(cordova, webView);
        RxScreenshotDetector.start(webView.getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(this.<String>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String path) {
//                        mTextView.setText(mTextView.getText() + "\nScreenshot: " + path);

                        Log.e("MainActivity", "path ： " + path);
                            File file = new File(path);
                            if (file.getName().contains("_cropped")){
                                return;
                            }
                            try {
                                File saveFile = saveSmallBitmap(file.getAbsolutePath());

                                //            String js = String
                                String js = String .format("cordova.plugins.ScreetShot.receiveMessageInAndroidCallback('%s');",
                                        saveFile.getAbsoluteFile());

                                try {
                                    if (instance != null){
                                        instance.webView.sendJavascript(js);
                                    }


                                } catch (NullPointerException e) {

                                } catch (Exception e) {

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                });

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(LOG_TAG, "No Method In This Plugin");
        return false;
    }



    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param imagesrc
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 320, 480);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFile(filePath, options);
    }
    public static File saveSmallBitmap(String filepath) throws IOException {
        Bitmap bitmap = getSmallBitmap(filepath);
        File file = new File(filepath.trim());
        String fileName = file.getName();
        String path = filepath.substring(0, filepath.lastIndexOf("/"));
        String newFileName = path + "/_cropped_" + fileName;
        File f = new File(newFileName);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

//    OnScreenShotTakenListener mOnScreenShotTakenListener = new OnScreenShotTakenListener(){
//        public void onScreenshotTaken(Uri uri){
//            Log.e("MainActivity", "onScreenshotTaken");
//            Log.e("MainActivity", "onScreenshotTaken " + uri.getPath());
//
//            String js = String
//                    .format("cordova.plugins.ScreetShot.receiveMessageInAndroidCallback('%s');",
//                            uri.getPath());
//
//
//            try {
//                if (instance != null){
//                    instance.webView.sendJavascript(js);
//                }
//
//
////			String jsEvent=String
////					.format("cordova.fireDocumentEvent('jpush.receiveMessage',%s)",
////							data.toString());
////			instance.webView.sendJavascript(jsEvent);
//            } catch (NullPointerException e) {
//
//            } catch (Exception e) {
//
//            }
//
//        }
//    };
}