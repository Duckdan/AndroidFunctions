package study.com.androidfunctions.contact;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.com.androidfunctions.DataResource;
import study.com.androidfunctions.R;
import study.com.androidfunctions.adapter.RvHeaderAdapter;
import study.com.androidfunctions.model.ListHeaderEntity;
import study.com.androidfunctions.model.PersonInfo;
import study.com.androidfunctions.utils.PinyinUtils;
import study.com.androidfunctions.weight.IndicatorView;


/**
 * 第一版联系人列表
 */
public class FirstContactActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.iv)
    IndicatorView iv;
    RvHeaderAdapter<ListHeaderEntity<PersonInfo>> adapter;
    ArrayList<PersonInfo> list = new ArrayList<>();

    List<ListHeaderEntity<PersonInfo>> adapterList = new ArrayList<>();
    private View searchView;
    private PinnedHeaderItemDecoration phid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_contact);
        ButterKnife.bind(this);
        generateData(list);
        adapterList = PinyinUtils.getContactsList(list);

        initRecyclerView();
        initAdapter();
        initEvent();
    }

    private void generateData(ArrayList<PersonInfo> list) {
        List<String> contactLists = Arrays.asList(DataResource.contacts);
        list.clear();
        for (int i = 0; i < contactLists.size(); i++) {
            PersonInfo personInfo = new PersonInfo(contactLists.get(i));
            list.add(personInfo);
        }
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        phid = new PinnedHeaderItemDecoration.
                Builder(RvHeaderAdapter.TYPE_HEADER).
                setDividerId(R.drawable.divider_item_shape).enableDivider(true).create();
        phid.getPinnedHeaderPosition();

        recyclerView.addItemDecoration(phid);


        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ListHeaderEntity<PersonInfo> item = adapter.getItem(i);
                switch (item.getItemType()) {
                    case RvHeaderAdapter.TYPE_DATA:
                        ListHeaderEntity<PersonInfo> entity = adapter.getData().get(i);
                        Toast.makeText(FirstContactActivity.this, entity.getListHeaderName() + ", position " + i + ", id " + entity.getData().getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case RvHeaderAdapter.TYPE_HEADER:
                        ListHeaderEntity<PersonInfo> headerEntity = adapter.getData().get(i);
                        Toast.makeText(FirstContactActivity.this, "标题：" + headerEntity.getListHeaderName() + ",position:" + i, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    private void initAdapter() {

        adapter = new RvHeaderAdapter<ListHeaderEntity<PersonInfo>>(adapterList) {

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

        View headerView = LayoutInflater.from(this).inflate(R.layout.list_contacts_header, null);
        searchView = headerView.findViewById(R.id.rl_search);
        View verifyView = headerView.findViewById(R.id.ll_verify);
        View groupView = headerView.findViewById(R.id.ll_group);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstContactActivity.this, "搜索", Toast.LENGTH_SHORT).show();

            }
        });

        verifyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstContactActivity.this, "验证", Toast.LENGTH_SHORT).show();
            }
        });

        groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstContactActivity.this, "群聊", Toast.LENGTH_SHORT).show();
            }
        });


        adapter.addHeaderView(headerView);
        //添加尾部布局
        View inflate = View.inflate(this, R.layout.list_data_item, null);
        inflate.setBackgroundColor(Color.GRAY);
        TextView tvName = (TextView) inflate.findViewById(R.id.tv_name);
        tvName.setText(list.size() + "位联系人");
        adapter.addFooterView(inflate);
        recyclerView.setAdapter(adapter);
    }


    private void initEvent() {
        iv.setOnItemClickListener(new IndicatorView.OnItemClickListener() {
            @Override
            public void onItemClick(String letter) {
                Map<String, Integer> keyMap = PinyinUtils.keyMap;
                Integer integer = keyMap.get(letter);

                if (integer != null) {

                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int height = manager.getChildAt(1).getHeight();

                    if (integer == 0) {
                        LinearLayout layout = adapter.getHeaderLayout();
                        manager.scrollToPositionWithOffset(integer, -layout.getHeight());
                        Log.e("index", layout.getHeight() + "===");
                    } else {
                        manager.scrollToPositionWithOffset(integer, -height - 50);
                        //实现悬浮窗的第二种方法===================================
//                        mIndex = integer+1;
//                        method2(integer, manager);
                    }


                }
            }
        });


    }

    //实现悬浮窗的第二种方法===================================

    private boolean move = false;
    private int mIndex = 0;

    private void method2(Integer integer, LinearLayoutManager manager) {
        recyclerView.stopScroll();

        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        Log.e("index", firstItem + "===" + integer + "===" + lastItem);
        //由于布局管理者来不及刷新可视条目的位置所以加此判断来防止获取到的指定位置的数据或控件无效
        if (integer <= firstItem) {
            recyclerView.scrollToPosition(integer + 1);
        } else if (integer <= lastItem) {
            int top = recyclerView.getChildAt(integer - firstItem + 1).getTop();
            Log.e("index", "top::" + top);
            recyclerView.scrollBy(0, top);
        } else {

            recyclerView.scrollToPosition(integer + 1);
            move = true;

        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (move) {
                    move = false;
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int n = mIndex - manager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerView.getChildCount()) {
                        int top = recyclerView.getChildAt(n).getTop();
                        FirstContactActivity.this.recyclerView.scrollBy(0, top);
                    }
                }
            }
        });

    }


}
