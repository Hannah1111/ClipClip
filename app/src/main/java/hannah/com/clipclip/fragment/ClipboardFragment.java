package hannah.com.clipclip.fragment;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import hannah.com.clipclip.ClipEditorActivity;
import hannah.com.clipclip.ClipboardItem;
import hannah.com.clipclip.Con;
import hannah.com.clipclip.R;
import hannah.com.clipclip.aLog;
import hannah.com.clipclip.adapter.ClipboardListAdapter;
import hannah.com.clipclip.adapter.ClipboardListHolder;
import hannah.com.clipclip.service.ItemTouchHelperCallback;
import hannah.com.clipclip.service.ItemTouchListener;

/**
 * Created by hannah on 2019. 5. 22..
 */
public class ClipboardFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    ArrayList<ClipboardItem> data = new ArrayList<>();
    ClipboardListAdapter adapter;
    ItemTouchHelper mItemTouchHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.bind(this,RootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClipboardListAdapter();

        if(getArguments()!=null){
            data = getArguments().getParcelableArrayList("clipboradlist");
            adapter.setData(getContext(),data, mRecylerItemListener);
            recyclerView.setAdapter(adapter);
        }

        //아이템 드래그앤드랍 리스너 적용
        ItemTouchHelperCallback mItemTouchHelperCallback = new ItemTouchHelperCallback(adapter,getContext());
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        return RootView;
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public static Fragment setInstance(String title){
        ClipboardFragment clipboardFragment = new ClipboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        clipboardFragment.setArguments(bundle);
        return clipboardFragment;
    }
    @Override

    public void onDestroyView() {
        super.onDestroyView();
    }

    public ItemTouchListener mRecylerItemListener = new ItemTouchListener() {
        @Override
        public void onItemClick(View v, ClipboardItem vo) {

            Intent intent = new Intent(getContext(), ClipEditorActivity.class);
            aLog.e("==onItemClick ="+vo.getId());
            vo.all();
            intent.putExtra("mode", Con.EDIT_MODE);
            intent.putExtra("clipdata",vo);
            getContext().startActivity(intent);
            ((Activity)getContext()).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
//            adapter.notifyDataSetChanged();
        }

        @Override
        public void onStartDrag(View v, ClipboardListHolder holder) {
            mItemTouchHelper.startDrag(holder);
        }
    };
}
