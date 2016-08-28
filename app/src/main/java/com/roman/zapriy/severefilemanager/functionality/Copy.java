package com.roman.zapriy.severefilemanager.functionality;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.roman.zapriy.severefilemanager.MainActivity;
import com.roman.zapriy.severefilemanager.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Copy extends AsyncTask<ArrayList<String>, Integer, String>{

    private static final int BUFFER = 2048;
    private Activity activity;
    private ATCopyCallBack callback;
    public Copy(Activity a){
        activity = a;
        callback = (ATCopyCallBack)a;
    }

    @Override
    protected String doInBackground(ArrayList<String>... arrayLists) {

        String result = "";
        ArrayList<String> arr = arrayLists[0];
        for (int i = 1; i < arr.size() ; i++){
            result = paste(arr.get(i), arr.get(0));
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(activity, R.string.copying, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callback.onTaskComplete(result);
    }


    public String paste(String from, String to ){
        File old_file = new File(from);
        File temp_dir = new File(to);
        byte[] data = new byte[BUFFER];
        int read = 0;

        if(old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()){
            String file_name = from.substring(from.lastIndexOf("/"), from.length());
            File cp_file = new File(to + file_name);

            try {
                BufferedOutputStream o_stream = new BufferedOutputStream(
                        new FileOutputStream(cp_file));
                BufferedInputStream i_stream = new BufferedInputStream(
                        new FileInputStream(old_file));

                while((read = i_stream.read(data, 0, BUFFER)) != -1)
                    o_stream.write(data, 0, read);

                o_stream.flush();
                i_stream.close();
                o_stream.close();
                return activity.getString(R.string.ok);

            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.getMessage());
                return e.getMessage();

            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                return e.getMessage();
            }

        }else if(old_file.isDirectory() && temp_dir.isDirectory() && temp_dir.canWrite()) {
            String files[] = old_file.list();
            String dir = to + from.substring(from.lastIndexOf("/"), from.length());
            int len = files.length;

            if(!new File(dir).mkdir()) {
                Log.e("mkdir", "" + activity.getString(R.string.noCrFold));
                return activity.getString(R.string.noCrFold);
            }

            for(int i = 0; i < len; i++)
                paste(from + "/" + files[i], dir);

        } else if(!temp_dir.canWrite()){
            Log.e("canWrite", ""+activity.getString(R.string.noWrite));
            return activity.getString(R.string.noWrite);
        }

        return activity.getString(R.string.nothing);
    }
}
