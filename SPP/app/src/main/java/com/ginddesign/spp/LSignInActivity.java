package com.ginddesign.spp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.parse.ui.ParseLoginBuilder;


public class LSignInActivity extends AppCompatActivity {

    public LSignInActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsignin);

        ParseLoginBuilder builder = new ParseLoginBuilder(LSignInActivity.this);
        startActivityForResult(builder.build(), 0);
    }

    @Override
    final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        if (resultCode == RESULT_OK) {
            Intent home = new Intent(LSignInActivity.this, LockersActivity.class);
            startActivity(home);
        } else {
            finish();
        }
    }
}
