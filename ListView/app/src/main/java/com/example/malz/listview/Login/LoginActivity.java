package com.example.malz.listview.Login;

/**
 * Created by malz on 7/14/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malz.listview.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText userNameET;
    EditText passwordET;
    EditText uEmail;
    EditText uPass;
    String mName;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    /* String Constants */
    private static final String EMAIL_INVALID =  "email is invalid :";
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get the shared instance of the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Set up an AuthStateListener that responds to changes in the user's sign-in state
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //Get reference to NEW email and password edittexts
        userNameET = (EditText)findViewById(R.id.edit_text_email);
        passwordET = (EditText)findViewById(R.id.edit_text_password);

        //Get reference to EXISTING email and password edittexts
        uEmail= (EditText)findViewById(R.id.editTextEmail);
        uPass= (EditText)findViewById(R.id.editTextPassword);

        //Button for existing account login
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginActivity.this.showFirebaseLoginPrompt();
                signIn(uEmail.getText().toString(), uPass.getText().toString());

            }
        });

        //Button for new account login
        Button createButton = (Button) findViewById(R.id.button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAUser(userNameET.getText().toString(),passwordET.getText().toString());
            }
        });
    }

    //Method for signing in
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(getApplicationContext(), "signInWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "signInWithEmail------:onComplete:" + task.isSuccessful());
                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(myIntent);
                        }

                    }
                });
    }


    //Method for creating a new user
    private void createAUser(String email, String password){
        if(userNameET.getText() == null ||  !isEmailValid(userNameET.getText().toString())) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        Toast.makeText(LoginActivity.this,"createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG)
                                .show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }


}
