package com.example.poster.easysellingsellers.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.poster.easysellingsellers.R;
import com.example.poster.easysellingsellers.event.UpdateCompanyUI;
import com.example.poster.easysellingsellers.eventbus.BusProvider;
import com.example.poster.easysellingsellers.lisntener.MyValueEventListener;
import com.example.poster.easysellingsellers.model.CompaniesInfoTable;
import com.example.poster.easysellingsellers.model.UserLoginInfoTable;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by POSTER on 25.08.2017.
 */

public class CreateCompanyActivity extends BaseActivity {
    @BindView(R.id.container_key_pr) LinearLayout keysProductContainer;
    @BindView(R.id.productName) EditText productName;
    @BindView(R.id.company_create_my_pos_container) LinearLayout positionsContainer;
    @BindView(R.id.position_name) EditText positionName;
    @BindView(R.id.company_create_logo) ImageView companyImage;
    @BindView(R.id.company_create_name_edit) EditText companyName;
    @BindView(R.id.company_create_descr_edit) EditText companyDescr;
    private List<String> productKeys;
    private Map<String, Object> positions;
    private Uri photoUri;
    private String dounloadUrl = "dafault_uri";
    private String companyID;
    private StorageReference storageRef;
    private DatabaseReference companyRef;
    private DatabaseReference refUserInfTable;

    private static final int PHOTO_REQUEST = 9002;
    private static final int REQUEST_READ_PERMISSION = 9003;
    private MyValueEventListener onUserKeyFinder = new MyValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data: dataSnapshot.getChildren()){
                if (user.getUid().equals(data.getValue(UserLoginInfoTable.class).getuID())){
                    updateUserProfile(data.getKey());
                    BusProvider.getInstance().post(new UpdateCompanyUI());
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);
        super.setActivityForBinder(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageRef = FirebaseStorage.getInstance().getReference("companies_logos");
        companyRef = super.database.getReference(COMP_INF_TABLE);
        refUserInfTable = super.database.getReference(USER_INFO_TABLE);
    }

    private void updateUserProfile(String userKey) {
        UserLoginInfoTable userModel = BaseActivity.userModel;
        userModel.setCompanyUid(companyID);
        refUserInfTable.child(userKey).setValue(userModel);
    }

    @OnClick(R.id.add_line_image)
    public void addKeyLine(){
        View view = getLayoutInflater().inflate(R.layout.key_prod_line, null);
        keysProductContainer.addView(view);
    }

    @OnClick(R.id.add_position_image)
    public void addPosition(){
        View view = getLayoutInflater().inflate(R.layout.key_prod_line, null);
        positionsContainer.addView(view);
    }

    @OnClick(R.id.company_create_btn)
    public void createCompanyClick(){
        innitPossitionsHashMap();
        innitKeyProductsList();
        innitCompanyData();
        finish();
    }

    private void innitCompanyData() {
        companyID = companyRef.push().getKey();
        CompaniesInfoTable companyInfoTable = new CompaniesInfoTable();
        companyInfoTable.setcompanyId(companyID);
        companyInfoTable.setCompanyName(companyName.getText().toString());
        companyInfoTable.setCompanyDescr(companyDescr.getText().toString());
        companyInfoTable.setCompanyLogoUri(dounloadUrl);
        companyInfoTable.setCompanyProducts(productKeys);
        companyInfoTable.setPositions(positions);
        companyInfoTable.setWareHouse(null);
        Map<String, Object> newTable = new HashMap<>();
        Map<String, Object> tempTable = companyInfoTable.toMap();
        newTable.put(companyID, tempTable);
        companyRef.updateChildren(newTable);
        refUserInfTable.addListenerForSingleValueEvent(onUserKeyFinder);
        showToast(CreateCompanyActivity.this, "Company created");
    }

    @OnClick(R.id.company_photo_edit)
    public void editCompanyPhoto(){
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(CreateCompanyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateCompanyActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
            }else {
                openFilePicker();
            }
        }else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openFilePicker();
            }else {
                Toast.makeText(this, "Cannot pick file from storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK && data != null){
            photoUri = data.getData();
            Glide.with(CreateCompanyActivity.this).load(photoUri).into(companyImage);
            savePhotoToStorage();
        }
    }

    private void savePhotoToStorage(){
        final StorageReference onlineStoragePhotoRef = storageRef.child(photoUri.getLastPathSegment());
        onlineStoragePhotoRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dounloadUrl = taskSnapshot.getDownloadUrl().toString();
            }
        });
    }

    private void innitKeyProductsList() {
        productKeys = new ArrayList<>();
        if (productName.getText().length() != 0){
            productKeys.add(productName.getText().toString());
        }
        for (int index = 1; index < keysProductContainer.getChildCount(); index++) {
            View view = keysProductContainer.getChildAt(index);
            EditText productName = ButterKnife.findById(view, R.id.product_name_in_line);
            if (productName.getText().length() != 0){
                productKeys.add(productName.getText().toString());
            }
        }
    }

    private void innitPossitionsHashMap(){
        positions = new HashMap<>();
        positions.put("founder", super.user.getUid());
        if (positionName.getText().length() != 0){
            positions.put(positionName.getText().toString(), null);
        }
        List<String> peoples = new ArrayList<>();
        peoples.add("default_uri");
        for (int index = 1; index < positionsContainer.getChildCount(); index++) {
            View view = positionsContainer.getChildAt(index);
            EditText positionName = ButterKnife.findById(view, R.id.product_name_in_line);
            if (positionName.getText().length() != 0){
                positions.put(positionName.getText().toString(), peoples);
            }
        }
    }
}
