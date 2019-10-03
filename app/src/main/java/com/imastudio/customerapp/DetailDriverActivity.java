package com.imastudio.customerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.imastudio.customerapp.helper.MyContants;
import com.imastudio.customerapp.model.modelojekonline.DataInfoDriver;
import com.imastudio.customerapp.model.modelojekonline.ResponseDriver;
import com.imastudio.customerapp.network.InitRetrofit;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDriverActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.txtnamadriver)
    TextView txtnamadriver;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.txthpdriver)
    TextView txthpdriver;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    private GoogleMap mMap;
    private String iddriver;
    private List<DataInfoDriver> driver;
    private double latdriver;
    private double londriver;
    private LatLng posisiDriver;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_driver);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        iddriver = getIntent().getStringExtra(MyContants.IDDRIVER);
        timer = new Timer();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDetailDriver();

    }

    private void getDetailDriver() {
        InitRetrofit.getInstance().getDetailDriver(iddriver).enqueue(new Callback<ResponseDriver>() {
            @Override
            public void onResponse(Call<ResponseDriver> call, Response<ResponseDriver> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    if (result.equals("true")) {
                        driver = response.body().getData();
                        txthpdriver.setText(driver.get(0).getUserHp());
                        txtnamadriver.setText(driver.get(0).getUserNama());
                        latdriver = Double.parseDouble(driver.get(0).getTrackingLat());
                        londriver = Double.parseDouble(driver.get(0).getTrackingLng());
                        posisiDriver = new LatLng(latdriver, londriver);
                        mMap.addMarker(new MarkerOptions().position(posisiDriver)).setIcon(
                                BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)
                        );
                        mMap.moveCamera(CameraUpdateFactory.
                                newLatLngZoom(posisiDriver, 17));
                        mMap.setPadding(40, 150, 50, 120);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDriver> call, Throwable t) {
                Toast.makeText(DetailDriverActivity.this, "gagal koneksi" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.txthpdriver)
    public void onViewClicked() {
   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        1110);
            }
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driver.get(0).getUserHp())));
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
                getDetailDriver();
            }
        }, 0,5000);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}
