package com.example.petshopx.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.petshopx.R;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetalhesPetActivity extends AppCompatActivity {

    private TextView text_nome, text_tipo, text_descricao, text_idade, text_peso, text_altura;

    private ProgressBar progressBar;

    private ImageView img_pet;

    private String petID;
    private String donoID;

    private Pet petDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pet);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            petID = (String) bundle.getSerializable("pet");
            donoID = (String) bundle.getSerializable("dono");

            recuperaDados();
        }

        configCliques();
    }

    private void recuperaDados() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("pets")
                .child(donoID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Pet pet = snap.getValue(Pet.class);
                       if(pet.getId().equals(petID)){
                           petDetalhes = pet;
                       }
                       configDados();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados(){
        Picasso.get().load(petDetalhes.getUrlImagem()).into(img_pet);
        text_nome.setText(petDetalhes.getNome());
        text_tipo.setText(petDetalhes.getTipo());
        text_descricao.setText(petDetalhes.getDescricao());
        text_idade.setText(petDetalhes.getIdade());
        text_peso.setText(petDetalhes.getPeso());
        text_altura.setText(petDetalhes.getAltura());
        progressBar.setVisibility(View.GONE);
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void iniciaComponentes() {
        text_nome = findViewById(R.id.text_nome);
        text_tipo = findViewById(R.id.text_tipo);
        text_descricao = findViewById(R.id.text_descricao);
        text_idade = findViewById(R.id.text_idade);
        text_peso = findViewById(R.id.text_peso);
        text_altura = findViewById(R.id.text_altura);
        progressBar = findViewById(R.id.progressBar);
        img_pet = findViewById(R.id.img_pet);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Informações do Pet");
    }
}