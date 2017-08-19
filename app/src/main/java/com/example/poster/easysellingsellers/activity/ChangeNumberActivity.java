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

public class ChangeNumberActivity extends BaseActivity {
    @BindView(R.id.set_new_number) EditText numberEdit;

    private DatabaseReference reference;
    private String userUid;
    private String userKey;
    private MyValueEventListener onNumberChangeListener = new MyValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ChangeNumberActivity.super.hideProgressDialog();
            for (DataSnapshot data: dataSnapshot.getChildren()) {
                if (userUid.equals(data.getValue(UserLoginInfoTable.class).getuID())){
                    userKey = data.getKey();
                    numberEdit.setText(String.valueOf(userModel.getMobileNumber()));
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);
        super.setActivityForBinder(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference = super.database.getReference(USER_INFO_TABLE);
        userUid = super.user.getUid();
        super.showProgressDialog("Loading data ... ");
        reference.addListenerForSingleValueEvent(onNumberChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.accept_changes_menu_btn){
            changeNumber(Integer.valueOf(numberEdit.getText().toString()), userKey, userModel);
        }
        finish();
        return true;
    }

    private void changeNumber(long number, String key, UserLoginInfoTable model){
        model.setMobileNumber(number);
        reference.child(key).setValue(model);
        super.showToast(this, "Saved");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accept_changes, menu);
        return true;
    }
}
