package com.acushlakoncept.marketmoni;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextLoginEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private AgentDatabaseHelper databaseHelper;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        session = new SessionManagement(activity);

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        initViews();
        initListerners();
        initObjects();
    }

    // THis method is to initialize views
    private void initViews() {

        nestedScrollView = (NestedScrollView)findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout)findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout)findViewById(R.id.textInputLayoutPassword);

        textInputEditTextLoginEmail = (TextInputEditText)findViewById(R.id.textInputEditTextLoginEmail);
        textInputEditTextPassword = (TextInputEditText)findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton)findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView)findViewById(R.id.textViewLinkRegister);
    }

    // This method is to initialize listeners
    private void initListerners(){
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    // This method is to initialize objects to be used
    private void initObjects() {
        databaseHelper = new AgentDatabaseHelper((activity));
        inputValidation = new InputValidation(activity);
    }

    // This implemented method is to listen the click on view
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Open Register Activity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextLoginEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextLoginEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        String userEmail = textInputEditTextLoginEmail.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();



        if (databaseHelper.doesDatabaseExist(activity)){
            if (databaseHelper.checkAgent(userEmail, password)) {

                Intent accountsIntent = new Intent(activity, UserSummary.class);
                accountsIntent.putExtra("EMAIL", userEmail);
                //accountsIntent.putExtra("PWD", password);

//            session.createLoginSession(userEmail);

                emptyInputEditText();
                startActivity(accountsIntent);
                finish();

            } else {
                // Snack Bar to show success message that record is wrong
                Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
                //Toast.makeText(activity, getString(R.string.error_valid_email_password), Toast.LENGTH_SHORT).show();
                // username / password doesn't match
                alert.showAlertDialog(activity, "Login failed..", "Username/Password is incorrect", false);
            }
        } else {
            alert.showAlertDialog(activity, "No Account", "Try creating an account first", false);
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextLoginEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}

