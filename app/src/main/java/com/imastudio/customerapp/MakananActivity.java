package com.imastudio.customerapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.imastudio.customerapp.adapter.RecyclerMakananAdapter;
import com.imastudio.customerapp.helper.MyContants;
import com.imastudio.customerapp.helper.SessionManager;
import com.imastudio.customerapp.model.modelmakanan.DataKategoriItem;
import com.imastudio.customerapp.model.modelmakanan.DataMakananItem;
import com.imastudio.customerapp.model.modelmakanan.ResponseKategoriMakanan;
import com.imastudio.customerapp.model.modelmakanan.ResponseMakanan;
import com.imastudio.customerapp.network.InitRetrofit;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakananActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerMakananAdapter.aksiKlikItemRecycler {

    @BindView(R.id.spincarimakanan)
    Spinner spincarimakanan;
    @BindView(R.id.listmakanan)
    RecyclerView listmakanan;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    private String strnamaMakanan;
    private Uri filepath;
    private Bitmap bitmap;
    private ViewHolderInsert insert;
    private String strPath;
    private SessionManager manager;
    private String striduser;
    private List<DataKategoriItem> dataKategori;
    private String strkategori;
    private ArrayList<DataMakananItem> dataMakanan;
    private RecyclerMakananAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//      startService(new Intent(this,ForegroundService.class));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDataMakanan();
            }
        });
        manager = new SessionManager(this);
        striduser = manager.getIdUser();
        cekPermission();
        getKategoriMakanan();

        refreshlayout.setOnRefreshListener(this);
    }

    private void getKategoriMakanan() {

        InitRetrofit.getInstanceMakanan().getKategoriMakanan().enqueue(new Callback<ResponseKategoriMakanan>() {
            @Override
            public void onResponse(Call<ResponseKategoriMakanan> call, Response<ResponseKategoriMakanan> response) {
                if (response.isSuccessful()) {
                    dataKategori = response.body().getDataKategori();
                    String[] id = new String[dataKategori.size()];
                    String[] nama = new String[dataKategori.size()];
                    for (int i = 0; i < dataKategori.size(); i++) {
                        id[i] = dataKategori.get(i).getIdKategori();
                        nama[i] = dataKategori.get(i).getNamaKategori();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(MakananActivity.this,
                            android.R.layout.simple_spinner_item, nama);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spincarimakanan.setAdapter(adapter);

                    //aksi ketika spinner dipilih
                    spincarimakanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            strkategori = adapterView.getItemAtPosition(i).toString();
                            getDataMakanan(strkategori);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseKategoriMakanan> call, Throwable t) {

            }
        });

    }

    private void getDataMakanan(String strkategori) {
        InitRetrofit.getInstanceMakanan().getMakanan(striduser, strkategori).enqueue(
                new Callback<ResponseMakanan>() {
                    @Override
                    public void onResponse(Call<ResponseMakanan> call, Response<ResponseMakanan> response) {
                        if (response.isSuccessful()) {
                            dataMakanan = response.body().getDataMakanan();
                            String[] idmakanan = new String[dataMakanan.size()];
                            String[] namaMakanan = new String[dataMakanan.size()];
                            String[] fotoMakanan = new String[dataMakanan.size()];
                        for (int i =0;i<dataMakanan.size();i++){
                            idmakanan[i]= dataMakanan.get(i).getIdMakanan();
                            namaMakanan[i]= dataMakanan.get(i).getMakanan();
                            fotoMakanan[i]= dataMakanan.get(i).getFotoMakanan();
                        }
                            adapter = new RecyclerMakananAdapter(MakananActivity.this,dataMakanan);
                            listmakanan.setAdapter(adapter);
                            //set tampilan recyclerview
                            listmakanan.setLayoutManager(new LinearLayoutManager(MakananActivity.this));
                            //panggil aksi adapter klik
                            adapter.setOnKlikItem(MakananActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMakanan> call, Throwable t) {

                    }
                });
    }


    private void cekPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 100
                );
            }
            return;
        }
    }

    private void InsertDataMakanan() {
        Dialog dialogInsert = new Dialog(this);
        dialogInsert.setTitle("Insert Data Makanan");
        dialogInsert.setCancelable(true);
        dialogInsert.setCanceledOnTouchOutside(false);
        View v = View.inflate(MakananActivity.this, R.layout.tambahmakanan, null);
        dialogInsert.setContentView(v);
        insert = new ViewHolderInsert(v);
        insert.btnuploadmakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilechooser(MyContants.REQ_FILE_CHOOSE);
            }
        });
        insert.btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strnamaMakanan = insert.edtnamamakanan.getText().toString();
                if (TextUtils.isEmpty(strnamaMakanan)) {
                    insert.edtnamamakanan.setError("nama makanan tidak boleh kosong");
                } else {

                    dialogInsert.dismiss();
                    prosesInsertMakanan();
                }
            }
        });
        dialogInsert.show();

    }


    private void prosesInsertMakanan() {
        try {
            strPath = getPath(filepath);

        } catch (Exception e) {
            Toast.makeText(this, "gagal menampilkan gambar", Toast.LENGTH_SHORT).show();
            e.getLocalizedMessage();
        }
        String waktu = currentDate();
        try {
            new MultipartUploadRequest(this, MyContants.UPLOAD_URL)
                    .addParameter("vsiduser", striduser)
                    .addParameter("vsnamamakanan", strnamaMakanan)
                    .addParameter("vstimeinsert", waktu)
                    .addParameter("vskategori", strkategori)
                    .addFileToUpload(strPath, "image")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        recreate();

        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    private String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);

    }

    //untuk mengambil path gambar
    public String getPath(Uri filepath) {
        Cursor cursor = getContentResolver().query(filepath, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //menampilkan galery image
    private void showFilechooser(int reqFileChoose) {
        Intent intentgalery = new Intent(Intent.ACTION_PICK);
        intentgalery.setType("image/*");
        intentgalery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentgalery, "select Pictures"), reqFileChoose);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyContants.REQ_FILE_CHOOSE && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                insert.imgupload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onRefresh() {
    getDataMakanan(strkategori);
    refreshlayout.setRefreshing(false);
    }

    @Override
    public void onItemClickRecycler(int position) {
        Intent i = new Intent(MakananActivity.this,EditDeleteMakananActivity.class);
        i.putParcelableArrayListExtra(MyContants.DATAMAKANAN,dataMakanan);
        i.putExtra(MyContants.INDEX, position);
        startActivity(i);
    }

    static
    class ViewHolderInsert {
        @BindView(R.id.edtnamamakanan)
        TextInputEditText edtnamamakanan;
        @BindView(R.id.btnuploadmakanan)
        Button btnuploadmakanan;
        @BindView(R.id.spincarikategori)
        Spinner spincarikategori;
        @BindView(R.id.imgupload)
        ImageView imgupload;
        @BindView(R.id.alert_layout_root)
        LinearLayout alertLayoutRoot;
        @BindView(R.id.btninsert)
        Button btninsert;
        @BindView(R.id.btnreset)
        Button btnreset;

        ViewHolderInsert(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
