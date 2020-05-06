package ch.epfl.sdp.kandle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import ch.epfl.sdp.kandle.R;
import ch.epfl.sdp.kandle.User;
import ch.epfl.sdp.kandle.dependencies.Authentication;
import ch.epfl.sdp.kandle.dependencies.DependencyManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {


    private TextView mSignIn;
    private EditText mEmail, mPassword;
    private Button mSignUpBtn;
    private ImageButton mGameButton;
    private Authentication auth;
    private CoordinatorLayout CNetworkBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = DependencyManager.getAuthSystem();


        if (auth.getCurrentUserAtApplicationStart()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mSignIn = findViewById(R.id.signUpLink);
        mSignIn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mSignUpBtn = findViewById(R.id.loginBtn);
        mGameButton = findViewById(R.id.startOfflineGameButton);

        mSignUpBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            if (checkFields(email, password) && checkForInternetConnection()) {
                attemptLogin(email, password);
            }
        });

        if(!checkForInternetConnection()){
            mGameButton.setVisibility(View.VISIBLE);
            mGameButton.setOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(), OfflineGameActivity.class));
            });
        }
    }


    private boolean checkFields(String email, String password) {

        if (email.isEmpty()) {
            mEmail.setError(getString(R.string.login_email_required));
            return false;
        }
        if (password.isEmpty()) {
            mPassword.setError(getString(R.string.login_password_required));
            return false;
        }

        return true;

    }

    private boolean checkForInternetConnection() {
        if (!DependencyManager.getNetworkStateSystem().isConnected()) {
            CNetworkBar = (CoordinatorLayout) findViewById(R.id.connectionBar);
            Snackbar snackbar = Snackbar.make(CNetworkBar, R.string.no_connexion, Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.white));
            CNetworkBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            snackbar.show();
            CNetworkBar.bringToFront();
            return false;
        }
        return true;

    }

    private void attemptLogin(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.login_in_progress));
        pd.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<User>() {
            @Override
            public void onComplete(@NonNull Task<User> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "An error has occurred : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    task.getException().printStackTrace();
                }
            }
        });

    }


}
