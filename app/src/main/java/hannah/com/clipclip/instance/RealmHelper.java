package hannah.com.clipclip.instance;

import android.content.Context;
import android.widget.Toast;

import hannah.com.clipclip.ClipObject;
import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.Con;
import hannah.com.clipclip.aLog;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Initalization on demand holder idiom (holder에 의한 초기화 방식)
 */
public class RealmHelper {

    private RealmConfiguration config;
    private static Context context;
    private Realm realm;

    //다른클래스에서 생성할 수 없도록 한다
    private RealmHelper(Context context){
        Realm.init(context);
        config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(Con.DB_NAME).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    // RealmHelperholder는 getinstance 할때 클래스가 처음 로드된다.
    // INSTANCE는 이 때 처음 Acceess 된다
    private static class RealmHelperholder{
        public static final RealmHelper INSTANCE = new RealmHelper(context);
    }

    public static RealmHelper getInstance(Context ctx){
        context = ctx;
        return RealmHelperholder.INSTANCE;
    }

    public Realm getDB(){
        return realm;
    }

    public RealmResults<ClipObject> selectAllData(){
        RealmResults<ClipObject> all = realm.where(ClipObject.class).findAll();
        return all;
    }

    public boolean isDataImmadiate(ClipboardItem item){
        ClipObject ci = realm.where(ClipObject.class)
                .equalTo("cliptext",item.getCliptext()).equalTo("title",item.getTitle()).findFirst();
        if(ci!=null){
            return true;
        }else{
            return false;
        }
    }


    public int getNextKey(){
        Number maxId = realm.where(ClipObject.class).max("key");
        int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
        return nextId;
    }

    public void insertData(ClipboardItem data){
        if(!isDataImmadiate(data)){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ClipObject ci = realm.createObject(ClipObject.class,data.getId());
                    aLog.e("==== insert nextId !! "+data.getId());
                    ci.setId(data.getId());
                    ci.setCliptext(data.getCliptext());
                    ci.setTitle(data.getTitle());
                    ci.setDate(data.getDate());
                    ci.setUpdate(Con.FALSE);
                    realm.insert(ci);
                    Toast.makeText(context, "생성되었습니다. "+data.getId(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void updateData(ClipboardItem data){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                ClipObject ci = realm.where(ClipObject.class).equalTo("id", data.getId()).findFirst();
                aLog.e("==== updateData nextId !! "+data.getId()+ " :"+ci);
                if(ci != null){
                    ci.setTitle(data.getTitle());
                    ci.setCliptext(data.getCliptext());
                    ci.setDate(Con.getCurrentTime());
                    ci.setUpdate(Con.TRUE);
                    Toast.makeText(context, "수정되었습니다. "+data.getId(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteall(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               RealmResults<ClipObject> all =  realm.where(ClipObject.class).findAll();
               all.deleteAllFromRealm();
            }
        });
    }

    public void deleteItem(ClipboardItem item){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ClipObject ci = realm.where(ClipObject.class).equalTo("id",item.getId()).findFirst();
                if(ci.isValid()){
                    aLog.e("===delete "+ci.getId());
                    ci.deleteFromRealm();
                }
            }
        });
    }
}
