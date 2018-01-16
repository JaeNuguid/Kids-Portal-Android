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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ParentAuth extends AppCompatActivity {

    EditText box1, box2, box3;
    ProgressDialog progressDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Accounts");
    DatabaseReference myRef2 = database.getReference("Configs");

    private SharedPreferences sharedPref;
    public String userE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_auth);
        final Activity activity = ParentAuth.this;;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        box1 = (EditText) findViewById(R.id.editTextEmail);
        box2 = (EditText) findViewById(R.id.editTextPassword);
        box3 = (EditText) findViewById(R.id.editTextPassword2);
    }
    String email, pass,pass2;
    public void signup(View view){
        email = box1.getText().toString().trim();
        pass = box2.getText().toString().trim();
        pass2 = box3.getText().toString().trim();

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

        if(!pass.equals(pass2)){
            msg("Password does not match.");
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }

        trySignUp(email,pass);



    }

    public void trySignUp(String user, String pass){
        final String p = pass;
        final String u = user;
        try {
            myRef.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Accounts acc = dataSnapshot.getValue(Accounts.class);
                    if(acc == null) {
                        Accounts a = new Accounts();
                        a.setPass(p);
                        a.setChanged(false);
                        myRef.child(u).setValue(a);

                        Configs c = new Configs("245 245 220","0 0 0","https://www.google.com/search?safe=active",0,p,true,true,"1",u);
                        myRef2.child(u).setValue(c);

                       success();

                    }else{
                        noSignUp();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    noSignUp();
                }

            });
        }catch (Exception e){

            noSignUp();
        }
    }

    public void noSignUp(){
        msg("Account already exist!");
    }



    public void loginNow(View v){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    public void success(){
        msg("You are now registered as a parent-admin!");
        Intent intent = new Intent(this, Activity_Main.class);
        Bundle x = new Bundle();
        x.putString("user", email);
        x.putBoolean("loggedIn", true);
        intent.putExtras(x);
        startActivity(intent);
        finish();

    }

    public void fail(){
        msg("Registration Failed!");
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
}
