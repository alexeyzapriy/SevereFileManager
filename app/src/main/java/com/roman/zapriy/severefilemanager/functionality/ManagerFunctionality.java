package com.roman.zapriy.severefilemanager.functionality;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.roman.zapriy.severefilemanager.MimeTypeUtils;
import com.roman.zapriy.severefilemanager.R;

import java.io.File;
import java.util.Date;

public class ManagerFunctionality {
    private Context context;

    public ManagerFunctionality(Context c) {
        context = c;
    }

    public Boolean newFolder(File target, String name) {
        File f = new File(target.getAbsolutePath() + "/" + name);
        try {
            f.mkdirs();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public String getStartDir() {
        SharedPreferences mSettings = context.getSharedPreferences("ManagerPrefsFile", 0);
        String startDir = "/storage";
        if (mSettings.contains("startRoot")) {
            Boolean startIsRoot = mSettings.getBoolean("startRoot", false);
            if (startIsRoot) {
                startDir = "/";
            } else {
                startDir = "/storage";
            }
        }
        return startDir;
    }

    public String getInfo(File f) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.type));
        try {
            Boolean type = f.isFile();
            sb.append(type ? context.getString(R.string.file) : context.getString(R.string.folder));
        } catch (Exception e) {
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.absolutPath));
        sb.append("\n");
        try {
            sb.append(f.getAbsolutePath());
        } catch (Exception e) {
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.isRead));
        try {
            Boolean r = f.canRead();
            sb.append(r ? context.getString(R.string.y) : context.getString(R.string.n));
        } catch (Exception e) {
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.isWrite));
        try {
            Boolean w = f.canWrite();
            sb.append(w ? context.getString(R.string.y) : context.getString(R.string.n));
        } catch (Exception e) {
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.isHidden));
        try {
            Boolean h = f.isHidden();
            sb.append(h ? context.getString(R.string.y) : context.getString(R.string.n));
        } catch (Exception e) {
            sb.append(context.getString(R.string.err_));
        }
        sb.append("\n");
        sb.append(context.getString(R.string.size));
        sb.append(getSizeToString(getDirectoryLength(f)));
        sb.append("\n");
        sb.append(context.getString(R.string.date));
        sb.append("\n");
        try {
            sb.append(new Date(f.lastModified()).toString());
        } catch (Exception e) {
            sb.append(context.getString(R.string.err_));
        }

        return sb.toString();
    }

    public String getSizeToString(long sizeFileByte) {
        try {
            float sizeK = sizeFileByte / 1024f;
            float sizeM = sizeFileByte / 1024f / 1024f;
            float sizeG = sizeFileByte / 1024f / 1024f / 1024f;
            if (sizeG > 1) return String.format("%.2f", sizeG) + " GB";
            else if (sizeM > 1) return String.format("%.2f", sizeM) + " MB";
            else return String.format("%.0f", sizeK) + " KB";
        } catch (Exception e) {
            return context.getString(R.string.err_);
        }
    }

    public long getDirectoryLength(File f) {
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

        return sum;
    }

    public boolean isTrueName(String value, File f) {
        if (f.isFile()) f = f.getParentFile();
        String[] names = f.list();
        for (String s : names) {
            if (s.equals(value)) {
                return false;
            }
        }

        return true;
    }


    public Boolean rename(File target, String name) {
        if (target.isDirectory()) {
            try {
                target.renameTo(new File(target.getParent() + "/" + name));
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                target.renameTo(new File(target.getParent() + "/" + name + "." + getFileExtension(target)));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private String getFileExtension(File file) {
        if (file.isDirectory()) return "";
        String ext = "";

        String name = file.getName();
        int lastdot = name.lastIndexOf(".");
        if (lastdot > 0) {
            ext = name.substring(lastdot + 1);
        }
        return ext;
    }

    public String getMime(File file) {
        String type;
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String fileExtension = getFileExtension(file);
        type = mime.getMimeTypeFromExtension(fileExtension);
        if (type != null) {
            type.toLowerCase();
            return type;
        } else {
            type = MimeTypeUtils.getMimeTypeForExtension(fileExtension);
            if (type != null) {
                type.toLowerCase();
                return type;
            }
        }

        return "application/octet-stream";
    }

}
