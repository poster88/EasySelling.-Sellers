package com.example.poster.easysellingsellers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.poster.easysellingsellers.model.CompaniesInfoTable;
import com.example.poster.easysellingsellers.model.Product;
import com.example.poster.easysellingsellers.model.UploadPhotoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
 * Created by POSTER on 13.07.2017.
 */

public class MenuActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    @BindView(R.id.userUidText) TextView userInfo;
    @BindView(R.id.userPhoto) ImageView userImage;

    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private FirebaseDatabase database;
    private DatabaseReference refCompInfTable;

    private StorageReference storageRef;

    private static final int PHOTO_REQUEST = 9002;
    private static final int REQUEST_READ_PERMISSION = 9003;

    private Uri photoUri;
    private String photoUrl;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        refCompInfTable = database.getReference("CompaniesInfoTable");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userInfo.setText(user.getUid());
        storageRef = FirebaseStorage.getInstance().getReference("users_images");
    }

    @OnClick(R.id.generateCompanyBtn)
    public void genCompany(){
        String id = refCompInfTable.push().getKey();
        List<String> productKeys = new ArrayList<>();
        productKeys.add("Тренажери та фітнес");
        productKeys.add("велосипеди");
        productKeys.add("спортивне харчування");

        List<String> peoples = new ArrayList<>();
        peoples.add("default_uri");
        peoples.add("default_uri");
        peoples.add("default_uri");
        peoples.add("default_uri");

        Map<String, Object> positions = new HashMap<>();
        positions.put("founder", "default_uri");
        positions.put("Manager", peoples);
        positions.put("HR", peoples);
        positions.put("accountant", peoples);
        positions.put("worker", peoples);
        positions.put("seller", peoples);

        Product product1 = new Product();
        product1.setId(refCompInfTable.push().getKey());
        product1.setName("Велосипед Cronus Rover");
        product1.setDescription("Розмір рами: 21, Клас: Гірський, Кількість швидкостей: 24, Тип: Хардтейл");
        product1.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2Fcronus_rover_310_21_26_white_00201_images_1451219364.jpg?alt=media&token=27d4bdaf-564b-42dd-b0b7-90cf0b648376");
        product1.setPrice(9550.0);
        product1.setAvailability(true);
        product1.setCount(100);

        Product product2 = new Product();
        product2.setId(refCompInfTable.push().getKey());
        product2.setName("Велосипед Rocky Mountain Fusion");
        product2.setDescription("Велосипед Rocky Mountain Fusion 910 — найнер повсякденного рівня для катання слабопересіченою місцевістю. Спрощена алюмінієва рама з гідроформінгом на колесах 29, допоможе проходити складніші ділянки, ніж класичні велосипеди, на 26.");
        product2.setPrice(12750.0);
        product2.setColor("black");
        product2.setAvailability(true);
        product2.setCount(66);
        product2.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2Fcopy_rocky_mountain_770416203059_593fca91d0253_images_2045490414.jpg?alt=media&token=e7c3712f-e57e-4958-8403-8ffed825421e");

        Product product3 = new Product();
        product3.setId(refCompInfTable.push().getKey());
        product3.setName("Велосипед Titan Десна 16");
        product3.setDescription("Применять велосипед желательно на ровном асфальтированном или грунтовом покрытии. Ездить на нем идеально именно в городских условиях.");
        product3.setPrice(3000.0);
        product3.setColor("white");
        product3.setAvailability(true);
        product3.setCount(512);
        product3.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2Ftitan_wns315_images_1847811398.jpg?alt=media&token=26e1c5ec-dd9a-465d-ad69-7a3068a7d10b");

        List<Object> velosipedu = new ArrayList<>();
        velosipedu.add(product1.toMap());
        velosipedu.add(product2.toMap());
        velosipedu.add(product3.toMap());


        Product product21 = new Product();
        product21.setId(refCompInfTable.push().getKey());
        product21.setName("Орбітрек EnergyFIT BE2200S");
        product21.setDescription("Тренажёр эллиптический EnergyFIT BЕ2200S относится к числу кардиотренажеров, которые тренируют основные группы мышц, повышают выносливость организма и развивают дыхательную систему.");
        product21.setPrice(7500.0);
        product21.setAvailability(true);
        product21.setCount(24);
        product21.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2Fenergyfit_be2200s_images_182330857.jpg?alt=media&token=8a809c24-fac8-4c26-83ca-a46c29f49ae8");

        List<Object> fitnes = new ArrayList<>();
        fitnes.add(product21.toMap());

        Product product31 = new Product();
        product31.setId(refCompInfTable.push().getKey());
        product31.setName("Креатин Креатин Optimum Nutrition");
        product31.setDescription("Creatine Powder – это пищевая добавка для атлетов, как начинающих, так и для активно занимающихся. Согласно результатам исследований креатина моногидрата при его употреблении у спортсменов наблюдается увеличение силы, выносливости, а также повышение пика предельных нагрузок.");
        product31.setPrice(360.0);
        product31.setAvailability(true);
        product31.setCount(2061);
        product31.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2FOPN-02384-10.jpg?alt=media&token=fc34dc29-f6bf-4b13-ae19-80493ecb5e98");

        Product product32 = new Product();
        product32.setId(refCompInfTable.push().getKey());
        product32.setName("Креатин BT 100% Creatine Monohydrate банка - 500g");
        product32.setDescription("Creatine Monohydrate является наиболее популярной и употребляемой спортивной пищевой добавкой в мире. Он способен оказывать сильное влияние на рост мышечной массы и ее силы. Наибольшее преимущество креатина заключается в его способности воздействовать на организм атлета: при восстановлении сил одновременно увеличивается мощь и сила.");
        product32.setPrice(240.0);
        product32.setAvailability(true);
        product32.setCount(4061);
        product32.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2F752852000_w200_h200_cid2718885_pid517635865-f7f92b50.jpg?alt=media&token=f2f6b82c-de3a-494e-8dae-adddde751cca");

        Product product33 = new Product();
        product33.setId(refCompInfTable.push().getKey());
        product33.setName("Протеїн SN 100% Whey Protein Prof 2350 г Vanilla Very Berry");
        product33.setDescription("100% Whey Protein Professional Scitec Nutrition – высококачественный концентрат ультрафильтрованного сывороточного протеина.");
        product33.setPrice(1300.0);
        product33.setAvailability(true);
        product33.setCount(5701);
        product33.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fgreen_world%2F91nPclrG47L._SY450_.jpg?alt=media&token=ac7f844a-a1b1-4cd1-a852-1fd1068ad8f5");

        List<Object> sportpit = new ArrayList<>();
        sportpit.add(product31.toMap());
        sportpit.add(product32.toMap());
        sportpit.add(product33.toMap());


        Map<String, Object> subCategories = new HashMap<>();
        subCategories.put("Велосипеди", velosipedu);
        subCategories.put("Тренажери та фітнес", fitnes);
        subCategories.put("спортивне харчування", sportpit);

        Map<String, Object> categories = new HashMap<>();
        categories.put("Спорт", subCategories);

        CompaniesInfoTable companyInfoTable = new CompaniesInfoTable(
                id,
                "GreenWorld",
                "Товари для спорту",
                "https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/companies_logos%2FgreenWorld.png?alt=media&token=1ce0ef26-26d7-448c-b04a-84c976ca3920",
                productKeys,
                positions,
                categories
        );

        Map<String, Object> newTable = new HashMap<>();
        Map<String, Object> tempTable = companyInfoTable.toMap();
        newTable.put(id, tempTable);
        refCompInfTable.updateChildren(newTable);
    }

    @OnClick(R.id.select_photo)
    public void selectPhoto(){
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
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
            Glide.with(MenuActivity.this).load(photoUri).into(userImage);
        }
    }

    @OnClick(R.id.savePhotoBtn)
    public void savePhotoToFB(){
        showProgressDialog();
        StorageReference onlineStoragePhotoRef = storageRef.child(photoUri.getLastPathSegment());
        onlineStoragePhotoRef.putFile(photoUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                System.out.println("Загрузка завершена на " + progress + "%");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                hideProgressDialog();
                photoUrl = taskSnapshot.getDownloadUrl().toString();
                UploadPhotoModel model = new UploadPhotoModel(user.getUid(), photoUrl);
                database.getReference("UsersPhoto").push().setValue(model);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(getBaseContext(), "Fail to add storage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.backBtn)
    public void backPress(){
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
