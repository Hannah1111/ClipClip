package hannah.com.clipclip;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import hannah.com.clipclip.instance.RealmHelper;
import hannah.com.clipclip.service.ClipboardService;

public class MainActivity extends AppCompatActivity {
    private int REQ_CODE_OVERLAY_PERMISSION = 0;
    MainPagerAdapter adapter;

    @BindView(R.id.tablayout) TabLayout mTab;
    @BindView(R.id.viewPager) ViewPager viewpager;

    @BindView(R.id.btn_floating_action)
    FloatingActionButton btn_floating_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkPermission();
        setData();

        mTab.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTab));
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btn_floating_action.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClipEditorActivity.class);
            intent.putExtra("mode", Con.NEW_MODE);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        });
    }

    private void setData(){
        ArrayList<ClipboardItem> immiData = getLocalClipboard();
        MainPagerAdapter adapter=new MainPagerAdapter(this, getSupportFragmentManager(),immiData);
        adapter.setContext(this);
        viewpager.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        setData();
        super.onResume();
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @NonNull
    public void createIntentToRequestOverlayPermission(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        aLog.e("ACTIVITY RESULT "+requestCode + " : "+resultCode);
        startService(new Intent(this, ClipboardService.class));
//        tvStatus.setText("서비스 상태 : 실행중");
    }

    private void checkPermission(){
        if(!Settings.canDrawOverlays(this)){
            createIntentToRequestOverlayPermission(this);
        }else{
            Toast.makeText(this,"권한있음",Toast.LENGTH_SHORT).show();

        }
    }

    private ArrayList<ClipboardItem> getLocalClipboard(){
        ArrayList<ClipboardItem> data = new ArrayList<>();
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if(manager.hasPrimaryClip()){
            ClipData clipData =  manager.getPrimaryClip();
            assert clipData != null;

            RealmHelper helper = RealmHelper.getInstance(this);

            aLog.e("getLocalClipboard getItemCount " +clipData.getItemCount());
            if(clipData.getItemCount() > 0){
                ClipboardItem clipboardItem = new ClipboardItem(helper.getNextKey(),"",
                        String.valueOf(clipData.getItemAt(0).getText()),Con.getCurrentTime(), Con.FALSE);
                if(!helper.isDataImmadiate(clipboardItem)){
                    helper.insertData(clipboardItem);
                    aLog.e("==== insert !! "+clipboardItem.getId());
                    data.add(clipboardItem);
                }
            }
        }
        return data;
    }

    @Override
    public void finish(){
//        stopService(new Intent(this, ClipboardService.class));
        super.finish();
    }
}
