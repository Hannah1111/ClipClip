package hannah.com.clipclip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 상수 모음
 */
public class Con {

    public static String TAB1 = "클립보드";
    public static String TAB2 = "날씨";
    public static String TAB3 = "할일";

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static final int EDIT_MODE = 1;
    public static final int NEW_MODE = 2;

    public static String DB_NAME = "clip_clip.realm";

    public static String SHARE_KEY = "clip.pref";

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        Date date = new Date();

        return dateFormat.format(date);
    }

}
