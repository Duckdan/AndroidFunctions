package study.com.androidfunctions.contact;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lqr.recyclerview.LQRRecyclerView;
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
 * 第二版联系人列表
 */
public class SecondContactActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    LQRRecyclerView recyclerView;
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
        setContentView(R.layout.activity_second_contact);
        ButterKnife.bind(this);
        generateData(list);
        adapterList = PinyinUtils.getContactsList(list);

        initRecyclerView();
        initAdapter();
        initEvent();
    }

    private void initRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        phid = new PinnedHeaderItemDecoration.
                Builder(RvHeaderAdapter.TYPE_HEADER).
                setDividerId(R.drawable.divider_item_shape).enableDivider(true).create();


        recyclerView.addItemDecoration(phid);


        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ListHeaderEntity<PersonInfo> item = adapter.getItem(i);
                switch (item.getItemType()) {
                    case RvHeaderAdapter.TYPE_DATA:
                        ListHeaderEntity<PersonInfo> entity = adapter.getData().get(i);
                        Toast.makeText(SecondContactActivity.this, entity.getListHeaderName() + ", position " + i + ", id " + entity.getData().getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case RvHeaderAdapter.TYPE_HEADER:
                        ListHeaderEntity<PersonInfo> headerEntity = adapter.getData().get(i);
                        Toast.makeText(SecondContactActivity.this, "标题：" + headerEntity.getListHeaderName() + ",position:" + i, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SecondContactActivity.this, "搜索", Toast.LENGTH_SHORT).show();

            }
        });

        verifyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondContactActivity.this, "验证", Toast.LENGTH_SHORT).show();
            }
        });

        groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondContactActivity.this, "群聊", Toast.LENGTH_SHORT).show();
            }
        });


//        adapter.addHeaderView(headerView);
        //添加尾部布局
        View inflate = View.inflate(this, R.layout.list_data_item, null);
        inflate.setBackgroundColor(Color.GRAY);
        TextView tvName = (TextView) inflate.findViewById(R.id.tv_name);
        tvName.setText(list.size() + "位联系人");
        adapter.addFooterView(inflate);
//        MyAdapter adapter = new MyAdapter(this, adapterList);
        recyclerView.setAdapter(adapter);
    }


    private void initEvent() {
        iv.setOnItemClickListener(new IndicatorView.OnItemClickListener() {
            @Override
            public void onItemClick(String letter) {
                Map<String, Integer> keyMap = PinyinUtils.keyMap;
                Integer integer = keyMap.get(letter);
                if (integer != null) {
                    recyclerView.moveToPosition(integer);
                }
            }
        });


    }

    private void generateData(ArrayList<PersonInfo> list) {
        List<String> contactLists = Arrays.asList(DataResource.contacts);
        list.clear();
        for (int i = 0; i < contactLists.size(); i++) {
            PersonInfo personInfo = new PersonInfo(contactLists.get(i));
            list.add(personInfo);
        }
    }

//    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//
//        private Context mContext;
//        private List<ListHeaderEntity<PersonInfo>> mData;
//
//        public MyAdapter(Context context, List<ListHeaderEntity<PersonInfo>> data) {
//            mContext = context;
//            mData = data;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            View view = View.inflate(mContext, R.layout.item_list, null);
//            View view = View.inflate(mContext, R.layout.list_data_item, null);
//            return new MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//
//            holder.tv.setText(mData.get(position).getData().getName());
//        }
//
//        @Override
//        public int getItemCount() {
//            if (mData != null) {
//                return mData.size();
//            }
//            return 0;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//
//            TextView tv;
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//
//                tv = (TextView) itemView.findViewById(R.id.tv_name);
//            }
//        }
//    }
}
