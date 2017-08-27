package com.example.poster.easysellingsellers.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.poster.easysellingsellers.R;
import com.example.poster.easysellingsellers.activity.BaseActivity;
import com.example.poster.easysellingsellers.activity.CreateCompanyActivity;
import com.example.poster.easysellingsellers.event.UpdateCompanyUI;
import com.example.poster.easysellingsellers.eventbus.BusProvider;
import com.example.poster.easysellingsellers.lisntener.MyValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by POSTER on 25.08.2017.
 */

public class MyCompanyFragment extends BaseFragment {
    @BindView(R.id.my_comp_no_data_layout) RelativeLayout noDataLayout;
    @BindView(R.id.my_comp_data_layout) LinearLayout dataLayout;
    @BindView(R.id.category_and_sub_cat_container) LinearLayout catSubCatContainer;

    private View view;
    private DatabaseReference refUserInfTable;

    private MyValueEventListener onUserKeyFinder = new MyValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data: dataSnapshot.getChildren()){

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_company, container, false);
        setFragmentForBinder(this, view);
        setHasOptionsMenu(true);
        checkUserData();
        refUserInfTable = super.database.getReference(USER_INFO_TABLE);
        return view;
    }

    private void checkUserData() {
        if (BaseActivity.userModel != null){
            if (!BaseActivity.userModel.getCompanyUid().equals("default_uri")){
                noDataLayout.setVisibility(View.GONE);
                dataLayout.setVisibility(View.VISIBLE);
                initCompanyData(dataLayout);
            }else {
                noDataLayout.setVisibility(View.VISIBLE);
                dataLayout.setVisibility(View.GONE);
                catSubCatContainer.setVisibility(View.GONE);
            }
        }
    }

    private void initCompanyData(View view) {
        if (BaseActivity.companiesInfoTable != null){
            ((TextView)view.findViewById(R.id.company_name_req)).setText(BaseActivity.companiesInfoTable.getCompanyName());
            ((TextView)view.findViewById(R.id.company_desc_req)).setText(BaseActivity.companiesInfoTable.getCompanyDescr());
            ((TextView)view.findViewById(R.id.company_key_pr)).setText(createStringText(BaseActivity.companiesInfoTable.getCompanyProducts()));
            ((TextView)view.findViewById(R.id.company_my_positions)).setText("founder");
            if (!BaseActivity.companiesInfoTable.getCompanyLogoUri().toString().equals("default_uri")){
                loadImage(Uri.parse(BaseActivity.companiesInfoTable.getCompanyLogoUri()), ((ImageView)view.findViewById(R.id.company_logo)));
            }
            if (BaseActivity.companiesInfoTable.getWareHouse() == null){
                catSubCatContainer.setVisibility(View.VISIBLE);
            }else {
                catSubCatContainer.setVisibility(View.GONE);
            }
        }
    }

    private void loadImage(Uri uri, ImageView view){
        Glide.with(this).load(uri).crossFade().thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    private String createStringText(Object o){
        ArrayList<String> keys = ((ArrayList<String>) o);
        StringBuilder builder = new StringBuilder();
        for (String value : keys) builder.append(value).append(", ");
        if (keys.size()> 0) builder.deleteCharAt(builder.length() - 2);
        return builder.toString();
    }

    @OnClick(R.id.add_company_btn)
    public void addCompanyClick(){
        startCurActivity(getContext(), CreateCompanyActivity.class);
    }

    @Subscribe
    public void updateCompanyInfo(UpdateCompanyUI event){
        checkUserData();
    }

    @Produce
    public UpdateCompanyUI update(){
        return new UpdateCompanyUI();
    }



    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
