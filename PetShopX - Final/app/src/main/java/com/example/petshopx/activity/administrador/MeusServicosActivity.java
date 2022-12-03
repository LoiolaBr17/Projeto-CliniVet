package com.example.petshopx.activity.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.petshopx.R;
import com.example.petshopx.adapter.AdapterServicos;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Reserva;
import com.example.petshopx.model.Servico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeusServicosActivity extends AppCompatActivity implements AdapterServicos.OnClick {

    private List<Servico> servicoList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView text_info;
    private SwipeableRecyclerView rv_servicos;
    private AdapterServicos adapterServicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_servicos);

        iniciaComponentes();

        configRv();

        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaServicos();
    }

    private void configCliques(){
        findViewById(R.id.ib_add).setOnClickListener(view ->
                startActivity(new Intent(this, FormServicoActivity.class)));

        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void configRv(){
        rv_servicos.setLayoutManager(new LinearLayoutManager(this));
        rv_servicos.setHasFixedSize(true);
        adapterServicos = new AdapterServicos(servicoList, this);
        rv_servicos.setAdapter(adapterServicos);

        rv_servicos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(position);
            }
        });
    }

    private void showDialogDelete(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Serviço");
        builder.setMessage("aperte em Sim para confirmar ou em Não para cancelar.");
        builder.setNegativeButton("Não", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            adapterServicos.notifyDataSetChanged();
        });
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            cancelarPossiveisReservas(servicoList.get(pos).getId());
            servicoList.get(pos).deletar();
            adapterServicos.notifyItemRemoved(pos);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cancelarPossiveisReservas(String idServico){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("reservas_all");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot snap : snapshot.getChildren()){
                        Reserva reserva = snap.getValue(Reserva.class);

                        if(reserva.getIdServico().equals(idServico)){
                            if(reserva.getStatus().equals("pendente")||reserva.getStatus().equals("reservada")){
                                reserva.setStatus("cancelada");
                                reserva.cancelarReserva();

                                DatabaseReference reference2 = FirebaseHelper.getDatabaseReference()
                                        .child("reservas");


                                reference2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){

                                            for(DataSnapshot snap : snapshot.getChildren()){
                                                Reserva reserva = snap.getValue(Reserva.class);

                                                if(reserva.getIdServico().equals(idServico)){
                                                    if(reserva.getStatus().equals("pendente")||reserva.getStatus().equals("reservada")){
                                                        reserva.setStatus("cancelada");
                                                        reserva.cancelarReserva();
                                                    }
                                                }

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void recuperaServicos(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("servicos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                servicoList.clear();
                if(snapshot.exists()){

                    for(DataSnapshot snap : snapshot.getChildren()){
                        Servico servico = snap.getValue(Servico.class);
                        servicoList.add(servico);
                    }

                    text_info.setText("");

                }else{
                    text_info.setText("Nenhum serviço cadastrado.");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(servicoList);
                adapterServicos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciaComponentes(){
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Serviços");

        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
        rv_servicos = findViewById(R.id.rv_servicos);
    }


    @Override
    public void OnClickListener(Servico servico) {
        Intent intent = new Intent(this, FormServicoActivity.class);
        intent.putExtra("servico", servico);
        startActivity(intent);
    }
}