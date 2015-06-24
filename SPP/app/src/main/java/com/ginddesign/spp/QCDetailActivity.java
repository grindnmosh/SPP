package com.ginddesign.spp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.grindesign.fragment.QCDetailFragment;
import com.parse.ParseUser;

public class QCDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qcdetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qcdetail, menu);
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
            Intent qc = new Intent(this, QuickContactActivity.class);
            this.startActivity(qc);
        }
        else if (id == R.id.action_home) {
            Intent lock = new Intent(this, ListMasterActivity.class);
            this.startActivity(lock);
        } else if (id == R.id.action_add) {
            Intent add = new Intent(this, LNewActivity.class);
            this.startActivity(add);
        } else if (id == R.id.action_lock) {
            Intent lock = new Intent(this, LSignInActivity.class);
            lock.putExtra("Sent", "contact");
            this.startActivity(lock);
        } else if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, QCDetailFragment.conName.getText() + "\n" + QCDetailFragment.conPhone.getText() + "\n" + QCDetailFragment.conEmail.getText() + "\n" + QCDetailFragment.conNotes.getText());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Using"));
        } else if (id == R.id.action_edit) {
            Intent add = new Intent(this, QCNewActivity.class);
            add.putExtra("Title", "Edit Contact");
            add.putExtra("Object ID", QCDetailFragment.ois);
            startActivity(add);
        } else if (id == R.id.action_settings) {
            AlertDialog.Builder lockExit = new AlertDialog.Builder(this);
            lockExit.setTitle("Are You Sure??");
            lockExit.setMessage("This will log you completely out of the application.");
            lockExit.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseUser.logOut();
                    Intent intent = new Intent(QCDetailActivity.this, MainActivity.class);
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
            Intent change = new Intent(this, SettingsActivity.class);
            change.putExtra("Sent", "contact");
            this.startActivity(change);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent lock = new Intent(this, QuickContactActivity.class);
        this.startActivity(lock);
    }


}
