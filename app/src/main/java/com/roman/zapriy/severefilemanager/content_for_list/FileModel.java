package com.roman.zapriy.severefilemanager.content_for_list;

import com.roman.zapriy.severefilemanager.R;

import java.io.File;

public class FileModel extends AbstractFileModel {
    public FileModel(File file) {
        super(file);
        icon = R.mipmap.ic_description_black_18dp;
    }

    @Override
    public void execute() {

    }
}
