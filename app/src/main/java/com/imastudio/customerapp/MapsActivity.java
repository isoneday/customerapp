package com.imastudio.customerapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.imastudio.customerapp.helper.DirectionMapsV2;
import com.imastudio.customerapp.helper.GPSTracker;
import com.imastudio.customerapp.helper.HeroHelper;
import com.imastudio.customerapp.helper.MyContants;
import com.imastudio.customerapp.model.modelmap.Distance;
import com.imastudio.customerapp.model.modelmap.Duration;
import com.imastudio.customerapp.model.modelmap.LegsItem;
import com.imastudio.customerapp.model.modelmap.ResponseMap;
import com.imastudio.customerapp.model.modelmap.RoutesItem;
import com.imastudio.customerapp.network.InitRetrofit;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.imgpick)
    ImageView imgpick;
    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.edtcatatan)
    EditText edtcatatan;
    @BindView(R.id.txtharga)
    TextView txtharga;
    @BindView(R.id.txtjarak)
    TextView txtjarak;
    @BindView(R.id.txtdurasi)
    TextView txtdurasi;
    @BindView(R.id.requestorder)
    Button requestorder;
    @BindView(R.id.rootlayout)
    RelativeLayout rootlayout;
    private GoogleMap mMap;
    private GPSTracker gps;
    private double latawal;
    private double lonawal;
    private String nameLocation;
    private LatLng myLocation;
    private double latTujuan;
    private double lonTujuan;
    private String nameLocationTujuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        gps = new GPSTracker(this);
        getMyLocation();
     }
//untuk mengambil lokasi saat ini /current location
    private void getMyLocation() {

        if (gps.canGetLocation()){
            latawal = gps.getLatitude();
            lonawal = gps.getLongitude();
            nameLocation = NameLocation(latawal,lonawal);
            //buat objek untuk mengatur tampilan map
            myLocation = new LatLng(latawal,lonawal);
            mMap.addMarker(new MarkerOptions().position(myLocation).title(
                    nameLocation
            )).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup));
            //fokus camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    myLocation,17));
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            lokasiawal.setText(nameLocation);
        }

    }

    private String NameLocation(double latawal, double lonawal) {
        nameLocation = null;
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latawal, lonawal, 1);
            if (list != null && list.size() > 0) {
                nameLocation = list.get(0).getAddressLine(0) + "" + list.get(0).getCountryName();

                //fetch data from addresses
            } else {
                Toast.makeText(MapsActivity.this, "kosong", Toast.LENGTH_SHORT).show();
                //display Toast message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameLocation;
    }

    @OnClick({R.id.imgpick, R.id.lokasiawal, R.id.lokasitujuan, R.id.requestorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgpick:
                startActivityForResult(new Intent(MapsActivity.this,SearchMapActivity.class),0);
                break;
            case R.id.lokasiawal:
                setLokasi(MyContants.LOKASIAWAL);
                break;
            case R.id.lokasitujuan:
                setLokasi(MyContants.LOKASITUJUAN);
                break;
            case R.id.requestorder:
                break;
        }
    }

    private void setLokasi(int kodeLokasi) {
        AutocompleteFilter filter =new AutocompleteFilter.Builder().
                setCountry("ID").build();

        Intent i = null;
        try {
            i = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).
                    setFilter(filter).build(MapsActivity.this);
            startActivityForResult(i, kodeLokasi);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MyContants.LOKASIAWAL&&resultCode==RESULT_OK){
            Place place = PlaceAutocomplete.getPlace(this, data);
            latawal =place.getLatLng().latitude;
            lonawal = place.getLatLng().longitude;
            LatLng lokAwal = new LatLng(latawal,lonawal);
            mMap.clear();
            nameLocation = place.getAddress().toString();
            mMap.addMarker(new MarkerOptions().position(lokAwal).title(
                    nameLocation
            )).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokAwal, 17));
            lokasiawal.setText(nameLocation);
        }else if (requestCode==MyContants.LOKASITUJUAN&&resultCode==RESULT_OK){
            Place place = PlaceAutocomplete.getPlace(this, data);
            latTujuan =place.getLatLng().latitude;
            lonTujuan = place.getLatLng().longitude;
            LatLng lokTujuan = new LatLng(latTujuan,lonTujuan);

            nameLocationTujuan = place.getAddress().toString();
            mMap.addMarker(new MarkerOptions().position(lokTujuan).title(
                    nameLocationTujuan
            )).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokTujuan, 17));
            lokasitujuan.setText(nameLocationTujuan);
            aksesRute();
        }else{
            Bundle resultData = data.getExtras();
            String value = resultData.getString("value");
            latawal = resultData.getDouble("lat");
            lonawal =resultData.getDouble("lon");
            mMap.clear();
            lokasiawal.setText(value);
        }

    }

    private void aksesRute() {
        String key = getString(R.string.google_maps_key);
        String lokasiawal = String.valueOf(latawal+","+lonawal);
        String lokasitujuan = String.valueOf(latTujuan+","+lonTujuan);
        InitRetrofit.getInstanceGoogle().getdataMap(
              lokasiawal,
               lokasitujuan,
                key

        ).enqueue(new Callback<ResponseMap>() {
            @Override
            public void onResponse(Call<ResponseMap> call, Response<ResponseMap> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();
                    if (status.equals("OK")){
                        List<RoutesItem> data = response.body().getRoutes();
                        List<LegsItem> dataLegs = data.get(0).getLegs();
                        Distance distance = dataLegs.get(0).getDistance();
                        Duration duration = dataLegs.get(0).getDuration();
                        double harga = Double.parseDouble(HeroHelper.removeLastChar(
                                distance.getText()))*5000;
                        txtharga.setText(String.valueOf(harga));
                        txtdurasi.setText(duration.getText());
                        txtjarak.setText(distance.getText());
                        DirectionMapsV2 mapsV2 = new DirectionMapsV2(MapsActivity.this);
                        String point = data.get(0).getOverviewPolyline().getPoints();
                        mapsV2.gambarRoute(mMap,point);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMap> call, Throwable t) {

            }
        });
    }
}
