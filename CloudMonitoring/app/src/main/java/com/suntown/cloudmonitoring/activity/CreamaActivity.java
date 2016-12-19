package com.suntown.cloudmonitoring.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.bean.ShopXmlBean;
import com.suntown.cloudmonitoring.bean.TagDetialBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;
import com.suntown.cloudmonitoring.utils.Xml2Json;
import com.suntown.cloudmonitoring.zxing.camera.CameraManager;
import com.suntown.cloudmonitoring.zxing.decoding.CaptureActivityHandler;
import com.suntown.cloudmonitoring.zxing.decoding.InactivityTimer;
import com.suntown.cloudmonitoring.zxing.view.ViewfinderView;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


public class CreamaActivity extends FragmentActivity implements Callback {

    private static final String TAG = "CreamaActivity";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private boolean isOnScanner;
    private ArrayList<String> scannerList = new ArrayList<>();
    private String serverIP;
    private String userId;
    private int intType;
    private String resultString;
    private OkHttpClient client;
    private String shelfGoods;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creama);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        shelfGoods = SPUtils.getString(this, Constant.SHELFGOODS);
        shelfGoods = shelfGoods.toUpperCase();
        Log.i(TAG,"shelfGoods："+shelfGoods);
        //TODO　判断是连扫还是单扫
        client = new OkHttpClient();
        Intent intent = getIntent();
        isOnScanner = intent.getBooleanExtra(Constant.IS_ON_SCANN, false);
        intType = intent.getIntExtra(Constant.SCANN_TYPE, 0);
        Log.i(TAG, isOnScanner + "");
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * ����ɨ����
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        resultString = result.getText();
        Log.i(TAG, "resultStr-" + resultString);
        if (resultString.equals("")) {
            Toast.makeText(CreamaActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else {
            if (isOnScanner) {
                if (intType == 1) {
                    if (resultString.contains(".")) {
                        //查询  标签是否存在 其他货架，若存在 tanchudialog
                        initData(resultString, 1);
                    } else {
                        Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.RESULT_CODE, resultString);
                        resultIntent.putExtras(bundle);
                        this.setResult(RESULT_OK, resultIntent);
                        CreamaActivity.this.finish();
                    }
                } else {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.RESULT_CODE, resultString);
                    resultIntent.putExtras(bundle);
                    this.setResult(RESULT_OK, resultIntent);
                    CreamaActivity.this.finish();
                }

            } else {
                if (resultString.contains(".")) {
                    //查询  标签是否存在 其他货架，若存在 tanchudialog
                    initData(resultString, 2);
                } else {
                    Utils.showToast(CreamaActivity.this,"扫描成功，继续扫描");
//                    addData(resultString);
                    scannerList.add(resultString);
//                    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//                    SurfaceHolder surfaceHolder = surfaceView.getHolder();
//                    initCamera(surfaceHolder);
                   handler.postDelayed(() -> {
                       if (handler != null) {
                           handler.restartPreviewAndDecode();
                       }
                   },2000);

                }
            }
        }

    }

    public void addData(String resultString) {
        if (!scannerList.contains(resultString)) {
            scannerList.add(resultString);
        }
    }

    private void initData(String tinyip, int i) {
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
            userId = SPUtils.getString(this, Constant.USER_ID);
        } else {
            userId = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        String sid = SPUtils.getString(this, Constant.SID);
        RequestBody formBody = new FormBody.Builder().
                add(Constant.TINYIP, tinyip).
                add(Constant.SID, sid).build();
        Request request = new Request.Builder()
                .url(Constant.formatBASE_HOST(serverIP) + "/axis2/services/STPdaService2/GetLabStatus2")
                .post(formBody)
                .build();
        new Thread(() -> {
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String xml = response.body().string();
                    xml = xml.replace("<ns:GetLabStatus2Response xmlns:ns=\"http://services.suntown.com\"><ns:return>","");
                    Log.i(TAG,"xml:"+xml);
                    xml = xml.replace("</ns:return></ns:GetLabStatus2Response>","");
                    xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&#xd;", "");
                    Log.i(TAG,"xml:"+xml);
                    try {
                        ShopXmlBean shopXmlBean = new Xml2Json(xml).pullXml2Bean();
                        String sfid = shopXmlBean.SFID;
                        runOnUiThread(() -> {
                            if (i == 1) {
                                Log.i(TAG,"shelfGoods："+shelfGoods);
                                if (!shelfGoods.equals(sfid)&&null!=sfid) {
                                    dialog(shelfGoods,tinyip,i);
                                } else {
                                    Intent resultIntent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constant.RESULT_CODE, tinyip);
                                    resultIntent.putExtras(bundle);
                                    setResult(RESULT_OK, resultIntent);
                                    CreamaActivity.this.finish();
                                }
                            } else if(i == 2){
                                if (!shelfGoods.equals(sfid)&&null!=sfid) {
                                    dialog(shelfGoods,tinyip,i);
                                } else {
                                    addData(tinyip);
                                    Utils.showToast(CreamaActivity.this,"扫描成功，继续扫描");
//                                    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//                                    SurfaceHolder surfaceHolder = surfaceView.getHolder();
//                                    initCamera(surfaceHolder);
                                    handler.postDelayed(() -> {
                                        if (handler != null) {
                                            handler.restartPreviewAndDecode();
                                        }
                                    },2000);
                                }
                            }
                        });
//                        Log.i(TAG,"shopXmlBean:"+shopXmlBean);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    Utils.showToast(CreamaActivity.this,"网络异常，请重试");
                }
            });
        }).start();

    }

    //TODO 返回键按下时带回数据
    @Override
    public void onBackPressed() {
        Log.i(TAG, scannerList.toString());
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra(Constant.RESULT_CODE, scannerList);
        setResult(200, resultIntent);
        CreamaActivity.this.finish();
        super.onBackPressed();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = mediaPlayer1 -> mediaPlayer1.seekTo(0);

    protected void dialog(String shelfGoods,String tinyip,int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("该标签已存在其他货架，是否替换?");
//        builder.setTitle("提示");
        builder.setPositiveButton("确认", (dialog, which) -> {
            dialog.dismiss();
            if (i==1){
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.RESULT_CODE,resultString);
                resultIntent.putExtras(bundle);
                this.setResult(RESULT_OK, resultIntent);
                CreamaActivity.this.finish();
            }else if (i==2){
                addData(tinyip);
                handler.postDelayed(() -> {
                    if (handler != null) {
                        handler.restartPreviewAndDecode();
                    }
                },2000);
            }

        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            dialog.dismiss();
            handler.postDelayed(() -> {
                if (handler != null) {
                    handler.restartPreviewAndDecode();
                }
            },2000);
        });
        builder.create().show();
    }


}
