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

    public String getMime(){
        String ext;
        String type;

        int lastdot = name.lastIndexOf(".");
        if(lastdot > 0){
            ext = name.substring(lastdot + 1);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(ext);
            if(type != null) {
                type.toLowerCase();
                return type;
            }
        }
        return "application/octet-stream";
    }
}
