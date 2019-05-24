package hannah.com.clipclip;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClipboardItem implements Parcelable {
    private int id;
    private String title;
    private String cliptext;
    private String date;
    private int isUpdate;

    public ClipboardItem(int id, String title,String cliptext, String date, int update){
        this.id = id;
        this.title = title;
        this.cliptext = cliptext;
        this.date = date;
        isUpdate = update;
        aLog.e("title "+title + " : "+cliptext+" : "+date+ " : "+id + " : "+isUpdate);
    }

    private ClipboardItem(Parcel in) {
        id  = in.readInt();
        title = in.readString();
        cliptext = in.readString();
        date = in.readString();
        isUpdate = in.readInt();
    }

    public static final Creator<ClipboardItem> CREATOR = new Creator<ClipboardItem>() {
        @Override
        public ClipboardItem createFromParcel(Parcel in) {
            return new ClipboardItem(in);
        }

        @Override
        public ClipboardItem[] newArray(int size) {
            return new ClipboardItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(cliptext);
        dest.writeString(date);
        dest.writeInt(isUpdate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        aLog.e("====setId "+id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCliptext() {
        return cliptext;
    }

    public void setCliptext(String cliptext) {
        this.cliptext = cliptext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }


    public void all(){
        aLog.e("ALLSTRING: "+title + " : "+cliptext+" : "+date+ " : "+id + " : "+isUpdate);
    }
}
