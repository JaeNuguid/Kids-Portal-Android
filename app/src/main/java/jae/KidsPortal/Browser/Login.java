package jae.KidsPortal.Browser;

import android.app.ProgressDialog;
import android.content.Intent;
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

import jae.KidsPortal.Browser.fragments.Fragment_Browser;
import jae.KidsPortal.Browser.helper.class_CustomViewPager;

public class Login extends AppCompatActivity {
    EditText box1, box2;
    public String user;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        box1 = (EditText) findViewById(R.id.editTextEmail);
        box2 = (EditText) findViewById(R.id.editTextPassword);
    }

    public void register(View view){
        Intent intent = new Intent(this, ParentAuth.class);
        startActivity(intent);
        finish();
    }
    public void login(View view){
        final String email = box1.getText().toString().trim();
        String pass = box2.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){

            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    success(email);

                }else{
                    fail();
                    box1.setText("");
                    box2.setText("");
                }

                progressDialog.hide();
            }
        });

    }

    public void success(String email){

        Toast.makeText(this, "You are now logged in!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Activity_Main.class);
        Bundle x = new Bundle();
        x.putString("username", email);
        x.putBoolean("loggedIn", true);
        intent.putExtras(x);
        startActivity(intent);
        finish();

    }



    public void fail(){
        Toast.makeText(this, "Access Failed!", Toast.LENGTH_SHORT).show();

    }
}
