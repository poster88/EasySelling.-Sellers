package com.example.poster.easysellingsellers.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.poster.easysellingsellers.R;
import com.example.poster.easysellingsellers.activity.BaseActivity;
import com.example.poster.easysellingsellers.event.UserAddEvent;
import com.example.poster.easysellingsellers.eventbus.BusProvider;
import com.example.poster.easysellingsellers.lisntener.MyValueEventListener;
import com.example.poster.easysellingsellers.model.UserLoginInfoTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by POSTER on 27.08.2017.
 */

public class MyUsersRequestFragment extends BaseFragment{
    private DatabaseReference userRef;
    private UserLoginInfoTable userTempData;
    private View view;

    private MyValueEventListener loadUserData = new MyValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data: dataSnapshot.getChildren()) {
                if (data.getValue(UserLoginInfoTable.class).getuID().equals(BaseActivity.requestTable.getClientUid())){
                    userTempData = data.getValue(UserLoginInfoTable.class);
                    innitDataToWidgets();
                    break;
                }
            }
        }
    };

    private void innitDataToWidgets() {
        ((ProgressBar)view.findViewById(R.id.progressBarRequest)).setVisibility(View.GONE);
        ((LinearLayout)view.findViewById(R.id.request_info_container)).setVisibility(View.VISIBLE);
        ((TextView)view.findViewById(R.id.request_user_name)).setText(userTempData.getName());
        ((TextView)view.findViewById(R.id.request_user_number)).setText(userTempData.getMobileNumber() + "");
        ((TextView)view.findViewById(R.id.request_user_position)).setText(BaseActivity.requestTable.getPosition());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request_add_to_company, container, false);
        setFragmentForBinder(this, view);
        userRef = database.getReference(USER_INFO_TABLE);
        if (BaseActivity.requestTable != null){
            userRef.addListenerForSingleValueEvent(loadUserData);
            ((ProgressBar)view.findViewById(R.id.progressBarRequest)).setVisibility(View.VISIBLE);
        }else {
            ((ProgressBar)view.findViewById(R.id.progressBarRequest)).setVisibility(View.GONE);
        }
        ((Button)view.findViewById(R.id.request_back_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserRequest();
                ((LinearLayout)view.findViewById(R.id.request_info_container)).setVisibility(View.INVISIBLE);
            }
        });
        ((Button)view.findViewById(R.id.request_next_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserCompanyID();
                ((LinearLayout)view.findViewById(R.id.request_info_container)).setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    private void deleteUserRequest() {
        DatabaseReference reference = super.database.getReference(REQUEST_TABLE).child(BaseActivity.requestTable.getRequestId());
        BaseActivity.requestTable = null;
        reference.removeValue();
        BusProvider.getInstance().post(new UserAddEvent());
    }


    private void updateUserCompanyID(){
        userTempData.setCompanyUid(BaseActivity.requestTable.getCompanyId());
        DatabaseReference userPosRef = database.getReferenceFromUrl(
                database.getReference(COMP_INF_TABLE) + "/" + BaseActivity.requestTable.getCompanyId() + "/positions/" + BaseActivity.requestTable.getPosition()
        );
        List<String> peoples = new ArrayList<>();
        peoples.add(BaseActivity.requestTable.getClientUid());
        userPosRef.setValue(peoples);
        userRef.addListenerForSingleValueEvent(onUserKeyFinder);
    }

    private void updateUserProfile(String userKey) {
        userRef.child(userKey).setValue(userTempData);
        deleteUserRequest();
    }

    private MyValueEventListener onUserKeyFinder = new MyValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data: dataSnapshot.getChildren()){
                if (userTempData.getuID().equals(data.getValue(UserLoginInfoTable.class).getuID())){
                    updateUserProfile(data.getKey());
                    break;
                }
            }
        }
    };
}
