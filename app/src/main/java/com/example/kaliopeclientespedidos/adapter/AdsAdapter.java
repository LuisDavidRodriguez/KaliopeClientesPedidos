package com.example.kaliopeclientespedidos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kaliopeclientespedidos.R;
import com.example.kaliopeclientespedidos.models.Ads;

import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.ViewHolderAds> {

    private List<Ads> list;

    public AdsAdapter(List<Ads> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdsAdapter.ViewHolderAds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_container_ads,
                    parent,
                false
        );

        return new ViewHolderAds(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsAdapter.ViewHolderAds holder, int position) {

        //seteamos los datos
        holder.imageView.setImageResource(R.drawable.cortador1);


        //Glide.with(((ViewHolderAds)holder).itemView)                //necesitamos una view para glide, use la manera que me enseño al usar el viewType del recicler, no se que hacen los parentecis jaja pero puedo acceder a las cosas de mi view holder
        //        .load(list.get(position).getUrl_image())
        //        .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderAds extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolderAds(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.item_container_ads_image);
        }




    }
}
