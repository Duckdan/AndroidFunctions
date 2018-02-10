package study.com.androidfunctions.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import study.com.androidfunctions.DataResource;
import study.com.androidfunctions.R;
import study.com.androidfunctions.adapter.RvCommonAdapter;
import study.com.androidfunctions.weight.SideslipRecyclerView;

public class SmartRefreshActivity extends AppCompatActivity {

    private final int START_FLAG_REFRESH = 100;
    private final int START_FLAG_LOAD_MORE = 200;

    @BindView(R.id.recycler_view)
    SideslipRecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String TAG = "smart";
    //初始数据
    List<String> list = Arrays.asList(DataResource.stringsFirst);
    private RvCommonAdapter<String> adapter;
    //判断是否还有更多数据
    private boolean isHasMoreData = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case START_FLAG_REFRESH:
                    refreshLayout.finishRefresh(true);//刷新成功
                    List<String> refreshLists = Arrays.asList(DataResource.stringsZero);
                    List<String> data = adapter.getData();
                    recyclerview.revover();
                    if (data.get(0).contains("00")) {
                        ClassicsHeader header = (ClassicsHeader) refreshLayout.getRefreshHeader();
                        header.setLastUpdateText("目前没有最新数据");
                        return;
                    }
                    list.clear();
                    list.addAll(refreshLists);
                    adapter.setNewData(list);

                    Log.e(TAG, list.toString());
                    break;
                case START_FLAG_LOAD_MORE:
                    //使用适配器上拉加载的时候使用下面这句代码告诉适配器上拉加载结束了
//                    adapter.loadMoreComplete();//上拉加载结束了
                    refreshLayout.finishLoadMore(true);//上拉加载成功
                    List<String> loadMoreLists = Arrays.asList(DataResource.stringsSecond);
                    data = adapter.getData();
                    String str = data.get(data.size() - 1);
                    if (str.contains("29")) {
                        //1. 使用SmartRefreshLayout上拉加载的时候使用下面两行
                        ClassicsFooter footer = (ClassicsFooter) refreshLayout.getRefreshFooter();
                        footer.setNoMoreData(true); //没有跟多数据了

                        //2. 使用适配器上拉加载的时候使用下面一行
//                        isHasMoreData = false;
                        return;
                    }
                    adapter.addData(loadMoreLists);
                    Log.e(TAG, loadMoreLists.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_refresh);
        ButterKnife.bind(this);
        list = new ArrayList<>(list);
        //设置 Header 为 经典 样式
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));


        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                Log.e(TAG, "开始刷新");
                startFresh();
            }
        });
        onLoadMore_smart();


        recyclerview.setOnItemClickListener(new SideslipRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                adapter.remove(position);
            }
        });

        initAdapter();
        initRecyclerView();
    }

    /**
     * 开始刷新
     */
    private void startFresh() {
        handler.sendEmptyMessageDelayed(START_FLAG_REFRESH, 2000);
    }

    /**
     * 开始加载
     */
    private void startLoadMore() {
        handler.sendEmptyMessageDelayed(START_FLAG_LOAD_MORE, 2000);
    }

    /**
     * 初始化Rv
     */
    private void initRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(adapter);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        adapter = new RvCommonAdapter<String>(R.layout.rv_item_layout, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
            }
        };

//        onLoadMore_adapter();
    }

    /**
     * 利用SmartRefreshLayout做上拉加载
     */
    private void onLoadMore_smart() {
        //设置 Footer 为 经典样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                Log.e(TAG, "开始加载");
                startLoadMore();
            }
        });
    }


    /**
     * 利用适配器达到上拉加载更多的目的
     */
    private void onLoadMore_adapter() {
        adapter.openLoadAnimation();
        adapter.isFirstOnly(true);
        adapter.setLoadMoreView(new SimpleLoadMoreView());

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.e(TAG, isHasMoreData + "=1=1=");
                recyclerview.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, isHasMoreData + "===");
                        if (isHasMoreData) {
                            startLoadMore();
                        } else {
                            //适配器的数据总量大于页面展示的数量时执行，主要是为了显示根布局
                            if (adapter.getData().size() > 5)
                                adapter.loadMoreEnd();
                                //适配器的数据总量小于于页面展示的数量时执行，主要是为了不显示根布局
                            else
                                adapter.loadMoreEnd(true);
                        }
                    }
                });

            }
        }, recyclerview);
    }

}
