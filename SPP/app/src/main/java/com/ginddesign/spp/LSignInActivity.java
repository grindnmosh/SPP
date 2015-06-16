package com.ginddesign.spp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;


public class LSignInActivity extends AppCompatActivity {

    Button verify;
    EditText password;
    String userId;
    String pass;
    Context thisHere = this;

    public LSignInActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsignin);

        //ParseLoginBuilder builder = new ParseLoginBuilder(LSignInActivity.this);
        //startActivityForResult(builder.build(), 0);

        password = (EditText) findViewById(R.id.Pass);
        verify = (Button) findViewById(R.id.verify);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = ParseUser.getCurrentUser().getUsername();
                pass = password.getText().toString().trim();
                Log.i("User", userId);

                ConnectivityManager cm = (ConnectivityManager) thisHere.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {



                    ParseUser.logInInBackground(userId, pass,
                            new LogInCallback() {
                                public void done(ParseUser parseUser, com.parse.ParseException e) {
                                    if (parseUser != null) {
                                        Intent intent = new Intent(
                                                LSignInActivity.this,
                                                LockersActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Incorrect Password, Please Try Again",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });


                }else{
                    Toast.makeText(thisHere, "No network detected. Please connect to a network to login", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public void onBackPressed()
    {
        Intent home = new Intent(this, ListMasterActivity.class);
        this.startActivity(home);

    }
}
