/*
Mobile App Development - W01
Team 5
Summer 2020
Semester Project - Dashyy

Any use of the following code is forbidden without prior consent.
*/

package CS7455.Team5.dashyy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StartupActivity extends AppCompatActivity {

    //Class Variables
    private boolean loginMode = true;


    //Component Variables
    TextView signupPB;
    EditText nameInput, emailInput, passwordInput, passwordConfirmInput;
    Button loginPB;


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


        /* STARTUP LOGIC */
        //TODO: Startup Cycle Logic (animations, loading, etc)

        //TODO: Enable Default Login items visibility once startup complete

    }

    //LOGIN:
    public void LoginClick (View view)
    {
        //True Login
        if (loginMode)
        {
            //TODO: Actual Login, just need temp access to screens for dev
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

        }
        //Sign Up
        else
        {

        }
    }

    //SIGNUP:
    public void SignupClick (View view)
    {
        if (loginMode)
        {
            //Show Fields & Change Name
            nameInput.setVisibility(View.VISIBLE);
            passwordConfirmInput.setVisibility(View.VISIBLE);
            loginPB.setText("SIGN UP");
            signupPB.setText("Log In");

            //Set Login Mode
            loginMode = false;
        }
        else
        {
            //Show Fields & Change Name
            nameInput.setVisibility(View.INVISIBLE);
            passwordConfirmInput.setVisibility(View.INVISIBLE);
            loginPB.setText("LOG IN");
            signupPB.setText("Sign Up");

            //Set Login Mode
            loginMode = true;
        }
    }

    //FORGOT PASSWORD:
    private void ForgotPasswordClick(View view)
    {
        //TODO: Transfer to something else to reset password (IF WE WANT TO DO THAT IDK)

    }
}
