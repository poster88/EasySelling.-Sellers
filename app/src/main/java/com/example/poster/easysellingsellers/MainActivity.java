package com.example.poster.easysellingsellers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.loginEdit) EditText login;
    @BindView(R.id.passEdit) EditText password;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        }

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (auth != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @OnClick(R.id.loginBtn)
    public void loginClick(){
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        auth.createUserWithEmailAndPassword(login.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = auth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, MenuActivity.class));
                        }else {
                            System.out.println(task.getException());
                        }
                    }
                });
        auth.signInWithEmailAndPassword(login.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = auth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, MenuActivity.class));
                        }else {
                            System.out.println(task.getException());
                        }
                    }
                });
    }


}
