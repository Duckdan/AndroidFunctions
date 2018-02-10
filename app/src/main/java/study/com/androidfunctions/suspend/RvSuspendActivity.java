package study.com.androidfunctions.suspend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.oushangfeng.pinnedsectionitemdecoration.callback.OnHeaderClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.com.androidfunctions.R;
import study.com.androidfunctions.adapter.RvHeaderAdapter;
import study.com.androidfunctions.model.ListHeaderEntity;
import study.com.androidfunctions.model.PersonInfo;

/**
 * RecyclerView的顶部悬停
 */
public class RvSuspendActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    RvHeaderAdapter<ListHeaderEntity<PersonInfo>> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_suspend);
        ButterKnife.bind(this);


        initRecyclerView();
        initAdapter();

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PinnedHeaderItemDecoration phid = new PinnedHeaderItemDecoration.
                Builder(RvHeaderAdapter.TYPE_HEADER).
                setDividerId(R.drawable.divider_item_shape).enableDivider(true)
                .setHeaderClickListener(headerClickListener).create();
        recyclerView.addItemDecoration(phid);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (adapter.getItemViewType(i)) {
                    case RvHeaderAdapter.TYPE_DATA:
                        ListHeaderEntity<PersonInfo> entity = adapter.getData().get(i);
                        Toast.makeText(RvSuspendActivity.this, entity.getListHeaderName() + ", position " + i + ", id " + entity.getData().getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {
        @Override
        public void onHeaderClick(View view, int id, int position) {
            Toast.makeText(RvSuspendActivity.this, "click, tag: " + adapter.getData().get(position).getListHeaderName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onHeaderLongClick(View view, int id, int position) {
            Toast.makeText(RvSuspendActivity.this, "long click, tag: " + adapter.getData().get(position).getListHeaderName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onHeaderDoubleClick(View view, int id, int position) {
            Toast.makeText(RvSuspendActivity.this, "double click, tag: " + adapter.getData().get(position).getListHeaderName(), Toast.LENGTH_SHORT).show();
        }
    };

    private void initAdapter() {
        List<ListHeaderEntity<PersonInfo>> list = new ArrayList<>();
        generateData(list);

        adapter = new RvHeaderAdapter<ListHeaderEntity<PersonInfo>>(list) {

            @Override
            protected void addItemTypes() {

                addItemType(TYPE_HEADER, R.layout.list_data_header);
                addItemType(TYPE_DATA, R.layout.list_data_item);
            }

            @Override
            protected void convert(BaseViewHolder helper, ListHeaderEntity<PersonInfo> item) {
                switch (item.getItemType()) {
                    case TYPE_HEADER:
                        helper.setText(R.id.tv_name, item.getListHeaderName());
                        break;
                    case TYPE_DATA:
                        helper.setText(R.id.tv_name, item.getData().getName());
                        break;
                }
            }

        };



        recyclerView.setAdapter(adapter);
    }

    /**
     * 创建条目填充数据
     *
     * @param list
     */
    private void generateData(List<ListHeaderEntity<PersonInfo>> list) {
        PersonInfo infoA = new PersonInfo("A");
        list.add(new ListHeaderEntity<PersonInfo>(infoA, RvHeaderAdapter.TYPE_HEADER, infoA.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item A" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, RvHeaderAdapter.TYPE_DATA, infoA.getName()));
        }

        PersonInfo infoB = new PersonInfo("B");
        list.add(new ListHeaderEntity<PersonInfo>(infoB, RvHeaderAdapter.TYPE_HEADER, infoB.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item B" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, RvHeaderAdapter.TYPE_DATA, infoB.getName()));
        }

        PersonInfo infoC = new PersonInfo("C");
        list.add(new ListHeaderEntity<PersonInfo>(infoC,RvHeaderAdapter.TYPE_HEADER, infoC.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item C" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, RvHeaderAdapter.TYPE_DATA, infoC.getName()));
        }

        PersonInfo infoD = new PersonInfo("D");
        list.add(new ListHeaderEntity<PersonInfo>(infoD, RvHeaderAdapter.TYPE_HEADER, infoD.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item D" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj, RvHeaderAdapter.TYPE_DATA, infoD.getName()));
        }

        PersonInfo infoE = new PersonInfo("E");
        list.add(new ListHeaderEntity<PersonInfo>(infoE, RvHeaderAdapter.TYPE_HEADER, infoE.getName()));
        for (int i = 1; i < 6; i++) {
            PersonInfo obj = new PersonInfo("Item E" + i);
            list.add(new ListHeaderEntity<PersonInfo>(obj,RvHeaderAdapter.TYPE_DATA, infoE.getName()));
        }
    }
}
