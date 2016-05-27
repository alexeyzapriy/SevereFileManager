package com.roman.zapriy.severefilemanager.content_for_list;

import com.roman.zapriy.severefilemanager.R;

import java.io.File;
import java.util.ArrayList;
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
        List<AbstractFileModel> list = new ArrayList<>();
        File[] files = file.listFiles();
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
