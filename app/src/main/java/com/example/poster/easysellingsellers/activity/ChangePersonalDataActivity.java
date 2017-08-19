package com.example.poster.easysellingsellers.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.poster.easysellingsellers.R;
import com.example.poster.easysellingsellers.lisntener.MyValueEventListener;
import com.example.poster.easysellingsellers.model.UserLoginInfoTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;

/**
 * Created by User on 019 19.08.17.
 */

public class ChangePersonalDataActivity extends BaseActivity {
    @BindView(R.id.set_new_name) EditText newName;
    @BindView(R.id.set_new_last_name) EditText newLastName;
    @BindView(R.id.set_new_surname) EditText newSurName;

    private DatabaseReference reference;
    private String userUid;
    private String userKey;
    private UserLoginInfoTable userModel;

    private MyValueEventListener onUserDataListener = new MyValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ChangePersonalDataActivity.super.hideProgressDialog();
            for (DataSnapshot data: dataSnapshot.getChildren()){
                if (userUid.equals(data.getValue(UserLoginInfoTable.class).getuID())){
                    userKey = data.getKey();
                    userModel = data.getValue(UserLoginInfoTable.class);
                    setDataToEditText();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_per_data);
        super.setActivityForBinder(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference = super.database.getReference(USER_INFO_TABLE);
        userUid = super.user.getUid();
        super.showProgressDialog("Loading data ...");
        reference.addListenerForSingleValueEvent(onUserDataListener);
    }

    private void setDataToEditText() {
        newName.setText(userModel.getName());
        newLastName.setText(userModel.getLastName());
        newSurName.setText(userModel.getSurname());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accept_changes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.accept_changes_menu_btn){
            saveChanges(newName.getText().toString(), newLastName.getText().toString(), newSurName.getText().toString());
        }
        finish();
        return true;
    }

    private void saveChanges(String name, String lastName, String surname) {
        userModel.setName(name);
        userModel.setLastName(lastName);
        userModel.setSurname(surname);
        reference.child(userKey).setValue(userModel);
        super.showToast(this, "Saved");
    }
}
