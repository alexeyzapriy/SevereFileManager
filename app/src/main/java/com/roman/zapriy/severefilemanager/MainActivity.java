package com.roman.zapriy.severefilemanager;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roman.zapriy.severefilemanager.functionality.ATCopyCallBack;
import com.roman.zapriy.severefilemanager.functionality.Copy;
import com.roman.zapriy.severefilemanager.functionality.Delete;
import com.roman.zapriy.severefilemanager.functionality.ManagerFunctionality;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ATCopyCallBack {

    private FragmentManager fragMan;
    private Boolean isShowHidden = false;
    private SharedPreferences mSettings;
    private String type = "";
    private MyArr myArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragMan = getSupportFragmentManager();
        fragMan.beginTransaction()
                .replace(R.id.container, new ItemFragment())
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.Fragment fragment = fragMan.getFragments().get(0);
                ((ItemFragment) fragment).upDir();
            }
        });

        mSettings = getSharedPreferences("ManagerPrefsFile", 0);

        if (mSettings.contains("hidden")) {
            isShowHidden = mSettings.getBoolean("hidden", false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ManagerFunctionality managerFunc = new ManagerFunctionality(this);
        int id = item.getItemId();

        final android.support.v4.app.Fragment fragment = fragMan.getFragments().get(0);
        String currDir = ((ItemFragment) fragment).getCurrentDir();
        final File f = new File(currDir);

        if (id == R.id.action_hidden) {
            toggleHidden();
            redraw();
            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        if (id == R.id.action_info) {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.aboutM)
                    .setMessage(R.string.aboutText)
                    .setCancelable(false)
                    .setNegativeButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .show();

            return true;
        }
        if (id == R.id.action_new_folder) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.newFoldName);
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if (managerFunc.isTrueName(value, f)) {
                        if (!managerFunc.newFolder(f, value)) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.noCrFold,
                                    Toast.LENGTH_SHORT).show();
                        }
                        redraw();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.noName,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(getApplicationContext(),
                            R.string.okCancel,
                            Toast.LENGTH_SHORT).show();
                }
            });

            alert.show();

            return true;
        }
        if (id == R.id.action_paste) {

            String pathListStr = "";
            if (mSettings.contains("Copied files")) {
                pathListStr = mSettings.getString("Copied files", "");
            }

            if (mSettings.contains("Type")) {
                type = mSettings.getString("Type", "");
            }
            myArr = new Gson().fromJson(pathListStr, MyArr.class);

            ArrayList<String> arrPath = new ArrayList<>();
            arrPath.add(currDir);
            arrPath.addAll(myArr.arr);
            Copy copy = new Copy(this);
            copy.execute(arrPath);

            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("Type", "");
            editor.apply();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void redraw() {
        android.support.v4.app.Fragment fragment = fragMan.getFragments().get(0);
        ((ItemFragment) fragment).reDraw();
    }

    private void toggleHidden() {
        isShowHidden = !isShowHidden;

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("hidden", isShowHidden);
        editor.apply();
    }

    @Override
    public void onTaskComplete(String result) {
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        switch (type){
            case "Copy":
                redraw();
                break;
            case "Cut":
                Delete delete = new Delete(this);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(myArr.arr);
                delete.execute(arrayList);
                break;
            default:
                redraw();
                break;
        }
    }
}
