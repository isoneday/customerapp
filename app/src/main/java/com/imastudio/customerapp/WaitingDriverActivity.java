package com.imastudio.customerapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.imastudio.customerapp.helper.HeroHelper;
import com.imastudio.customerapp.helper.MyContants;
import com.imastudio.customerapp.helper.SessionManager;
import com.imastudio.customerapp.model.modelojekonline.ResponseCheckBooking;
import com.imastudio.customerapp.network.InitRetrofit;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingDriverActivity extends AppCompatActivity {

    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    @BindView(R.id.buttoncancel)
    Button buttoncancel;
    private int idbooking;
    private SessionManager manager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_driver);
        ButterKnife.bind(this);
        pulsator.start();
        idbooking = getIntent().getIntExtra(MyContants.IDBOOKING, 0);
        checkBooking();
        timer = new Timer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkBooking();
                    }
                },0, 3000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();

    }

    private void checkBooking() {

        InitRetrofit.getInstance().CheckBooking(String.valueOf(idbooking)).enqueue(new Callback<ResponseCheckBooking>() {
            @Override
            public void onResponse(Call<ResponseCheckBooking> call, Response<ResponseCheckBooking> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String msg = response.body().getMsg();
                    if (result.equals("true")) {
                        String idDriver = response.body().getDriver();
                        Intent i = new Intent(WaitingDriverActivity.this, DetailDriverActivity.class);
                        i.putExtra(MyContants.IDDRIVER, idDriver);
                        startActivity(i);
                        finish();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckBooking> call, Throwable t) {
                Toast.makeText(WaitingDriverActivity.this, "gagal koneksi" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.buttoncancel)
    public void onViewClicked() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Cancel");
        alert.setMessage("Apakah anda yakin cancel orderan ini ?");
        alert.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelOrder();
            }
        });
        alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

    }

    private void cancelOrder() {
        ProgressDialog loading = ProgressDialog.show(this, "Process cancel order", "Loading....");
        manager = new SessionManager(this);
        String token = manager.getToken();
        String device = HeroHelper.getDeviceUUID(this);
        InitRetrofit.getInstance().CancelBooking(
                String.valueOf(idbooking), token, device).enqueue(new Callback<ResponseCheckBooking>() {
            @Override
            public void onResponse(Call<ResponseCheckBooking> call, Response<ResponseCheckBooking> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String msg = response.body().getMsg();
                    loading.dismiss();
                    if (result.equals("true")) {
                        Toast.makeText(WaitingDriverActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(WaitingDriverActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckBooking> call, Throwable t) {
                Toast.makeText(WaitingDriverActivity.this, "gagal koneksi:"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
