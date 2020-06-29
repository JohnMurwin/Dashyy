/*
Mobile App Development - W01
Team 5
Summer 2020
Semester Project - Dashyy

Any use of the following code is forbidden without prior consent.
*/

package CS7455.Team5.dashyy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class StartupActivity extends AppCompatActivity {

    //Class Variables
    private boolean loginMode = true;
    private FirebaseAuth firebaseAuth;


    //Component Variables
    EditText nameInput, emailInput, passwordInput, passwordConfirmInput;
    Button loginPB, signupPB, forgotPWPB;
    TextInputLayout nameTIL, passwordConfirmTIL;
    LinearLayout inputLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //Component Setup
        signupPB = findViewById(R.id.signup_PB);

        nameInput = findViewById(R.id.name_ET);
        emailInput = findViewById(R.id.email_ET);
        passwordInput = findViewById(R.id.password_ET);
        passwordConfirmInput = findViewById(R.id.passwordConfirm_ET);

        loginPB = findViewById(R.id.login_PB);
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        nameTIL = findViewById(R.id.name_TIL);
        passwordConfirmTIL = findViewById(R.id.passwordConfirm_TIL);

        inputLL = findViewById(R.id.Input_LinearLayout);


        /* STARTUP LOGIC */
        //TODO: Startup Cycle Logic (animations, loading, etc)

        //TODO: Enable Default Login items visibility once startup complete

    }

    //LOGIN:
    public void LoginClick (View view)
    {
        String email = emailInput.getText().toString().toLowerCase();
        String pass = passwordInput.getText().toString();
        final String displayName = nameInput.getText().toString();
        final Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        //True Login
        if (loginMode)
        {
            if(Strings.isEmptyOrWhitespace(email) ||  Strings.isEmptyOrWhitespace(pass)) {
                Toast.makeText(getApplicationContext(), "Email and Password cannot be empty",Toast.LENGTH_LONG).show();
            }
            else {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(StartupActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(intent);
                                } else {
                                    // TODO: # Failed attempts "Lock account"?
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        //Sign Up
        else {
            String confirmPass = passwordConfirmInput.getText().toString();
            String accountCreateMessage = "";
            if(Strings.isEmptyOrWhitespace(email) ||  Strings.isEmptyOrWhitespace(pass) ||  Strings.isEmptyOrWhitespace(displayName) ||  Strings.isEmptyOrWhitespace(confirmPass)) {
                accountCreateMessage= "All form fields must be completed.";
            }
            else if(!confirmPass.equals(pass)){
                accountCreateMessage = "Password does not match confirm password";
            }
            else {// use firebaseAuth instance to create user
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    // Update user's Name
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(displayName).build();
                                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.accountCreatedText), Toast.LENGTH_LONG).show();
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user
                                    // TODO: Check if email already exists
                                    Toast.makeText(getApplicationContext(), "Auth Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
            if(!Strings.isEmptyOrWhitespace(accountCreateMessage))
                Toast.makeText(getApplicationContext(), accountCreateMessage, Toast.LENGTH_LONG).show();
        }
    }

    //SIGNUP:
    public void SignupClick (View view)
    {
        TransitionManager.beginDelayedTransition(inputLL);

        if (loginMode)
        {
            //Show Fields & Change Name
            nameTIL.setVisibility(View.VISIBLE);
            passwordConfirmTIL.setVisibility(View.VISIBLE);
            loginPB.setText("SIGN UP");
            signupPB.setText("Log In");

            //Set Login Mode
            loginMode = false;
        }
        else
        {
            //Show Fields & Change Name
            nameTIL.setVisibility(View.INVISIBLE);
            passwordConfirmTIL.setVisibility(View.INVISIBLE);
            loginPB.setText("LOG IN");
            signupPB.setText("Sign Up");

            //Set Login Mode
            loginMode = true;
        }
    }

    //FORGOT PASSWORD:
    public void ForgotPasswordClick(View view) {
        String email = emailInput.getText().toString().toLowerCase();
        if(email == ""){
            Toast.makeText(getApplicationContext(), "Please enter a valid email address in the 'EMAIL' field", Toast.LENGTH_LONG).show();
        }
        else{
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(getApplicationContext(), "Email Sent.", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
