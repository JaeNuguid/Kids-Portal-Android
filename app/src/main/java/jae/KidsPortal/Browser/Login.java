package jae.KidsPortal.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jae.KidsPortal.Browser.fragments.Fragment_Browser;
import jae.KidsPortal.Browser.helper.class_CustomViewPager;

public class Login extends AppCompatActivity {
    EditText box1, box2;
    public String user;
    private SharedPreferences sharedPref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Accounts");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final Activity activity = Login.this;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        box1 = (EditText) findViewById(R.id.editTextEmail);
        box2 = (EditText) findViewById(R.id.editTextPassword);
    }


    public void signUpNow(View v){
        Intent intent = new Intent(this, ParentAuth.class);
        startActivity(intent);
        finish();
    }
    public void login(View view){
        final String email = box1.getText().toString().trim();
        String pass = box2.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            msg("Please enter a username.");
            return;
        }

        if(TextUtils.isEmpty(pass)){
            msg("Please enter a password.");
            return;
        }

        if(email.length()<6){
            msg("Username is too short.");
            return;
        }

        if(pass.length()<6){
            msg("Password is too short.");
            return;
        }

        try {
            final String p = pass;
            final String u = email;
            myRef.child(u).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Accounts acc = dataSnapshot.getValue(Accounts.class);
                    if(acc != null) {
                        if (acc.getPass().equals(p)) {

                            login(dataSnapshot.getKey(),p);

                        } else {
                            noLogin();
                        }
                    }else{
                        noLogin();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    noLogin();
                }

            });
        }catch (Exception e){

            noLogin();
        }

    }





    public void login(String user, String pass){

        sharedPref.edit().putString("user",user).apply();
        sharedPref.edit().putString("pass",pass).apply();

        msg("You are now logged in!");
        Intent intent = new Intent(this, Activity_Main.class);
        Bundle x = new Bundle();
        x.putString("user", user);
        x.putBoolean("loggedIn", true);
        intent.putExtras(x);
        startActivity(intent);
        finish();
    }

    public void noLogin(){
        msg("Account does not exist!");
    }

    public void msg(String msg){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(msg);
        builder1.setTitle("Kids Portal");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }
    public void fail(){
        Toast.makeText(this, "Access Failed!", Toast.LENGTH_SHORT).show();

    }
}
