package com.roman.zapriy.severefilemanager.functionality;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.roman.zapriy.severefilemanager.MainActivity;
import com.roman.zapriy.severefilemanager.R;

import java.io.File;
import java.util.ArrayList;

public class Delete extends AsyncTask<ArrayList<String>, Integer, String> {

    private Activity activity;

    public Delete(Activity a){
        activity = a;
    }

    @Override
    protected String doInBackground(ArrayList<String>... arrayLists) {
        String result = "";
        ArrayList<String> arr = arrayLists[0];

        try {
            for (int i = 0; i < arr.size() ; i++){
                File f = new File(arr.get(i));
                delDirectory(f);
            }
            result = activity.getString(R.string.ok);
        }catch (Exception e){
            Log.e("delErr", "" + activity.getString(R.string.delErr) + " : " + e.getMessage());
            result = activity.getString(R.string.delErr);
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(activity, R.string.deletion, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
        ((MainActivity)activity).redraw();
    }


    public void delDirectory(File f) {
        if (f.isDirectory()) {
            File[] arrPath = f.listFiles();
            for (File file : arrPath)
                delDirectory(file);
            f.delete();
        } else f.delete();
    }
}
