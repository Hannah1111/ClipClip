package hannah.com.clipclip.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import hannah.com.clipclip.ClipEditorActivity;
import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.Con;
import hannah.com.clipclip.R;
import hannah.com.clipclip.aLog;
import hannah.com.clipclip.instance.RealmHelper;
import hannah.com.clipclip.service.ItemTouchHelperCallback;
import hannah.com.clipclip.service.ItemTouchListener;

public class ClipboardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperCallback.OnItemMoveListener  {


    ArrayList<ClipboardItem> data = new ArrayList<>();
    Context context;
    private ItemTouchListener mListener;

    public void setData(Context mCtx, ArrayList<ClipboardItem> item, ItemTouchListener listener){
        context = mCtx;
        mListener = listener;
        if(item != null){
            data.clear();
            data.addAll(item);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClipboardListHolder(context, LayoutInflater.from(context).inflate(R.layout.cell_clipboardlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ClipboardListHolder main = (ClipboardListHolder) holder;
        ClipboardItem item = (ClipboardItem)data.get(position);
        main.onBinding(item, mListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < 0 || fromPosition >= data.size() || toPosition < 0 || toPosition >= data.size()){
            return;
        }
        if(data!=null && data.get(toPosition)!=null){
            ClipboardItem vo = (ClipboardItem) data.get(fromPosition);
            Toast.makeText(context,vo.getId() + " 연결 순서가 변경 되었습니다.",Toast.LENGTH_SHORT).show();
            Log.e("----onItemMove", "position changed "+fromPosition+ " to "+toPosition );
        }else{
            Log.e("----onItemMove", "position changed data error "+ data);
        }
        Collections.swap(data,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        Log.e("----onItemSwipe", "direction "+ direction);
        if(direction == ItemTouchHelper.LEFT){
            if(data!=null && data.get(position)!=null){
                ClipboardItem vo = (ClipboardItem) data.get(position);
                Toast.makeText(context,vo.getId()+ " 삭제됩니다.",Toast.LENGTH_SHORT).show();
                Log.e("----onItemDismiss", "position delete " + vo.getId());
                RealmHelper.getInstance(context).deleteItem(vo);
            }else{
                Log.e("----onItemDismiss", "position delete data error "+ data);
            }
            data.remove(position);
            notifyItemRemoved(position);
        }else{
            //수정
            ClipboardItem vo = (ClipboardItem) data.get(position);
            aLog.e("==onItemClick2 ="+vo.getId());
            Intent intent = new Intent(context, ClipEditorActivity.class);
            intent.putExtra("mode", Con.EDIT_MODE);
            intent.putExtra("clipdata",vo);
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }
}
