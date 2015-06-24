package com.ginddesign.spp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

public class LChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lchild);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lchild, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qc) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent qc = new Intent(LChildActivity.this, QuickContactActivity.class);
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
        } else if (id == R.id.action_home) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent home = new Intent(LChildActivity.this, ListMasterActivity.class);
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
        } else if (id == R.id.action_image) {
            Intent image = new Intent(this, LImageActivity.class);
            this.startActivity(image);
        } else if (id == R.id.action_add) {
            Intent add = new Intent(this, LNewActivity.class);
            add.putExtra("Title", "Create New Child");
            add.putExtra("Object ID", "New");
            this.startActivity(add);
        } else if (id == R.id.action_settings) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Are You Sure??");
            lockExit.setMessage("This will log you completely out of the application.");
            lockExit.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseUser.logOut();
                    Intent intent = new Intent(LChildActivity.this, MainActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                }
            });
            lockExit.setNegativeButton("Stay Logged In", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            lockExit.setIcon(android.R.drawable.ic_dialog_alert);
            lockExit.show();

        } else if (id == R.id.action_change) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent change = new Intent(LChildActivity.this, SettingsActivity.class);
                    change.putExtra("Sent", "lock");
                    startActivity(change);
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

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent lock = new Intent(this, LockersActivity.class);
        this.startActivity(lock);
    }



}
