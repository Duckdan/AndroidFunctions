package study.com.androidfunctions.suspend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.com.androidfunctions.R;
import study.com.androidfunctions.adapter.LvSuspendAdapter;
import study.com.androidfunctions.model.ListHeaderEntity;
import study.com.androidfunctions.model.PersonInfo;

/**
 * ListView版的顶部悬停
 */
public class LvSuspendActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_name)
    TextView tvName;

    String TAG = "lv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv_suspend);
        ButterKnife.bind(this);
        initAdapter();
    }

    private void initAdapter() {
        List<ListHeaderEntity<PersonInfo>> list = new ArrayList<>();
        generateData(list);
        final LvSuspendAdapter<ListHeaderEntity<PersonInfo>> adapter = new LvSuspendAdapter<>(this, list);
        lv.setAdapter(adapter);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //获取可视布局的第二个条目
                View secondView = lv.getChildAt(1);
                if (secondView == null) {
                    return;
                }

                //获取第二个条目距离ListView顶部的距离
                int top = secondView.getTop();

                //tvName的高
                int height = tvName.getHeight();

                //获取第一个可视条目数据的“头值”
                String firstHeaderName = adapter.getItem(firstVisibleItem).getListHeaderName();
                //获取第二个可视条目数据的“头值”
                String secondHeaderName = adapter.getItem(firstVisibleItem + 1).getListHeaderName();

                if (top <= height) {
                    if (!firstHeaderName.equals(secondHeaderName)) {
                        tvName.setText(firstHeaderName);
                        tvName.setTranslationY(top - height);
                    } else {
                        tvName.setText(secondHeaderName);
                        tvName.setTranslationY(0);
                    }
                }
                Log.e("index", "top::" + top);
            }
        });
    }

    /**
     * 创建条目填充数据
     *
     * @param list
     */
    private void generateData(List<ListHeaderEntity<PersonInfo>> list) {
        PersonInfo infoA = new PersonInfo("A");
        list.add(new ListHeaderEntity<PersonInfo>(infoA, LvSuspendAdapter.TYPE_HEADER, infoA.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item A" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, LvSuspendAdapter.TYPE_DATA, infoA.getName()));
        }

        PersonInfo infoB = new PersonInfo("B");
        list.add(new ListHeaderEntity<PersonInfo>(infoB, LvSuspendAdapter.TYPE_HEADER, infoB.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item B" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, LvSuspendAdapter.TYPE_DATA, infoB.getName()));
        }

        PersonInfo infoC = new PersonInfo("C");
        list.add(new ListHeaderEntity<PersonInfo>(infoC, LvSuspendAdapter.TYPE_HEADER, infoC.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item C" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, LvSuspendAdapter.TYPE_DATA, infoC.getName()));
        }

        PersonInfo infoD = new PersonInfo("D");
        list.add(new ListHeaderEntity<PersonInfo>(infoD, LvSuspendAdapter.TYPE_HEADER, infoD.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item D" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, LvSuspendAdapter.TYPE_DATA, infoD.getName()));
        }

        PersonInfo infoE = new PersonInfo("E");
        list.add(new ListHeaderEntity<PersonInfo>(infoE, LvSuspendAdapter.TYPE_HEADER, infoE.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item E" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, LvSuspendAdapter.TYPE_DATA, infoE.getName()));
        }
    }
}
