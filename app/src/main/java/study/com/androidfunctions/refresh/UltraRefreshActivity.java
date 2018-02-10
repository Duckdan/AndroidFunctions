package study.com.androidfunctions.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.classic.common.MultipleStatusView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import study.com.androidfunctions.DataResource;
import study.com.androidfunctions.R;
import study.com.androidfunctions.adapter.RvCommonAdapter;
import study.com.androidfunctions.weight.SideslipRecyclerView;

/**
 * 与侧滑事件有冲突，待解决
 */
public class UltraRefreshActivity extends AppCompatActivity {
    private final int START_FLAG_REFRESH = 100;
    private final int START_FLAG_LOAD_MORE = 200;

    @BindView(R.id.recycler_view)
    SideslipRecyclerView recyclerView;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private String TAG = "ultra";
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
                    //刷新成功

                    ptrLayout.refreshComplete();
                    List<String> refreshLists = Arrays.asList(DataResource.stringsZero);
                    List<String> data = adapter.getData();
                    recyclerView.revover();
                    if (data.get(0).contains("00")) {
                        return;
                    }
                    list.clear();
                    list.addAll(refreshLists);
                    adapter.setNewData(list);

                    Log.e(TAG, list.toString());
                    break;
                case START_FLAG_LOAD_MORE:
                    List<String> loadMoreLists = Arrays.asList(DataResource.stringsSecond);
                    data = adapter.getData();
                    String str = data.get(data.size() - 1);
                    //使用适配器上拉加载的时候使用下面这句代码告诉适配器上拉加载结束了
                    adapter.loadMoreComplete();
                    if (str.contains("29")) {

                        //2. 使用适配器上拉加载的时候使用下面一行
                        isHasMoreData = false;
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
        setContentView(R.layout.activity_ultra_refresh);
        ButterKnife.bind(this);
        list = new ArrayList<>(list);

        initView();
    }

    private void initView() {
        ptrLayout.setResistance(2.6f);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setKeepHeaderWhenRefresh(true);

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            //开始刷新
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                startFresh();
            }
        });

        initAdapter();
        initRecyclerView();
        initMultipleStatusView();
    }

    private void initMultipleStatusView() {
        //设置重试的事件
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.error_retry_view:
                        show("重新加载...");
                        break;
                    case R.id.no_network_retry_view:
                        show("设置网络...");
                        break;
                }
            }
        });
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
        recyclerView.setSlide(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
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

        onLoadMore_adapter();
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
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
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
        }, recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_1:
                show("1");
                multipleStatusView.showLoading();
                break;
            case R.id.action_2:
                show("2");
                multipleStatusView.showEmpty();
                break;
            case R.id.action_3:
                show("3");
                multipleStatusView.showError();
                break;
            case R.id.action_4:
                show("4");
                multipleStatusView.showNoNetwork();
                break;
            case R.id.action_5:
                show("5");
                multipleStatusView.showContent();
                break;
        }
        return true;
    }

    public void show(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
