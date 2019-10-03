package com.imastudio.customerapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.imastudio.customerapp.helper.MyContants;
import com.imastudio.customerapp.model.modelmakanan.DataKategoriItem;
import com.imastudio.customerapp.model.modelmakanan.DataMakananItem;
import com.imastudio.customerapp.model.modelmakanan.ResponseKategoriMakanan;
import com.imastudio.customerapp.network.InitRetrofit;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.imastudio.customerapp.helper.MyContants.BASE_MAKANANURL;

public class EditDeleteMakananActivity extends AppCompatActivity {

    @BindView(R.id.edtidmakanan)
    EditText edtidmakanan;
    @BindView(R.id.edtnamamakanan)
    TextInputEditText edtnamamakanan;
    @BindView(R.id.spincarikategori)
    Spinner spincarikategori;
    @BindView(R.id.btnuploadmakanan)
    Button btnuploadmakanan;
    @BindView(R.id.imgupload)
    ImageView imgupload;
    @BindView(R.id.alert_layout_root)
    LinearLayout alertLayoutRoot;
    @BindView(R.id.btnupdate)
    Button btnupdate;
    @BindView(R.id.btndelete)
    Button btndelete;
    private Uri filepath;
    private Bitmap bitmap;
    private ArrayList<DataMakananItem> dataMakanan;
    private int index;
    private Target gambarPreview;
    private String strnamaMakanan;
    private String strPath;
    private List<DataKategoriItem> dataKategori;
    private String stridkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_makanan);
        ButterKnife.bind(this);
        Intent i = getIntent();
        dataMakanan = i.getParcelableArrayListExtra(MyContants.DATAMAKANAN);

        index = i.getIntExtra(MyContants.INDEX, 0);
        edtnamamakanan.setText(dataMakanan.get(index).getMakanan());
        edtidmakanan.setText(dataMakanan.get(index).getIdMakanan());
        stridkategori =dataMakanan.get(index).getIdKategori();
        gambarPreview = new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imgupload.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(BASE_MAKANANURL+"uploads/"+dataMakanan.get(index)
        .getFotoMakanan()).placeholder(R.drawable.empty).error(R.drawable.empty)
                .into(gambarPreview);
        getKategoriMakanan();
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

                    ArrayAdapter adapter = new ArrayAdapter(EditDeleteMakananActivity.this,
                            android.R.layout.simple_spinner_item, nama);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spincarikategori.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //aksi ketika spinner dipilih
                    spincarikategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            stridkategori = adapterView.getItemAtPosition(i).toString();

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
    @OnClick({R.id.btnuploadmakanan, R.id.btnupdate, R.id.btndelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnuploadmakanan:
                showFilechooser(MyContants.REQ_FILE_CHOOSE);
                break;
            case R.id.btnupdate:
                strnamaMakanan = edtnamamakanan.getText().toString();
                if (TextUtils.isEmpty(strnamaMakanan)) {
                 edtnamamakanan.setError("nama makanan tidak boleh kosong");
                }else if (imgupload.getDrawable()==null) {
                    Toast.makeText(this, "gambar harus dipilih", Toast.LENGTH_SHORT).show();

                }   else
                 {
                    prosesUpdateMakanan();
                }


                break;
            case R.id.btndelete:
//                prosesDeleteMakanan();
                break;
        }
    }

    private void prosesUpdateMakanan() {
        try {
            strPath = getPath(filepath);

        } catch (Exception e) {
            Toast.makeText(this, "gagal menampilkan gambar", Toast.LENGTH_SHORT).show();
            e.getLocalizedMessage();
        }
        String stridmakanan =edtidmakanan.getText().toString();
        try {
            new MultipartUploadRequest(this, MyContants.UPLOADUPDATE_URL)
                    .addParameter("vsnamamakanan", strnamaMakanan)
                    .addParameter("vsidmakanan", stridmakanan)
                    .addParameter("vsidkategori", stridkategori)
                    .addFileToUpload(strPath, "image")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(EditDeleteMakananActivity.this,MakananActivity.class));
            finish();

    }

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
                imgupload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

}
