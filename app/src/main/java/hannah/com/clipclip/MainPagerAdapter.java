package hannah.com.clipclip;

import hannah.com.clipclip.fragment.ClipboardFragment;
import hannah.com.clipclip.instance.RealmHelper;
import io.realm.RealmResults;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by hannah on 2019. 5. 22..
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<FragmentItem> frags=new ArrayList<>();
    private Context context=null;


    class FragmentItem{
        private Fragment frag;
        private String name;

        FragmentItem(Fragment mFrag, String title){
            frag = mFrag;
            name = title;
        }

        Fragment getFrag(){
            return frag;
        }

        String getName(){
            return name;
        }
    }

    MainPagerAdapter(Context context, FragmentManager fm , ArrayList<ClipboardItem> immData) {
        super(fm);
        this.context = context;
        RealmResults<ClipObject> allData = RealmHelper.getInstance(context).selectAllData();
        if(allData.size() > 0){
            for(int i =0; i < allData.size() ; i++ ){
                ClipObject item = allData.get(i);
                immData.add(new ClipboardItem(item.getId(), item.getTitle(),item.getCliptext(),item.getDate(),item.getUpdate()));
            }
        }

        Bundle args = new Bundle();
        args.putParcelableArrayList("clipboradlist", immData);
        Fragment fragment = ClipboardFragment.setInstance("1번째");
        fragment.setArguments(args);
        frags.add(new FragmentItem(fragment,Con.TAB1));
        frags.add(new FragmentItem(ClipboardFragment.setInstance("2번째"),Con.TAB2));
        frags.add(new FragmentItem(ClipboardFragment.setInstance("3번째"),Con.TAB3));
    }

    void setContext(Context context){
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {
        return frags.get(i).getFrag();
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return frags.get(position).getName();
    }

    public ArrayList<FragmentItem> getAdapterItem(){
        return frags;
    }


}
