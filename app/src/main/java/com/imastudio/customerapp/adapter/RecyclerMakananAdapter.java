package com.imastudio.customerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imastudio.customerapp.MakananActivity;
import com.imastudio.customerapp.R;
import com.imastudio.customerapp.model.modelmakanan.DataMakananItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.imastudio.customerapp.helper.MyContants.BASE_MAKANANURL;

public class RecyclerMakananAdapter extends
        RecyclerView.Adapter<RecyclerMakananAdapter.ViewHolder> {

    MakananActivity act;
    List<DataMakananItem> dataMakanan;

    aksiKlikItemRecycler klikItem;


    public  interface  aksiKlikItemRecycler{
        void onItemClickRecycler(int position);
    }

    public  void setOnKlikItem(aksiKlikItemRecycler onKlikItem){
        this.klikItem =onKlikItem;
    }

    public RecyclerMakananAdapter(MakananActivity makananActivity, List<DataMakananItem> dataMakanan) {
        this.dataMakanan = dataMakanan;
        act = makananActivity;
    }

    //untuk mengatur tampilan layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tampilan = LayoutInflater.from(act).inflate(
                R.layout.tampilanlistmakanan, null);
        ViewHolder holder = new ViewHolder(tampilan);
        return holder;
    }

    //untuk setdata atau set tampilan
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    holder.txtmakanan.setText(dataMakanan.get(position).getMakanan());
        Picasso.get().load(BASE_MAKANANURL+"uploads/"+dataMakanan
                .get(position).getFotoMakanan()).into(holder.imgmakanan);
        //itemview mewakili 1set tampilan item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            klikItem.onItemClickRecycler(position);
            }
        });

    }

    //untuk menghitung jumlah data yang akan ditampilkan
    @Override
    public int getItemCount() {
        return dataMakanan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgmakanan)
        ImageView imgmakanan;
        @BindView(R.id.txtmakanan)
        TextView txtmakanan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
