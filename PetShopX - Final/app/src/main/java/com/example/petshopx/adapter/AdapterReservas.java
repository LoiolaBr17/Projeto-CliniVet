package com.example.petshopx.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopx.R;
import com.example.petshopx.model.Reserva;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterReservas extends RecyclerView.Adapter<AdapterReservas.MyViewHolder> {

    private List<Reserva> reservaList;
    private AdapterReservas.OnClick onClick;

    public AdapterReservas(List<Reserva> reservaList, AdapterReservas.OnClick onClick) {
        this.reservaList = reservaList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Reserva reserva = reservaList.get(position);

        Picasso.get().load(reserva.getImgPet()).into(holder.img_pet);
        holder.text_servico.setText(reserva.getServicoNome());
        holder.text_pet.setText(reserva.getPetNome());
        holder.text_dia.setText(reserva.getDia()+" "+reserva.getHora());
        holder.text_status.setText(reserva.getStatus());

        holder.itemView.setOnClickListener(view -> onClick.OnClickListener(reserva));
    }

    @Override
    public int getItemCount() {
        return reservaList.size();
    }

    public interface OnClick {
        public void OnClickListener(Reserva reserva);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_pet;
        TextView text_servico, text_pet, text_dia,text_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_pet = itemView.findViewById(R.id.img_pet);
            text_servico = itemView.findViewById(R.id.text_servico);
            text_pet = itemView.findViewById(R.id.text_pet);
            text_dia = itemView.findViewById(R.id.text_dia);
            text_status = itemView.findViewById(R.id.text_status);
        }
    }
}
