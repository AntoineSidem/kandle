package ch.epfl.sdp.kandle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    TextView mSignIn;
    EditText mEmail, mPassword;
    Button mSignUpBtn;
    FirebaseAuth fAuth;
    //ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // FirebaseAuthFactory fAuthFactory = new FirebaseAuthFactory();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuthFactory.getDependency();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mSignIn = findViewById(R.id.signUpLink);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });


        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mSignUpBtn = findViewById(R.id.loginBtn);




        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (!checkFields(email, password))  {
                    return;
                }

                performLoginViaFirebase(email, password);
            }
        });

    }


    private boolean checkFields (String email, String password){

        if (email.isEmpty()) {
            mEmail.setError(getString(R.string.login_email_required));
            return false ;
        }
        if (password.isEmpty()) {
            mPassword.setError(getString(R.string.login_password_required));
            return false ;
        }

        return true;

    }



    private void performLoginViaFirebase(String email, String password) {

        System.out.println("begin");
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               // System.out.println("begin");

                //pd = new ProgressDialog(LoginActivity.this);
                //pd.setMessage("Connection...");
                //pd.show();

                if (task.isSuccessful()) {

                    System.out.println("done task");
                    //pd.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                } else {
                    //pd.dismiss();
                    System.out.println("dont task");
                    Toast.makeText(LoginActivity.this, "An error has occurred : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
