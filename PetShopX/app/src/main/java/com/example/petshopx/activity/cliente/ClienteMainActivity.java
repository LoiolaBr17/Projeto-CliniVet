package com.example.petshopx.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.activity.administrador.DetalhesServicoActivity;
import com.example.petshopx.activity.autenticacao.LoginActivity;
import com.example.petshopx.activity.comum.MinhaContaActivity;
import com.example.petshopx.adapter.AdapterServicos;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Servico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClienteMainActivity extends AppCompatActivity implements AdapterServicos.OnClick {

    private RecyclerView rv_servicos;
    private TextView text_info;
    private ProgressBar progressBar;

    private List<Servico> servicoList = new ArrayList<>();
    private AdapterServicos adapterServicos;
    private ImageButton ib_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_main);

        iniciaComponentes();

        configRv();

        configCliques();

        recuperaServicos();
    }

    private void configRv(){
        rv_servicos.setLayoutManager(new LinearLayoutManager(this));
        rv_servicos.setHasFixedSize(true);
        adapterServicos = new AdapterServicos(servicoList, this);
        rv_servicos.setAdapter(adapterServicos);
    }

    private void recuperaServicos(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("servicos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void configCliques(){
        ib_menu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, ib_menu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home_usuario, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.menu_minhas_reservas){
                    startActivity(new Intent(this, MinhasReservasActivity.class));
                }else if(menuItem.getItemId() == R.id.menu_meus_pets){
                    startActivity(new Intent(this, MeusPetsActivity.class));
                }else if(menuItem.getItemId() == R.id.menu_minha_conta){
                    startActivity(new Intent(this, MinhaContaActivity.class));
                }else if(menuItem.getItemId() == R.id.menu_sair){
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(ClienteMainActivity.this, LoginActivity.class));
                    finish();
                }


                return true;
            });
            popupMenu.show();
        });
    }

    private void iniciaComponentes(){
        ib_menu = findViewById(R.id.ib_menu);
        rv_servicos = findViewById(R.id.rv_servicos);
        text_info = findViewById(R.id.text_info);
        progressBar = findViewById(R.id.progressBar);
    }


    @Override
    public void OnClickListener(Servico servico) {
        Intent intent = new Intent(this, DetalhesServicoActivity.class);
        intent.putExtra("servico", servico);
        startActivity(intent);
    }
}