package com.roman.zapriy.severefilemanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roman.zapriy.severefilemanager.content_for_list.AbstractFileModel;

import java.util.ArrayList;
import java.util.List;

public class FilesRVAdapter extends BaseAdapter {

    private final Activity mActivity;
    private List<AbstractFileModel> mValues = new ArrayList<>();

    public FilesRVAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setData(List<AbstractFileModel> items) {
        mValues.clear();  // не обязательно, но гдето читал что сборщику мусора от этого легче
        mValues = items;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.fragment_item, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder(convertView);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AbstractFileModel objectItem = mValues.get(position);

        if (objectItem != null) {
            ManagerFunctionality managerFunctionality = new ManagerFunctionality(mActivity);
            viewHolder.mContentView.setText(objectItem.getName());
            viewHolder.imView.setImageResource(objectItem.getIcon());
            viewHolder.mContentView.setTag(objectItem.getAbsolutePath());
            viewHolder.mSizeView.setText(objectItem.isDirectory() ? "" : managerFunctionality.getSizeToString(managerFunctionality.getDirectoryLength(objectItem.getFile())));
        }

        return convertView;
    }

    public class ViewHolder {
        public final View mView;
        public final ImageView imView;
        public final TextView mContentView;
        public final TextView mSizeView;
        public AbstractFileModel mItem;

        public ViewHolder(View view) {
            mView = view;
            imView = (ImageView) view.findViewById(R.id.imageFileS);
            mContentView = (TextView) view.findViewById(R.id.content);
            mSizeView = (TextView) view.findViewById(R.id.size);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
