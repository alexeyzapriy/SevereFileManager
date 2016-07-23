package com.roman.zapriy.severefilemanager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;

public class ManagerFunctionality {

    private static final int BUFFER = 2048;
    private Context context;

    public ManagerFunctionality(Context c){
        context = c;
    }

    public void delDirectory(File f){
        if (f.isDirectory()) {
            File[] arrPath = f.listFiles();
            for (File file : arrPath)
                delDirectory(file);
            f.delete();
        } else f.delete();
    }

    public Boolean newFolder(File target, String name){
        File f = new File(target.getAbsolutePath() + "/" + name);
        try {
            f.mkdirs();
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    public static String getStartDir(){
        if (Environment.getExternalStorageState() == null) {
            return Environment.getDataDirectory().getAbsolutePath();
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public String getInfo(File f){
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.type));
        try {
            Boolean type = f.isFile();
            sb.append(type ? context.getString(R.string.file) : context.getString(R.string.folder));
        }
        catch (Exception e){
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.absolutPath));
        sb.append("\n");
        try {
            sb.append(f.getAbsolutePath());
        }
        catch (Exception e){
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.isRead));
        try {
            Boolean r = f.canRead();
            sb.append(r ? context.getString(R.string.y) : context.getString(R.string.n));
        }
        catch (Exception e){
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.isWrite));
        try {
            Boolean w = f.canWrite();
            sb.append(w? context.getString(R.string.y) : context.getString(R.string.n));
        }
        catch (Exception e){
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.isHidden));
        try {
            Boolean h = f.isHidden();
            sb.append(h? context.getString(R.string.y) : context.getString(R.string.n));
        }
        catch (Exception e){
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.size));
        sb.append(getSize(f));
        sb.append("\n");
        sb.append(context.getString(R.string.date));
        sb.append("\n");
        try {
            sb.append(new Date(f.lastModified()).toString());
        }
        catch (Exception e){
            sb.append(context.getString(R.string.err_));
        }

        return sb.toString();
    }

    public String getSize(File f){
        long zizeFileByte = 0;
        try {
            zizeFileByte = getDirectoryLength(f);
            float sizeK = zizeFileByte/1024f;
            float sizeM = zizeFileByte/1024f/1024f;
            float sizeG = zizeFileByte/1024f/1024f/1024f;
            if(sizeG > 1) return sizeG + "GB";
            else if(sizeM > 1) return sizeM + "MB";
            else return sizeK + "KB";
        }
        catch (Exception e){
            return context.getString(R.string.err_);
        }
    }

    private long getDirectoryLength(File f){
        long sum = 0;

        Boolean isDir = false;
        try {
            isDir = f.isDirectory();
        } catch (Exception e) {
            Log.e("isDirectory", e.getMessage());
        }
        if (isDir) {
            File[] arrPath = f.listFiles();
            if (arrPath.length > 0)
                for (File file : arrPath)
                    sum += getDirectoryLength(file);
        } else {
            try {
                sum += f.length();
            } catch (Exception e) {
                Log.e("length", e.getMessage());
            }
        }

        return  sum;
    }
}
