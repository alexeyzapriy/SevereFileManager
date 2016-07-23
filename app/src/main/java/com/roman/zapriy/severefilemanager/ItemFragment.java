package com.roman.zapriy.severefilemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.roman.zapriy.severefilemanager.content_for_list.AbstractFileModel;
import com.roman.zapriy.severefilemanager.content_for_list.DirectoryModel;
import com.roman.zapriy.severefilemanager.content_for_list.FileModel;

import java.io.File;
import java.util.List;

public class ItemFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.MultiChoiceModeListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FilesRVAdapter mAdapter;
    private List<AbstractFileModel> mValues;
    private String startDir = ManagerFunctionality.getStartDir();
    private String currentDir = startDir;
    private ListView mListView;

    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof ListView) {
            mListView = (ListView) view;
            mListView.setOnItemClickListener(this);
            mListView.setOnItemLongClickListener(this);
            mListView.setMultiChoiceModeListener(this);
            mAdapter = new FilesRVAdapter(getActivity());
            mAdapter.setData(new DirectoryModel(new File(startDir)).getListFiles());
            mListView.setAdapter(mAdapter);
        }
        return view;
    }

    public void upDir(){
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        File file = new File(currentDir);
        if(!currentDir.equals(startDir)){
            currentDir = file.getParent();
        }else{
            currentDir = startDir;
        }
        file = new File(currentDir);
        DirectoryModel dm = new DirectoryModel(file);
        mAdapter.setData(dm.getListFiles());
        mAdapter.notifyDataSetChanged();
    }

    public void reDraw(){
        File file = new File(currentDir);
        DirectoryModel dm = new DirectoryModel(file);
        mAdapter.setData(dm.getListFiles());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbstractFileModel item = (AbstractFileModel) mListView.getItemAtPosition(position);
        if(mListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
            //item.toggleIsSelected();
            mAdapter.notifyDataSetChanged();
        }else {
            if (item.isDirectory()) {
                currentDir = item.getAbsolutePath();
                mAdapter.setData(((DirectoryModel) item).getListFiles());
                mAdapter.notifyDataSetChanged();
                mListView.clearChoices();
            } else {
                Intent intent1 = new Intent();
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction(android.content.Intent.ACTION_VIEW);

                intent1.setDataAndType(Uri.fromFile(item.getFile()), ((FileModel) item).getMime());

                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(getActivity(),
                            getString(R.string.noOpen),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(mListView.getChoiceMode() != ListView.CHOICE_MODE_MULTIPLE_MODAL) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            //AbstractFileModel item = (AbstractFileModel) mListView.getItemAtPosition(position);
            //item.toggleIsSelected();
           // mListView.setSelection(position);
           mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        final int checkedCount = mListView.getCheckedItemCount();
        switch (checkedCount) {
            case 0:
                mode.setSubtitle(null);
                break;
            case 1:
                mode.setSubtitle("One item selected");
                break;
            default:
                mode.setSubtitle("" + checkedCount + " items selected");
                break;
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_for_long_click, menu);
        mode.setTitle("Select Items");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public String getCurrentDir(){
        return currentDir;
    }

}
