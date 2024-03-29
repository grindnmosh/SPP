package com.ginddesign.spp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.grindesign.fragment.LDetailFragment;
import com.parse.ParseUser;

public class LDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ldetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ldetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_qc) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Leave The Lockers?");
            lockExit.setMessage("This will take you out of the secure Locker Area and you will be required to login to enter again. Are you sure you want to do this?");
            lockExit.setPositiveButton("Exit Locker", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent qc = new Intent(LDetailActivity.this, QuickContactActivity.class);
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
                    Intent home = new Intent(LDetailActivity.this, ListMasterActivity.class);
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
        } else if (id == R.id.action_edit) {
            Intent edit = new Intent(this, LNewActivity.class);
            edit.putExtra("Title", "Update Child Information");
            edit.putExtra("Object ID", LDetailFragment.ois);
            this.startActivity(edit);
        } else if (id == R.id.action_image) {
            Intent image = new Intent(this, LImageActivity.class);
            this.startActivity(image);
        } else if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, LDetailFragment.dName.getText() + "\n" + LDetailFragment.ddob.getText() + "\n" + "Allergies: " + LDetailFragment.dAll.getText() + "\n" + "Medical Conditions: " + LDetailFragment.dMed.getText());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Using"));
        } else if (id == R.id.action_child) {
            Intent child = new Intent(this, LChildActivity.class);
            this.startActivity(child);
        } else if (id == R.id.action_settings) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Are You Sure??");
            lockExit.setMessage("This will log you completely out of the application.");
            lockExit.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseUser.logOut();
                    Intent intent = new Intent(LDetailActivity.this, MainActivity.class);
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
                    Intent change = new Intent(LDetailActivity.this, SettingsActivity.class);
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
        Intent lock = new Intent(this, LChildActivity.class);
        this.startActivity(lock);
    }


}
