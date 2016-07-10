package com.roman.zapriy.severefilemanager.content_for_list;

import java.io.File;

public abstract class AbstractFileModel {
    protected int icon;
    protected String name;
    protected File file;
    protected Boolean isSelected = false;

    public AbstractFileModel(File file){
        this.file = file;
        this.name = file.getName();
    }

    public abstract boolean isDirectory();

    public int getIcon(){return icon;}

    public String getName(){return name;}

    public String getAbsolutePath(){
        return file.getAbsolutePath();
    }

    public File getFile(){return file;}

    public Boolean getIsSelected(){return isSelected;}

    public void toggleIsSelected(){
        isSelected = !getIsSelected();
    }
}
