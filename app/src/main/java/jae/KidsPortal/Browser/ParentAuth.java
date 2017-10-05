package jae.KidsPortal.Browser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class ParentAuth extends AppCompatActivity {

    EditText box1, box2;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    public String userE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_auth);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        box1 = (EditText) findViewById(R.id.editTextEmail);
        box2 = (EditText) findViewById(R.id.editTextPassword);
    }
    String email, pass;
    public void signup(View view){
        email = box1.getText().toString().trim();
        pass = box2.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){

            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Registering Parent...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userE = email;
                    //   OneSignal.sendTag("user_ID",userE);

                    success();

                }else{
                    fail();
                    box1.setText("");
                    box2.setText("");
                }

                progressDialog.hide();
            }
        });

    }

    public void loginNow(View v){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
    public void success(){

        Toast.makeText(this, "You are now registered as a parent-admin!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Activity_Main.class);
        Bundle x = new Bundle();
        x.putString("username", email);
        x.putBoolean("loggedIn", true);
        intent.putExtras(x);
        startActivity(intent);
        finish();

    }

    public void fail(){
        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
    }
}
