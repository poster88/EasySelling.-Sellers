package com.example.poster.easysellingsellers.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;

import com.example.poster.easysellingsellers.R;
import com.example.poster.easysellingsellers.model.UserLoginInfoTable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 017 17.08.17.
 */

public class RegistrationActivity extends BaseActivity{
    @BindView(R.id.reg_name) EditText name;
    @BindView(R.id.reg_last_name) EditText lastName;
    @BindView(R.id.reg_sur_name) EditText surName;
    @BindView(R.id.reg_mob_num) EditText number;
    @BindView(R.id.reg_email) EditText email;
    @BindView(R.id.reg_password) EditText password;
    @BindView(R.id.reg_rep_pass) EditText repeatPass;

    @BindView(R.id.layout_name) TextInputLayout inputLayoutName;
    @BindView(R.id.layout_last_name) TextInputLayout inputLayoutLastName;
    @BindView(R.id.layout_sur_name) TextInputLayout inputLayoutSurName;
    @BindView(R.id.layout_mob_num) TextInputLayout inputLayoutMobNum;
    @BindView(R.id.layout_email) TextInputLayout inputLayoutEmail;
    @BindView(R.id.layout_password) TextInputLayout inputLayoutPass;
    @BindView(R.id.layout_rep_pass) TextInputLayout inputLayoutRepPass;

    private DatabaseReference reference;
    private UserLoginInfoTable usersInfoTable;
    private OnCompleteListener onCompleteListenerCreateUser = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            hideProgressDialog();
            if (task.isSuccessful()) {
                addUser();
                RegistrationActivity.super.showToast(RegistrationActivity.this, "User account is created");
                onBackPressed();
                return;
            }
            inputLayoutEmail.setError(task.getException().getMessage());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registaration);
        super.setActivityForBinder(this);
        reference = super.database.getReference(USER_INFO_TABLE);
    }

    private void addUser(){
        String id = reference.push().getKey();
        setDataToConstructor();
        Map<String, Object> userLoginInfo = usersInfoTable.toMap();
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put(id, userLoginInfo);
        reference.updateChildren(userAttributes);
    }

    private void setDataToConstructor() {
        usersInfoTable = new UserLoginInfoTable(
                name.getText().toString(), lastName.getText().toString(), surName.getText().toString(),
                "default_uri", Long.parseLong(number.getText().toString()), email.getText().toString(),
                super.auth.getCurrentUser().getUid(), "default_uri");
    }

    private void submitForm() {
        if (!super.isValidPersonalData(name, inputLayoutName) || !super.isValidPersonalData(lastName, inputLayoutLastName) ||
                !super.isValidPersonalData(surName, inputLayoutSurName) || !super.isValidNumber(number, inputLayoutMobNum) ||
                !super.isValidEmail(email, inputLayoutEmail) || !super.isValidPassword(password, inputLayoutPass, repeatPass, inputLayoutRepPass)) {
            return;
        }
        createAccount(email.getText().toString(), password.getText().toString());
    }

    private void createAccount(String email, String password) {
        super.showProgressDialog("Registration...");
        super.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListenerCreateUser);
    }

    @OnClick({R.id.reg_back_btn, R.id.reg_next_btn})
    public void pickAction(Button button){
        if (button.getId() == R.id.reg_next_btn){
            submitForm();
            return;
        }
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        RegistrationActivity.super.startCurActivity(RegistrationActivity.this, LoginActivity.class);
        finish();
    }
}
