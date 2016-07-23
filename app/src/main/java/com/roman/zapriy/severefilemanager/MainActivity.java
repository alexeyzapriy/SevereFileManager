package com.roman.zapriy.severefilemanager;

import android.app.Activity;
import android.content.DialogInterface;
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

import java.io.File;

public class MainActivity extends AppCompatActivity{

    private FragmentManager fragMan;
    private Activity activity;

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
                ((ItemFragment)fragment).upDir();
            }
        });

        activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        final android.support.v4.app.Fragment fragment = fragMan.getFragments().get(0);
        String currDir = ((ItemFragment)fragment).getCurrentDir();
        final File f = new File(currDir);

        if (id == R.id.action_hidden) {

            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        if (id == R.id.action_info) {

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
                    if (isTrueName(value, f)) {
                        ManagerFunctionality fm = new ManagerFunctionality(activity);
                        if (!fm.newFolder(f, value)) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.noCrFold,
                                    Toast.LENGTH_SHORT).show();
                        }
                        ((ItemFragment)fragment).reDraw();
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isTrueName(String value, File f) {
        if(f.isFile()) f = f.getParentFile();
        String [] names = f.list();
        for (String s : names){
            if(s.equals(value)) {
                return false;
            }
        }

        return  true;
    }

}
