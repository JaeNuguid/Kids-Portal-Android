package jae.KidsPortal.Browser;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText box1, box2;
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

    public void success(){

        Toast.makeText(this, "Access Approved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void fail(){
        Toast.makeText(this, "Access Denied!", Toast.LENGTH_SHORT).show();

    }
}
