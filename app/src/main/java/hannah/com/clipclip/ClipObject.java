package hannah.com.clipclip;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClipObject extends RealmObject {

    @PrimaryKey
    private int key;

    private int id;
    private String title;
    private String cliptext;
    private String date;
    private int update;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUpdate(){
        return update;
    }
    public void setUpdate(int update){
        this.update = update;
    }
}
