package hannah.com.clipclip.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.Con;
import hannah.com.clipclip.R;
import hannah.com.clipclip.aLog;
import hannah.com.clipclip.service.ItemTouchListener;

public class ClipboardListHolder extends RecyclerView.ViewHolder {

    Context context;
    ItemTouchListener mListener ;
    @BindView(R.id.btn_modify)
    ImageButton btn_modify;
    @BindView(R.id.btn_share)
    ImageButton btn_share;
    @BindView(R.id.tv_clipboard_title)
    TextView tv_clipboard_title;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_modify_date)
    TextView tv_modify_date;

    public ClipboardListHolder(Context mContext, @NonNull View itemView) {
        super(itemView);
        context = mContext;
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onBinding(ClipboardItem item, ItemTouchListener listener){

        mListener = listener;

        if(item.getTitle()!=null && !item.getTitle().isEmpty()){
            tv_clipboard_title.setText(item.getTitle());
        }else{
            tv_clipboard_title.setText(item.getCliptext());
        }

        if(item.getIsUpdate() == Con.TRUE){
            tv_state.setText("수정됨");
            tv_state.setVisibility(View.VISIBLE);
        }else{
            tv_state.setVisibility(View.INVISIBLE);
        }

        if(item.getDate()!=null){
            tv_modify_date.setText(item.getDate());
        }

        itemView.setOnTouchListener((v,event)->{

            if(MotionEventCompat.getActionMasked(event) != MotionEvent.ACTION_OUTSIDE ){
                mListener.onStartDrag(v, this);
            }
            return false;
        });

        itemView.setOnClickListener(v -> {
            aLog.e("setOnClickListener "+item.getId());
            mListener.onItemClick(v,item);
        });
    }
}
