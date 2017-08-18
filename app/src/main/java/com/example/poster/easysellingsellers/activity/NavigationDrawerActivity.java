package com.example.poster.easysellingsellers.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.poster.easysellingsellers.R;
import com.example.poster.easysellingsellers.event.UpdateCompanyUI;
import com.example.poster.easysellingsellers.event.UpdateUIEvent;
import com.example.poster.easysellingsellers.eventbus.BusProvider;
import com.example.poster.easysellingsellers.utils.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 017 17.08.17.
 */

public class NavigationDrawerActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private View navHeader;
    private Handler handler;
    private Runnable pendingRunnable = new Runnable() {
    @Override
    public void run() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, getHomeFragment(), CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        }
    };

    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        setActivityForBinder(this);
        setSupportActionBar(toolbar);
        handler = new Handler();
        navHeader = navigationView.getHeaderView(0);
        loadNavHeader();
        setUpNavigationView();
        checkSaveInstanceState(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        userRef = super.database.getReference(USER_INFO_TABLE).orderByChild("uID").equalTo(super.user.getUid());
        }

    @Subscribe
    public void updateUI(UpdateUIEvent event) {
        TextView userName = ButterKnife.findById(navHeader, R.id.name);
        TextView userEmail = ButterKnife.findById(navHeader, R.id.email);
        ImageView userPhoto = ButterKnife.findById(navHeader, R.id.img_profile);
        ProgressBar progressBar = ButterKnife.findById(navHeader, R.id.progressBar);
        userName.setText(userModel.getName());
        userEmail.setText(userModel.getEmail());
        if (!userModel.getPhotoUrl().equals("default_uri")) {
            progressBar.setProgress(getTaskId());
            Glide.with(getApplicationContext())
                    .load(Uri.parse(NavigationDrawerActivity.userModel.getPhotoUrl()))
                    .crossFade().thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(userPhoto);
        }
        if (!userModel.getCompanyUid().equals("default_uri")) {
            refCompanyTable = database.getReference(COMP_INF_TABLE).child(userModel.getCompanyUid());
            refCompanyTable.addValueEventListener(onCompanyInfoTableListener);
        } else {
            companiesInfoTable = null;
            refCompanyTable = null;
            BusProvider.getInstance().post(new UpdateCompanyUI());
        }
    }

    private void checkSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            CURRENT_TAG = TAG_HOME;
            /*loadHomeFragment();*/
        }
    }

    private void loadNavHeader() {
        if (super.user != null && super.user.isAnonymous()) {
            innitWidgets();
        } else {
            TextView logOut = ButterKnife.findById(navHeader, R.id.log_out);
            logOut.setVisibility(View.VISIBLE);
            logOut.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_in_form_nav){
            NavigationDrawerActivity.super.startCurActivity(NavigationDrawerActivity.this, RegistrationActivity.class);
            finish();
            return;
        }else if (v.getId() == R.id.log_out){
            FirebaseAuth.getInstance().signOut();
        }
        NavigationDrawerActivity.super.startCurActivity(NavigationDrawerActivity.this, LoginActivity.class);
        finish();
    }

    private void innitWidgets() {
        navHeader.findViewById(R.id.reg_buttons_container).setVisibility(View.VISIBLE);
        navHeader.findViewById(R.id.name).setVisibility(View.GONE);
        navHeader.findViewById(R.id.email).setVisibility(View.GONE);
        navHeader.findViewById(R.id.sign_in_form_nav).setOnClickListener(this);
        navHeader.findViewById(R.id.reg_in_form_nav).setOnClickListener(this);
    }

    private void loadHomeFragment(){
        getSupportActionBar().setTitle(CURRENT_TAG);
        /*pendingRunnable.run();
        if (pendingRunnable != null){
            handler.post(pendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();*/
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        innitToggle();
    }

    private void innitToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (!CURRENT_TAG.equals(TAG_HOME)) {
            CURRENT_TAG = TAG_HOME;
            /*loadHomeFragment();*/
            return;
        }
        if (!super.user.isAnonymous()){
            NavigationDrawerActivity.super.exitProgram();
            return;
        }
        super.startCurActivity(this, LoginActivity.class);
        finish();
    }

    private Fragment getHomeFragment(){
        Fragment fragment = null;
        /*if (CURRENT_TAG.equals(TAG_HOME)){
            fragment = new ProductCatalogFragment();
        }
        if (CURRENT_TAG.equals(TAG_ACCOUNT)){
            fragment = new MyAccountFragment();
        }
        if (CURRENT_TAG.equals(TAG_ORDER)){
            fragment = new MyOrdersFragment();
        }
        if (CURRENT_TAG.equals(TAG_CHAT)){
            fragment = new CompanyChatFragment();
        }
        if (CURRENT_TAG.equals(TAG_FAVORITE)){
            fragment = new WishListFragment();
        }
        if (CURRENT_TAG.equals(TAG_NEWS)){
            fragment = new NewsFragment();
        }
        if (CURRENT_TAG.equals(TAG_INFORMATION)){
            fragment = new InformationFragment();
        }*/
        return fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*if (item.getItemId() == R.id.nav_catalog_items) {
            CURRENT_TAG = TAG_HOME;
        }
        if (item.getItemId() == R.id.nav_account) {
            CURRENT_TAG = TAG_ACCOUNT;
        }
        if (item.getItemId() == R.id.nav_order) {
            CURRENT_TAG = TAG_ORDER;
        }
        if (item.getItemId() == R.id.nav_messages) {
            CURRENT_TAG = TAG_CHAT;
        }
        if (item.getItemId() == R.id.nav_wish_list) {
            CURRENT_TAG = TAG_FAVORITE;
        }
        if (item.getItemId() == R.id.nav_news) {
            CURRENT_TAG = TAG_NEWS;
        }
        if (item.getItemId() == R.id.nav_tech_support) {
            CURRENT_TAG = TAG_INFORMATION;
        }
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }*/
        loadHomeFragment();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (super.user != null && !super.user.isAnonymous()){
            BusProvider.getInstance().register(this);
            super.auth.addAuthStateListener(authStateListener);
            if (userRef != null){
                userRef.addValueEventListener(onUidUserDataListener);
            }
            if (refCompanyTable != null){
                refCompanyTable.addValueEventListener(onCompanyInfoTableListener);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (super.user != null && !super.user.isAnonymous()){
            BusProvider.getInstance().unregister(this);
            super.auth.removeAuthStateListener(authStateListener);
            if (onUidUserDataListener != null && userRef != null){
                userRef.removeEventListener(onUidUserDataListener);
            }
            if (onCompanyInfoTableListener != null && refCompanyTable != null){
                refCompanyTable.removeEventListener(onCompanyInfoTableListener);
            }
        }
    }
}
