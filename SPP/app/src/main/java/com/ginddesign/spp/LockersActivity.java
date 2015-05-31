package com.ginddesign.spp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

public class LockersActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lockers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lockers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ParseUser.logOut();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        else if (id == R.id.action_qc) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent qc = new Intent(LockersActivity.this, QuickContactActivity.class);
                    startActivity(qc);
                }
            });
            lockExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            lockExit.setIcon(android.R.drawable.ic_dialog_alert);
            lockExit.show();
        }
        else if (id == R.id.action_home) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent home = new Intent(LockersActivity.this, ListMasterActivity.class);
                    startActivity(home);
                }
            });
            lockExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            lockExit.setIcon(android.R.drawable.ic_dialog_alert);
            lockExit.show();
        }
        switch (item.getItemId()) {
        case R.id.home:
            Log.i("TEST", "TESSSSSSSSSSSSSSSST");
            final Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                Log.i("TEST", "TESSSSSSSSSSSSSSSST IFFFFF");
                finish();
                return true;
            } else {
                Log.i("TEST", "TESSSSSSSSSSSSSSSST ELSE");
                AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
                lockExit.setTitle("Leave The Lockers?");
                lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
                lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpTo(LockersActivity.this, upIntent);
                    }
                });
                lockExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                lockExit.setIcon(android.R.drawable.ic_dialog_alert);
                lockExit.show();

            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
        lockExit.setTitle("Leave The Lockers?");
        lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
        lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent home = new Intent(LockersActivity.this, ListMasterActivity.class);
                startActivity(home);
            }
        });
        lockExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        lockExit.setIcon(android.R.drawable.ic_dialog_alert);
        lockExit.show();

    }
}
