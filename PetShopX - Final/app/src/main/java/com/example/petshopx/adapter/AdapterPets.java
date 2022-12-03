package com.example.petshopx.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopx.R;
import com.example.petshopx.model.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPets extends RecyclerView.Adapter<AdapterPets.MyViewHolder> {

    private List<Pet> petList;
    private OnClick onClick;

    public AdapterPets(List<Pet> petList, OnClick onClick) {
        this.petList = petList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pet pet = petList.get(position);

        Picasso.get().load(pet.getUrlImagem()).into(holder.img_pet);
        holder.text_nome.setText(pet.getNome());
        holder.text_descricao.setText(pet.getDescricao());

        holder.itemView.setOnClickListener(view -> {
            onClick.OnClickListener(pet);
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public interface OnClick {
        public void OnClickListener(Pet pet);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_pet;
        TextView text_nome, text_descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_pet = itemView.findViewById(R.id.img_pet);
            text_nome = itemView.findViewById(R.id.text_nome_servico);
            text_descricao = itemView.findViewById(R.id.text_descricao_servico);
        }
    }
}
