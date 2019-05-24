package hannah.com.clipclip.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.Con;
import hannah.com.clipclip.aLog;
import hannah.com.clipclip.instance.RealmHelper;

/**
 * Created by hannah on 2019. 5. 22..
 */
public class ClipboardService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {

    ClipboardManager manager;

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
        return super.onStartCommand(intent, flags, startId);
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
}
