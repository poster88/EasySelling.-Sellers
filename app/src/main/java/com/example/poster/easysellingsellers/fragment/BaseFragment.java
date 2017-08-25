package com.example.poster.easysellingsellers.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.poster.easysellingsellers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by User on 018 18.08.17.
 */

public class BaseFragment extends Fragment {
    protected FirebaseDatabase database;
    protected FirebaseUser user;
    protected StorageReference refUsersPhoto;
    protected ProgressDialog progressDialog;

    protected final static String USER_INFO_TABLE = "UserLoginInfoTable";
    protected final static String COMP_INF_TABLE = "CompaniesInfoTable";
    protected final static String USERS_IMAGES = "users_images";
    protected final static String REF_USER_PHOTO = "UsersPhoto";
    protected final static String USER_ORDER_TABLE = "UserOrderTable";
    protected final static int PHOTO_REQUEST = 9002;
    protected final static int REQUEST_READ_PERMISSION = 9003;
    protected final static int RESULT_OK = -1;
    protected Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        refUsersPhoto = FirebaseStorage.getInstance().getReference(USERS_IMAGES);
    }

    protected void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(msg);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void setFragmentForBinder(Object target, View source){
        unbinder = ButterKnife.bind(target, source);
    }

    protected void showToast(Context context, String title){
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    protected void startCurActivity(Context packageContext, Class<?> cls){
        startActivity(new Intent(packageContext, cls));
    }

    protected void showAlertDialog(String title, String message, int icon, boolean cancelable, String positiveBtnTitle, String negativeBtnTitle, DialogInterface.OnClickListener posBtnClickListener, DialogInterface.OnClickListener negBtnClickListener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setIcon(icon);
        ab.setCancelable(cancelable);
        ab.setPositiveButton(positiveBtnTitle, posBtnClickListener);
        ab.setNegativeButton(negativeBtnTitle, negBtnClickListener);
        ab.show();
    }

    protected void showAlertDialogWithView(String title, View view, int icon, boolean cancelable, String positiveBtnTitle, String negativeBtnTitle, DialogInterface.OnClickListener posBtnClickListener, DialogInterface.OnClickListener negBtnClickListener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setView(view);
        ab.setTitle(title);
        ab.setIcon(icon);
        ab.setCancelable(cancelable);
        ab.setPositiveButton(positiveBtnTitle, posBtnClickListener);
        ab.setNegativeButton(negativeBtnTitle, negBtnClickListener);
        ab.create();
        ab.show();
    }

    protected void showAlertDialogOneBtn(String title, String message, String btnTitle, DialogInterface.OnClickListener btnClickListener){
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle(title);
        ab.setIcon(android.R.drawable.stat_sys_warning);
        ab.setMessage(message);
        ab.setNegativeButton(btnTitle, btnClickListener);
        ab.create();
        ab.show();
    }

    protected boolean isValidEmail(EditText eMail, TextInputLayout inputLayoutEmail) {
        String mail = eMail.getText().toString().trim();
        if (TextUtils.isEmpty(mail)){
            inputLayoutEmail.setError(getString(R.string.err_msg_empty_email));
            requestFocus(eMail);
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(eMail);
            return false;
        }
        inputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    protected void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null){
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}
