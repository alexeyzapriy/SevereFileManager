package com.roman.zapriy.severefilemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roman.zapriy.severefilemanager.content_for_list.AbstractFileModel;

import java.util.List;

public class FilesRVAdapter extends RecyclerView.Adapter<FilesRVAdapter.ViewHolder> {

    private List<AbstractFileModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FilesRVAdapter(List<AbstractFileModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void setData(List<AbstractFileModel> items){
        mValues.clear();  // не обязательно, но гдето читал что сборщику мусора от этого легче
        mValues = items;
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imView.setImageResource(mValues.get(position).getIcon());
        holder.mContentView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imView;
        public final TextView mContentView;
        public AbstractFileModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imView = (ImageView) view.findViewById(R.id.imageFileS);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
