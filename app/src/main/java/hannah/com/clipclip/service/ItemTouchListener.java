package hannah.com.clipclip.service;

import android.view.View;

import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.adapter.ClipboardListHolder;

/**
 * Created by hannah on 2019. 2. 19..
 */
public interface ItemTouchListener {
    void onItemClick(View v, ClipboardItem vo);
    void onStartDrag(View v, ClipboardListHolder holder);
}
