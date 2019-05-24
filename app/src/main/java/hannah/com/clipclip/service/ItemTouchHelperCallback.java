package hannah.com.clipclip.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import hannah.com.clipclip.R;
import hannah.com.clipclip.aLog;

/**
 * Created by hannah on 2019. 2. 7..
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    // 길게 누를시 이동여부
    private boolean isLongPressDragEnabled = false;

    // 이동여부
    private boolean isItemViewSwipeEnabled = true;

    // 삭제여부
    private boolean isItemDismiss = true;


    //어느 방향으로 움직일지 정하는 플래그
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        if(!isLongPressDragEnabled)
            dragFlags = 0;

        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT ;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    public void setIsLongPressDragEnabled(boolean b) {
        isLongPressDragEnabled = b;
    }

    public void setIsItemViewSwipeEnabled(boolean b) {
        isItemViewSwipeEnabled = b;
    }

    public void setIsItemDismiss(boolean b) {
        isItemDismiss = b;
    }

    public boolean isItemDismiss() {
        return isItemDismiss;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemViewSwipeEnabled;
    }

    public interface OnItemMoveListener{
        void onItemMove(int fromPosition, int toPosition);
        void onItemSwipe(int position, int direction);
    }

    private Context mCtx;
    private final OnItemMoveListener mItemMoveListener;
    public ItemTouchHelperCallback(OnItemMoveListener listener, Context context){
        mItemMoveListener = listener;
        mCtx = context;
    }

    //위치 이동
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return isItemViewSwipeEnabled;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemMoveListener.onItemSwipe(viewHolder.getAdapterPosition(), direction);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Bitmap icon;
        Paint p = new Paint();
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if(dX > 0){
                p.setColor(Color.parseColor("#bbede5"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.edit);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            } else {
                p.setColor(Color.parseColor("#d8c7d8"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.delete);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}
