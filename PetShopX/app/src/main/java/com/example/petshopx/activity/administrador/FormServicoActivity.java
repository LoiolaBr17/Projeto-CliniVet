package com.example.petshopx.activity.administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.petshopx.R;
import com.example.petshopx.model.Servico;

public class FormServicoActivity extends AppCompatActivity {

    private EditText edit_nome_servico, edit_pets_servico,edit_descricao_servico,edit_preco_servico;
    private ProgressBar progressBar;
    private Servico servico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_servico);

        iniciaComponentes();

        configCliques();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            servico = (Servico) bundle.getSerializable("servico");
            configDados();
        }
    }

    private void configDados(){
        edit_nome_servico.setText(servico.getNome());
        edit_pets_servico.setText(servico.getPets());
        edit_descricao_servico.setText(servico.getDescricao());
        edit_preco_servico.setText(servico.getPreco());
    }

    private void configCliques(){
        findViewById(R.id.ib_salvar).setOnClickListener(view -> validaDados());
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void validaDados(){
        String nome = edit_nome_servico.getText().toString();
        String pets = edit_pets_servico.getText().toString();
        String descricao = edit_descricao_servico.getText().toString();
        String preco = edit_preco_servico.getText().toString();

        if(!nome.isEmpty()){
            if(!pets.isEmpty()){
                if(!descricao.isEmpty()){
                    if(!preco.isEmpty()){

                        if(servico == null){
                            servico = new Servico();
                        }

                        servico.setNome(nome);
                        servico.setPets(pets);
                        servico.setDescricao(descricao);
                        servico.setPreco(preco);

                        progressBar.setVisibility(View.VISIBLE);
                        servico.salvar();
                        finish();

                    }else{
                        edit_preco_servico.requestFocus();
                        edit_preco_servico.setError("Informe o preço do serviço");
                    }
                }else{
                    edit_descricao_servico.requestFocus();
                    edit_descricao_servico.setError("Informe a descrição do serviço");
                }
            }else{
                edit_pets_servico.requestFocus();
                edit_pets_servico.setError("Informe os pets que são atendidos pelo serviço");
            }
        }else{
            edit_nome_servico.requestFocus();
            edit_nome_servico.setError("Informe o nome do serviço");
        }
    }

    private void iniciaComponentes(){
        edit_nome_servico = findViewById(R.id.edit_nome_servico);
        edit_pets_servico = findViewById(R.id.edit_pets_servico);
        edit_descricao_servico = findViewById(R.id.edit_descricao_servico);
        edit_preco_servico = findViewById(R.id.edit_preco_servico);
        progressBar = findViewById(R.id.progressBar);
    }
}