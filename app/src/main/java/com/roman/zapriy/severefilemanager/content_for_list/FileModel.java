package com.roman.zapriy.severefilemanager.content_for_list;

import android.webkit.MimeTypeMap;

import com.roman.zapriy.severefilemanager.R;

import java.io.File;

public class FileModel extends AbstractFileModel {
    public FileModel(File file) {
        super(file);
        icon = R.mipmap.ic_insert_drive_file_white_36dp;
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }
}
