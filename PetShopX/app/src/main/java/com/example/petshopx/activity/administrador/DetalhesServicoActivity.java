package com.example.petshopx.activity.administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.activity.cliente.FormReservaActivity;
import com.example.petshopx.model.Servico;

public class DetalhesServicoActivity extends AppCompatActivity {

    private TextView titulo_servico, descricao_servico,preco_servico;
    private Button btn_reservar;
    private Servico servico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_servico);

        iniciaComponentes();

        configCliques();

        servico = (Servico) getIntent().getSerializableExtra("servico");
        configDados();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
        findViewById(R.id.btn_reservar).setOnClickListener(view -> {
            Intent intent = new Intent(this, FormReservaActivity.class);
            intent.putExtra("servico", servico);
            startActivity(intent);
            finish();
        });
    }

    private void configDados(){
        if(servico != null){
            titulo_servico.setText(servico.getNome());
            descricao_servico.setText(servico.getDescricao());
            preco_servico.setText(servico.getPreco());
        }else{
            Toast.makeText(this, "Não foi possível recuperar as informações.", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciaComponentes(){
        titulo_servico = findViewById(R.id.titulo_servico);
        descricao_servico = findViewById(R.id.descricao_servico);
        preco_servico = findViewById(R.id.preco_servico);
        btn_reservar = findViewById(R.id.btn_reservar);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Detalhes do Serviço");
    }
}