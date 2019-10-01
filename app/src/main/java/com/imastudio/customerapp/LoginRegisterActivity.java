package com.imastudio.customerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.imastudio.customerapp.helper.HeroHelper;
import com.imastudio.customerapp.helper.SessionManager;
import com.imastudio.customerapp.model.modelojekonline.DataLogin;
import com.imastudio.customerapp.model.modelojekonline.ResponseLogin;
import com.imastudio.customerapp.model.modelojekonline.ResponseRegister;
import com.imastudio.customerapp.network.InitRetrofit;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegisterActivity extends AppCompatActivity {

    @BindView(R.id.txt_rider_app)
    TextView txtRiderApp;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.rootlayout)
    RelativeLayout rootlayout;
    private DataLogin dataLogin;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnSignIn, R.id.btnRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                login();
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void login() {
        AlertDialog.Builder alertLogin = new AlertDialog.Builder(this);
        alertLogin.setTitle("Login");
        alertLogin.setMessage(R.string.messagelogin);
        LayoutInflater inflater = getLayoutInflater();
        View formLogin = inflater.inflate(R.layout.layout_login, null);
        ViewHolderLogin holderLogin = new ViewHolderLogin(formLogin);
        alertLogin.setView(formLogin);
        alertLogin.setPositiveButton("Login", null);
        alertLogin.setNegativeButton("cancel", null);
        AlertDialog dialog = alertLogin.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(holderLogin.edtEmail.getText().toString())) {
                    holderLogin.edtEmail.setError(getString(R.string.requireemail));
                } else if (TextUtils.isEmpty(holderLogin.edtPassword.getText().toString())) {
                    holderLogin.edtPassword.setError(getString(R.string.requirepassword));

                } else {
                    prosesLogin(holderLogin, dialog);
                }
            }
        });

    }

    private void prosesLogin(ViewHolderLogin holderLogin, AlertDialog dialog) {
        ProgressDialog loading = ProgressDialog.show(this,
                "Proses Login", "Loading . . .");
       String device = HeroHelper.getDeviceUUID(this);
        InitRetrofit.getInstance().login(
                device,
                holderLogin.edtPassword.getText().toString(),
                holderLogin.edtEmail.getText().toString()
                ).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()){
                    String result=response.body().getResult();
                    String msg =response.body().getMsg();
                    loading.dismiss();
                    if (result.equals("true")){
                        Toast.makeText(LoginRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //SIMPAN DATA LOGIN
                        dataLogin = response.body().getData();
                        dialog.dismiss();
                        manager = new SessionManager(LoginRegisterActivity.this);
                        manager.setIduser(dataLogin.getIdUser());
                        manager.createLoginSession(response.body().getToken());
                        startActivity(new Intent(LoginRegisterActivity.this,
                                MainActivity.class));
                       //kill activity/halaman
                        finish();
                    }else{
                        Toast.makeText(LoginRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(LoginRegisterActivity.this, "gagal koneksi:"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register() {
        AlertDialog.Builder alertRegister = new AlertDialog.Builder(this);
        alertRegister.setTitle("Register");
        alertRegister.setMessage(R.string.messageregister);
        LayoutInflater inflater = getLayoutInflater();
        View formRegis = inflater.inflate(R.layout.layout_register, null);
        ViewHolderRegis holderRegis = new ViewHolderRegis(formRegis);
        alertRegister.setView(formRegis);
        alertRegister.setPositiveButton("register", null);
        alertRegister.setNegativeButton("cancel", null);
        AlertDialog dialog = alertRegister.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(holderRegis.edtEmail.getText().toString())) {
                    holderRegis.edtEmail.setError(getString(R.string.requireemail));
                } else if (TextUtils.isEmpty(holderRegis.edtName.getText().toString())) {
                    holderRegis.edtName.setError(getString(R.string.requirename));

                } else if (TextUtils.isEmpty(holderRegis.edtPassword.getText().toString())) {
                    holderRegis.edtPassword.setError(getString(R.string.requirepassword));

                } else if (TextUtils.isEmpty(holderRegis.edtPhone.getText().toString())) {
                    holderRegis.edtPhone.setError(getString(R.string.requirephone));

                } else {
                    prosesRegister(holderRegis, dialog);
                }
            }
        });


    }

    private void prosesRegister(ViewHolderRegis holderRegis, AlertDialog dialog) {
        ProgressDialog loading = ProgressDialog.show(this,
                "Proses Register", "Loading . . .");
        InitRetrofit.getInstance().
                register(holderRegis.edtName.getText().toString(),
                        holderRegis.edtPassword.getText().toString(),
                        holderRegis.edtEmail.getText().toString(),
                        holderRegis.edtPhone.getText().toString()).enqueue(
                new Callback<ResponseRegister>() {
                    @Override
                    public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                        if (response.isSuccessful()) {
                            String result = response.body().getResult();
                            String msg = response.body().getMsg();
                            loading.dismiss();
                            if (result.equals("true")) {
                                dialog.dismiss();
                                Toast.makeText(LoginRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRegister> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(LoginRegisterActivity.this, "gagal koneksi" +
                                t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    static
    class ViewHolderRegis {
        @BindView(R.id.edtEmail)
        MaterialEditText edtEmail;
        @BindView(R.id.edtPassword)
        MaterialEditText edtPassword;
        @BindView(R.id.edtName)
        MaterialEditText edtName;
        @BindView(R.id.edtPhone)
        MaterialEditText edtPhone;

        ViewHolderRegis(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ViewHolderLogin {
        @BindView(R.id.edtEmail)
        MaterialEditText edtEmail;
        @BindView(R.id.edtPassword)
        MaterialEditText edtPassword;

        ViewHolderLogin(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
