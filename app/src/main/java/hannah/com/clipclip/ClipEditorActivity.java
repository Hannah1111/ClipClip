package hannah.com.clipclip;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hannah.com.clipclip.instance.RealmHelper;

public class ClipEditorActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.btn_back)
    ImageButton btn_back;
    @BindView(R.id.btn_setting)
    ImageButton btn_setting;

    @BindView(R.id.edit_title)
    EditText edit_title;
    @BindView(R.id.edit_content)
    EditText edit_content;
    @BindView(R.id.tv_date)
    TextView tv_date;

    int mode = 0;
    int contectId = 0;
    @Override
    protected void onCreate(@Nullable Bundle intent) {
        super.onCreate(intent);
        setContentView(R.layout.activity_clipeditor);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        btn_back.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        if(bundle !=null){
            mode = bundle.getInt("mode");

            if(mode == Con.EDIT_MODE){
                ClipboardItem clipboardItem = bundle.getParcelable("clipdata");
                clipboardItem.all();
                contectId = clipboardItem.getId();

                tv_title.setText("수정하기");
                edit_title.setText(clipboardItem.getTitle());
                edit_content.setText(clipboardItem.getCliptext());
                tv_date.setText(clipboardItem.getDate());
            }else{
                tv_title.setText("새로쓰기");
                contectId = RealmHelper.getInstance(this).getNextKey();
                tv_date.setText(Con.getCurrentTime());
            }
        }

    }

    @Override
    public void finish() {
        overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_setting:
                RealmHelper realmHelper = RealmHelper.getInstance(this);

                String title = "";
                String contents  = "";
                if(edit_title.length() > 0){
                    title = edit_title.getText().toString();
                }

                if(edit_content.length() > 0){
                    contents = edit_content.getText().toString();
                }

                if(mode == Con.NEW_MODE){
                    ClipboardItem clipboardItem = new ClipboardItem(contectId,
                            title,contents,Con.getCurrentTime(),Con.TRUE);
                    realmHelper.insertData(clipboardItem);
                }else{
                    ClipboardItem clipboardItem = new ClipboardItem(contectId,
                            title,contents,Con.getCurrentTime(),Con.FALSE);
                    realmHelper.updateData(clipboardItem);
                }
                break;
        }
    }
}
