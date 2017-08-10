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
        productKeys.add("Пральні порошки");
        productKeys.add("Гелі для прання");
        productKeys.add("Мила");
        productKeys.add("Відбілювачі");

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
        product1.setName("Пральний порошок Persil");
        product1.setDescription("Стиральный порошок Persil Колор от компании Henkel предназначен для стирки цветных вещей. Он отлично удаляет загрязнения и сохраняет яркость вашей одежды.");
        product1.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fpersil.jpg?alt=media&token=023e0662-fe6c-459f-a308-df452244a97d");
        product1.setPrice(539.4);
        product1.setWeight("3kg");
        product1.setColor("white");
        product1.setAvailability(true);
        product1.setCount(993);

        Product product2 = new Product();
        product2.setId(refCompInfTable.push().getKey());
        product2.setName("Плямовивідник порошкоподібний для тканин Vanish Gold Oxi Action");
        product2.setDescription("Порошковый пятновыводитель Vanish Gold Oxi Action удаляет пятна всего за 30 секунд, подходит как для белых, так и для цветных вещей");
        product2.setPrice(117.0);
        product2.setColor("white");
        product2.setAvailability(true);
        product2.setCount(444);
        product2.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fvanish.jpg?alt=media&token=313e9fb7-1b48-446b-8d5e-7f391869b5cc");

        Product product3 = new Product();
        product3.setId(refCompInfTable.push().getKey());
        product3.setName("Ополіскувач для білизни Silan Небесна свіжість");
        product3.setDescription("Silan делает ткани мягкими, предотвращает электризацию вещей, облегчает глажку. Благодаря Silan вещи высыхают быстрее, а также приобретают длительный нежный аромат");
        product3.setPrice(110.0);
        product3.setColor("white");
        product3.setAvailability(true);
        product3.setCount(444);
        product3.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fsilan.jpg?alt=media&token=3c250aff-5c06-40cb-9d9a-d89b5217bea9");

        List<Product> dlya_prannya = new ArrayList<>();
        dlya_prannya.add(product1);
        dlya_prannya.add(product2);
        dlya_prannya.add(product3);


        Product product21 = new Product();
        product21.setId(refCompInfTable.push().getKey());
        product21.setName("Засіб для миття посуду Fairy ProDerma Алое Вера і Кокос");
        product21.setDescription("Средство для мытья посуды Fairy ProDerma Алоэ Вера и Кокос от Procter & Gamble – всемирно известной американской транснациональной компании, являющейся одной из лидеров мирового рынка потребительских товаров.");
        product21.setPrice(27.0);
        product21.setAvailability(true);
        product21.setCount(55);
        product21.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Ffairy.jpg?alt=media&token=9d5deb50-37b8-4511-a630-afb7c6ebaba2");

        Product product22 = new Product();
        product22.setId(refCompInfTable.push().getKey());
        product22.setName("Опис Гель для миття посуду Bio Formula Чайне дерево і М'ята");
        product22.setDescription("Гель содержит биологически разлагаемые ПАВ. Легко смывается с посуды! Содержит экстракт щавеля (для придания блеска) и экстракты чайного дерева и мяты перечной. Грицерин защищает кожу рук от пересыхания. Бальзам имеет легкий аромат.");
        product22.setPrice(12.0);
        product22.setAvailability(true);
        product22.setCount(500);
        product22.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fbio_formula.jpg?alt=media&token=0a335eb6-9a51-4507-a114-3282cb2149bf");

        Product product23 = new Product();
        product23.setId(refCompInfTable.push().getKey());
        product23.setName("Опис Таблетки для посудомийної машини Somat");
        product23.setDescription("Somat All in 1 имеет безфосфатную формулу и усилен действием лимонной кислоты. Он избавляется даже от стойких остатков еды и накипи от кофе и чая, а также обеспечивает безупречный результат уже при 40°.");
        product23.setPrice(270.0);
        product23.setAvailability(true);
        product23.setCount(401);
        product23.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fsomat.jpg?alt=media&token=59b80bb8-780f-4b68-9090-d1d33e613225");

        List<Product> dlya_posudu = new ArrayList<>();
        dlya_posudu.add(product21);
        dlya_posudu.add(product22);
        dlya_posudu.add(product23);

        Product product31 = new Product();
        product31.setId(refCompInfTable.push().getKey());
        product31.setName("Засіб від цвілі Glutoclean з хлором");
        product31.setDescription("Средство от плесени для эффективного и долговременного удаления плесени и грибка всех видов. Мгновенное дезинфицирующее действие с сильным отбеливающим эффектом. Помогает легко удалить плесень с любых поверхностей.");
        product31.setPrice(190.0);
        product31.setAvailability(true);
        product31.setCount(2061);
        product31.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fbio_formula.jpg?alt=media&token=0a335eb6-9a51-4507-a114-3282cb2149bf");

        Product product32 = new Product();
        product32.setId(refCompInfTable.push().getKey());
        product32.setName("Засіб для миття підлоги Fit Mr Floor Лайм");
        product32.setDescription("Компания Алес — производитель высококачественной косметики и бытовой химии. Компания славится высококвалифицированным кадровым потенциалом, разработанными фирменными рецептурами, высококачественным сырьем известных компаний и разными сериями косметических средств таких, как Olis, 5 Five, Fit и другие.");
        product32.setPrice(28.0);
        product32.setAvailability(true);
        product32.setCount(19061);
        product32.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Ffir.jpg?alt=media&token=147086fa-439e-42e3-ae2d-c336c5729e3d");

        List<Product> uborka = new ArrayList<>();
        uborka.add(product31);
        uborka.add(product32);

        Product product41 = new Product();
        product41.setId(refCompInfTable.push().getKey());
        product41.setName("Серветки універсальні Bella №1 паперові чотиришарові 100 шт");
        product41.setDescription("Мягкие, тонкие, двухслойные косметические салфетки без аромата");
        product41.setPrice(25.0);
        product41.setAvailability(true);
        product41.setCount(300234);
        product41.setPhotoUries("https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/items_images%2Fhouse_master%2Fbella.jpg?alt=media&token=b9537f7e-25d2-4cde-ae0e-5e6d87d5009f");

        List<Product> gosp_tovar = new ArrayList<>();
        gosp_tovar.add(product41);


        Map<String, Object> subCategories = new HashMap<>();
        subCategories.put("Для прання", dlya_prannya);
        subCategories.put("Для миття посуду", dlya_posudu);
        subCategories.put("Для прибирання", uborka);
        subCategories.put("Господарчі товари", gosp_tovar);

        Map<String, Object> categories = new HashMap<>();
        categories.put("Побутова хімія", subCategories);

        CompaniesInfoTable companyInfoTable = new CompaniesInfoTable(
                id,
                "HouseMaster",
                "Виробництво та збут побутової хімії",
                "https://firebasestorage.googleapis.com/v0/b/fir-projectdb.appspot.com/o/companies_logos%2FhouseMaster.png?alt=media&token=329acb45-4048-4704-99fd-1b78a73309b2",
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
