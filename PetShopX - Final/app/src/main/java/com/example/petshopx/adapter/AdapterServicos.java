package com.example.petshopx.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopx.R;
import com.example.petshopx.model.Servico;

import java.util.List;

public class AdapterServicos extends RecyclerView.Adapter<AdapterServicos.MyViewHolder> {

    private List<Servico> servicoList;
    private OnClick onClick;

    public AdapterServicos(List<Servico> servicoList, OnClick onClick) {
        this.servicoList = servicoList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servico, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Servico servico = servicoList.get(position);

        holder.text_nome.setText(servico.getNome());
        holder.text_descricao.setText(servico.getDescricao());
        holder.text_valor.setText("R$ "+servico.getPreco());

        holder.itemView.setOnClickListener(view -> onClick.OnClickListener(servico));
    }

    @Override
    public int getItemCount() {
        return servicoList.size();
    }

    public interface OnClick {
        public void OnClickListener(Servico servico);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text_nome, text_descricao, text_valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text_nome = itemView.findViewById(R.id.text_nome_servico);
            text_descricao = itemView.findViewById(R.id.text_descricao_servico);
            text_valor = itemView.findViewById(R.id.text_preco_servico);
        }
    }
}
