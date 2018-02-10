package study.com.androidfunctions.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import study.com.androidfunctions.R;
import study.com.androidfunctions.model.ListHeaderEntity;
import study.com.androidfunctions.model.PersonInfo;

/**
 * ListView的顶部悬停适配器
 */

public class LvSuspendAdapter<T extends MultiItemEntity> extends LvCommonAdapter<T> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_DATA = 1;

    public LvSuspendAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        T t = data.get(position);
        if (t.getItemType() == TYPE_HEADER) {
            return TYPE_HEADER;
        } else   if (t.getItemType() == TYPE_DATA) {
            return TYPE_DATA;
        }

        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        T t = data.get(position);
        ListHeaderEntity lhe = (ListHeaderEntity) t;
        HeaderHolder headerHolder = null;
        ItemHolder itemHolder = null;
        switch (itemViewType) {
            case TYPE_HEADER:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.list_data_header, null);
                    headerHolder = new HeaderHolder(convertView);
                    convertView.setTag(headerHolder);
                } else {
                    headerHolder = (HeaderHolder) convertView.getTag();
                }
                headerHolder.setText(lhe.getListHeaderName());
                break;
            case TYPE_DATA:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.list_data_item, null);
                    itemHolder = new ItemHolder(convertView);
                    convertView.setTag(itemHolder);
                } else {
                    itemHolder = (ItemHolder) convertView.getTag();
                }
                PersonInfo data = (PersonInfo) lhe.getData();
                itemHolder.setText(data.getName());
                break;
        }
        return convertView;
    }

    public class HeaderHolder {
        private View view;
        private final TextView tvName;

        public HeaderHolder(View view) {
            this.view = view;
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }

        public void setText(String message) {
            tvName.setText(message);
        }
    }

    public class ItemHolder {
        private View view;
        private final TextView tvName;

        public ItemHolder(View view) {
            this.view = view;
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }

        public void setText(String message) {
            tvName.setText(message);
        }
    }
}
