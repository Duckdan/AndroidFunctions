package study.com.androidfunctions.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 列表头部实体
 */
public class ListHeaderEntity<T> implements MultiItemEntity {

    private final int itemType;

    private T data;

    private String listHeaderName;

    public ListHeaderEntity(T data, int itemType, String listHeaderName) {
        this.data = data;
        this.itemType = itemType;
        this.listHeaderName = listHeaderName;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setListHeaderName(String listHeaderName) {
        this.listHeaderName = listHeaderName;
    }

    public T getData() {
        return data;
    }

    public String getListHeaderName() {
        return listHeaderName;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
