package com.roman.zapriy.severefilemanager;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roman.zapriy.severefilemanager.content_for_list.AbstractFileModel;
import com.roman.zapriy.severefilemanager.content_for_list.DirectoryModel;
import com.roman.zapriy.severefilemanager.functionality.Delete;
import com.roman.zapriy.severefilemanager.functionality.ManagerFunctionality;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AbsListView.MultiChoiceModeListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FilesRVAdapter mAdapter;
    private List<AbstractFileModel> mValues;
    private String startDir;
    private String currentDir;
    private ListView mListView;
    private SharedPreferences mSettings;
    private ManagerFunctionality mfunction;
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
        mSettings = getActivity().getSharedPreferences("ManagerPrefsFile", 0);
        mfunction = new ManagerFunctionality(getActivity());
        startDir = mfunction.getStartDir();
        currentDir = startDir;

        if (view instanceof ListView) {
            mListView = (ListView) view;
            mListView.setOnItemClickListener(this);
            mListView.setOnItemLongClickListener(this);
            mListView.setMultiChoiceModeListener(this);
            mAdapter = new FilesRVAdapter(getActivity());
            mAdapter.setData(new DirectoryModel(new File(startDir)).getListFiles(getActivity()));
            mListView.setAdapter(mAdapter);
        }
        return view;
    }

    public void upDir(){
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (!startDir.equals("/")) {
            if (!isPart(startDir, currentDir)) {
                currentDir = startDir;
            }
        }

        File file = new File(currentDir);
        if(!currentDir.equals(startDir)){
            currentDir = file.getParent();
        }else{
            currentDir = startDir;
        }
      reDraw();
    }

    public void reDraw(){
        startDir = mfunction.getStartDir();

        File file = new File(currentDir);
        DirectoryModel dm = new DirectoryModel(file);
        mAdapter.setData(dm.getListFiles(getActivity()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbstractFileModel item = (AbstractFileModel) mListView.getItemAtPosition(position);
        if(mListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE){
            mAdapter.notifyDataSetChanged();
        }else {
            if (item.isDirectory()) {
                currentDir = item.getAbsolutePath();
                mAdapter.setData(((DirectoryModel) item).getListFiles(getActivity()));
                mAdapter.notifyDataSetChanged();
                mListView.clearChoices();
            } else {
                ;
                Intent intent1 = new Intent();
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction(android.content.Intent.ACTION_VIEW);

                String mime = mfunction.getMime(item.getFile());
                intent1.setDataAndType(Uri.fromFile(item.getFile()), mime);

                try {
                    startActivity(intent1);
                } catch (ActivityNotFoundException e) {
                    Log.e("Open file", e.getMessage());
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

        SparseBooleanArray arr = mListView.getCheckedItemPositions();
        switch (item.getItemId()) {
            case R.id.aboutId:
                StringBuilder sbI = new StringBuilder();

                if(mListView.getCheckedItemCount() == 1){
                    AbstractFileModel model = (AbstractFileModel) mListView.getItemAtPosition(arr.keyAt(0));
                    sbI.append(mfunction.getInfo(model.getFile()));
                }else{
                    sbI.append(getString(R.string.sizeAll));
                    long size = 0;
                    for (int i = 0; i < arr.size(); i++) {
                        if (arr.valueAt(i)) {
                            AbstractFileModel model = (AbstractFileModel) mListView.getItemAtPosition(arr.keyAt(i));
                            size += mfunction.getDirectoryLength(model.getFile());
                        }
                    }
                    sbI.append(" ");
                    sbI.append(mfunction.getSizeToString(size));
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.aboutM)
                        .setMessage(sbI)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                mode.finish();
                return true;
            case R.id.copyId:
                saveType("Copy");

                saveCopiedFiles(arr);
                mode.finish();
                return true;
            case R.id.cutId:
                saveType("Cut");
                saveCopiedFiles(arr);
                mode.finish();
                return true;
            case R.id.deleteId:
              /*  StringBuilder sbD = new StringBuilder();
                sbD.append(getString(R.string.isDelete));
                sbD.append(" ");
                sbD.append(getString(R.string.chItems));
                sbD.append("?");
              */
                Delete delete = new Delete(getActivity());
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.valueAt(i)) {
                        AbstractFileModel model = (AbstractFileModel) mListView.getItemAtPosition(arr.keyAt(i));
                        arrayList.add(model.getAbsolutePath());
                    }
                }
                delete.execute(arrayList);
                mode.finish();

                return true;
            case R.id.renameId:
                if(mListView.getCheckedItemCount() == 1){
                    final AbstractFileModel model = (AbstractFileModel) mListView.getItemAtPosition(arr.keyAt(0));
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle(R.string.rename);
                    final EditText input = new EditText(getActivity());
                    input.setText(model.getName());
                    alert.setView(input);

                    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            if (mfunction.isTrueName(value, model.getFile())) {

                                if (!mfunction.rename(model.getFile(), value)) {
                                    Toast.makeText(getActivity(),
                                            R.string.noRename,
                                            Toast.LENGTH_SHORT).show();
                                }
                                reDraw();
                            } else {
                                Toast.makeText(getActivity(),
                                        R.string.noName,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
                }else{
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.aboutM)
                            .setMessage(R.string.singleFile)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                mode.finish();
                return true;
            case R.id.cancelId:
                mode.finish();
                return true;
            default:
                mode.finish();
                return true;
        }
    }

    private void saveType(String type) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("Type", type);
        editor.apply();
    }

    private void saveCopiedFiles(SparseBooleanArray arr) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            if (arr.valueAt(i)) {
                AbstractFileModel model = (AbstractFileModel) mListView.getItemAtPosition(arr.keyAt(i));
                arrayList.add(model.getFile().getAbsolutePath());
            }
        }
        MyArr ma = new MyArr();
        ma.arr = arrayList;
        String jsonStr = new Gson().toJson(ma);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("Copied files", jsonStr);
        editor.apply();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public String getCurrentDir(){
        return currentDir;
    }

    private Boolean isPart(String start, String current) {
        return current.contains(start);
    }

}
