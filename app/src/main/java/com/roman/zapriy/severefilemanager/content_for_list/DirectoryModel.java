package com.roman.zapriy.severefilemanager.content_for_list;

import com.roman.zapriy.severefilemanager.R;

import java.io.File;

public class DirectoryModel extends AbstractFileModel{
    public DirectoryModel(File file) {
        super(file);
        icon = R.mipmap.ic_folder_black_18dp;
    }

    @Override
    public void execute() {

    }
}
