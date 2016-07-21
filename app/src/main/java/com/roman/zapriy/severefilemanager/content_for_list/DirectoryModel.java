package com.roman.zapriy.severefilemanager.content_for_list;

import com.roman.zapriy.severefilemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryModel extends AbstractFileModel{
    public DirectoryModel(File file) {
        super(file);
        icon = R.mipmap.ic_folder_open_white_36dp;
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    public List<AbstractFileModel> getListFiles(){
        List<AbstractFileModel> listFile = new ArrayList<>();
        List<AbstractFileModel> listDir = new ArrayList<>();
        File[] files = file.listFiles();
        if (files == null){
            return listDir;
        }
        for (File f : files) {

            if(f.isDirectory()){
                listDir.add(new DirectoryModel(f));
            }else{
                listFile.add(new FileModel(f));
            }
        }
        listDir.addAll(listFile);
        return listDir;
    }

}
