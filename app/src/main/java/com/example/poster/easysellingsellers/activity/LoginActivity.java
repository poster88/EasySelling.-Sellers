package com.example.poster.easysellingsellers.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poster.easysellingsellers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 017 17.08.17.
 */

public class LoginActivity extends BaseActivity{
    @BindView(R.id.inputTextEmail) TextInputLayout inputEmail;
    @BindView(R.id.emailEdit) EditText emailEdit;
    @BindView(R.id.inputTextPass) TextInputLayout inputPass;
    @BindView(R.id.passEdit) EditText passEdit;
    @BindView(R.id.logo_image) ImageView logoImage;


    private OnCompleteListener onCompleteListenerSignIn = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            LoginActivity.super.hideProgressDialog();
            if (task.isSuccessful()){
                startNavActivity();
                return;
            }
            LoginActivity.super.showToast(LoginActivity.this, "Authentication failed. "  + task.getException().getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        super.setActivityForBinder(this);
        checkUserData();
    }

    private void checkUserData(){
        if (super.user != null && !super.user.isAnonymous()){
            startNavActivity();
        }
    }

    private void startNavActivity(){
        super.startCurActivity(this, NavigationDrawerActivity.class);
        finish();
    }

    private void signIn() {
        if (super.isValidEmail(emailEdit, inputEmail) && super.checkLengthPass(passEdit, inputPass)){
            super.showProgressDialog("Loading...");
            super.auth.signInWithEmailAndPassword(emailEdit.getText().toString(), passEdit.getText().toString())
                    .addOnCompleteListener(onCompleteListenerSignIn);
        }
    }

    @OnClick(R.id.skipImgBtn)
    public void anonymouslySingIn(){
        super.auth.signInAnonymously().addOnCompleteListener(onCompleteListenerSignIn);
    }

    @OnClick({R.id.sign_in_btn, R.id.registration_btn})
    public void pickAction(TextView textView){
        if (textView.getId() == R.id.sign_in_btn){
            signIn();
        }else {
            LoginActivity.super.startCurActivity(this, RegistrationActivity.class);
        }
    }

    @OnClick(R.id.sent_pass_on_email)
    public void sendPassResetEmail() {
        /*super.startCurActivity(this, SentPassToEmailActivity.class);*/
    }

    @Override
    public void onBackPressed() {
        super.exitProgram();
    }
}
