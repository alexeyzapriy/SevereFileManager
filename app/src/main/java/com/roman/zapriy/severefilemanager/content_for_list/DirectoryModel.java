package com.roman.zapriy.severefilemanager.content_for_list;

import android.util.Log;

import com.roman.zapriy.severefilemanager.R;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DirectoryModel extends AbstractFileModel{
    public DirectoryModel(File file) {
        super(file);
        icon = R.mipmap.ic_folder_black_24dp;
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    public List<AbstractFileModel> getListFiles(){
        List<AbstractFileModel> list = new LinkedList<AbstractFileModel>();
        File[] files = null;
        try {
             files = file.listFiles();
        } catch (Exception e) {
            Log.e("FM", "Can not read folder " + file.getAbsolutePath());
            e.printStackTrace();
        }
        if (files == null){
            return list;
        }
        for (File f : files) {
            if(f.isDirectory()){
                list.add(new DirectoryModel(f));
            }else{
                list.add(new FileModel(f));
            }
        }
        return list;
    }


}
