package ch.epfl.sdp.kandle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.kandle.DependencyInjection.Authentication;
import ch.epfl.sdp.kandle.DependencyInjection.Database;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mFullName,mEmail,mPassword, mPasswordConfirm;
    Button mSignUpBtn;
    TextView mSignInLink;
    Authentication Auth;
    //ProgressDialog pd;

    Database fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password);
        mPasswordConfirm = findViewById(R.id.passwordConfirm);
        mSignUpBtn = findViewById(R.id.loginBtn);
        mSignInLink = findViewById(R.id.signInLink);
        fStore = Database.getDatabaseSystem();
        Auth = Authentication.getAuthenticationSystem();

        mSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });



        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = mFullName.getText().toString();
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String passwordConfirm = mPasswordConfirm.getText().toString().trim();

                if (!checkFields(fullName,email, password, passwordConfirm)){
                    return;
                }
                 performRegisterViaFirebase(fullName, email, password);
            }
        });


    }

    private void performRegisterViaFirebase (final String fullName, final String email, String password)  {

        // pd = new ProgressDialog(RegisterActivity.this);
        // pd.setMessage("Connection...");
        // pd.show();

        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Toast.makeText(RegisterActivity.this, "User has been created", Toast.LENGTH_LONG ).show();

                    userID = Auth.getCurrentUser().getUid();

                    fStore.createUser( new User(userID, email, email));

                    startActivity(new Intent(getApplicationContext(), CustomAccountActivity.class));
                    finish();

                }

                else {
                    Toast.makeText(RegisterActivity.this, "An error has occurred : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private boolean checkFields (String fullName, String email, String password, String passwordConfirm){

        boolean bool = true;

        if (fullName.isEmpty() ){
            mFullName.setError("Your full name is required !");
            bool = false;
        }

         else if (email.isEmpty() ){
            mEmail.setError("Your email is required !" );
            bool =  false;
        }

        else if (password.length()<8){
            mPassword.setError("Please choose a password of more than 8 characters !");
            bool = false;
        }

        else if (!password.equals(passwordConfirm)){
            mPasswordConfirm.setError("Your passwords do not match !");
            bool = false;
        }

        return bool;

    }


}
