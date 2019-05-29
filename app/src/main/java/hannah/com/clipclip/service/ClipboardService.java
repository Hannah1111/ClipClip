package hannah.com.clipclip.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.Con;
import hannah.com.clipclip.MainActivity;
import hannah.com.clipclip.R;
import hannah.com.clipclip.aLog;
import hannah.com.clipclip.instance.RealmHelper;

/**
 * Created by hannah on 2019. 5. 22..
 */
public class ClipboardService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {


    ClipboardManager manager;
    static String CHANNEL_PARAM = "CLIPBOARDSERVICE";
    //사용자에게 보여지는 이름
    static String CHANNEL_P_NAME = "클립보드 자동 저장";

    @Override
    public void onCreate() {
        super.onCreate();

        aLog.e("service oncreate");
        manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager.addPrimaryClipChangedListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        aLog.e("service onStartCommand");
        aLog.e("[onStartCommand]"+intent.getAction()+" : "+flags);

        if(intent != null && intent.getAction() !=null && intent.getAction().equals("STOP_SERVICE")){
            stopSelf();
        }else{
            startForegroundService(this);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(manager!=null){
            manager.removePrimaryClipChangedListener(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) new UnsupportedOperationException("NOT IMPLEMENT");
    }

    @Override
    public void onPrimaryClipChanged() {

        RealmHelper helper = RealmHelper.getInstance(this);
        aLog.e("-- onPrimaryClipChanged "+manager + " : "+helper);
        if(helper!=null && manager!=null && manager.getPrimaryClip() !=null){
            ClipData data = manager.getPrimaryClip();

            int dataCount = data.getItemCount();
            if(dataCount> 0){
                data.getItemAt(0).coerceToText(this);
                aLog.e(String.valueOf(data.getItemAt(0).coerceToText(this)));

                helper.insertData(new ClipboardItem(helper.getNextKey(),"",String.valueOf(data.getItemAt(0).coerceToText(this)),
                        Con.getCurrentTime(), Con.FALSE));
            }
        }else{
            aLog.e("-- manager is null "+manager + " : "+helper);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    public void startForegroundService(Context context) {
        aLog.e("[startForegroundService]"+context);
        NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder mBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(!checkNotificationChannel(context,CHANNEL_PARAM)){
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_PARAM,
                        CHANNEL_P_NAME, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("클립보드 사용중");
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // 락화면에서 알림을 어느정도 보여줄지 설정

                notiManager.createNotificationChannel(notificationChannel);
            }
            mBuilder = new Notification.Builder(context,CHANNEL_PARAM);
        }else{
            mBuilder = new Notification.Builder(context);
        }

        Intent toLaunch = new Intent(context, MainActivity.class);
        toLaunch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intentgoApp = PendingIntent.getActivity(context, 0, toLaunch, 0);

        Intent stopIntent = new Intent(context,ClipboardService.class);
        stopIntent.setAction("STOP_SERVICE").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intentstop = PendingIntent.getService(context, 0, stopIntent, 0);

        RemoteViews customNoti = new RemoteViews(getPackageName(), R.layout.noti);
        customNoti.setOnClickPendingIntent(R.id.ll_service_stop,intentstop);
        customNoti.setOnClickPendingIntent(R.id.ll_app_start,intentgoApp);

        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("앱으로 바로가기")
                .setOnlyAlertOnce(true)
                .setCustomContentView(customNoti);
//                .setContentIntent(intentBack); //버튼 누르면 동작.

        startForeground(1, mBuilder.build());
    }

    //notification channel 등록 체크
    @TargetApi(Build.VERSION_CODES.O)
    public static boolean checkNotificationChannel(Context ctx, String channelId ) {
        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager.getNotificationChannel(channelId) != null ? true : false;
    }


}
