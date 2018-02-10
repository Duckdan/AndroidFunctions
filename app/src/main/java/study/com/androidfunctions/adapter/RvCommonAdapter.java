package study.com.androidfunctions.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * RecyclerView公用的适配器
 * T是数据集合中存储的类型
 */

public abstract class RvCommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    /**
     * 创建适配器
     *
     * @param layoutResId 条目布局
     * @param data        数据集合
     */
    public RvCommonAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }


    @Override
    protected abstract void convert(BaseViewHolder helper, T item);
}
