package com.example.petshopx.activity.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.activity.administrador.AdministradorMainActivity;
import com.example.petshopx.activity.cliente.ClienteMainActivity;
import com.example.petshopx.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email, edit_senha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configCliques();

        iniciaComponentes();
    }

    private void configCliques(){
        findViewById(R.id.text_criar_conta).setOnClickListener(view -> {
            startActivity(new Intent(this, CriarContaActivity.class));
        });

        findViewById(R.id.text_recuperar_conta).setOnClickListener(view -> {
            startActivity(new Intent(this, RecuperarContaActivity.class));
        });
    }

    public void validaDados(View view){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);

                logar(email, senha);
            }else{
                edit_senha.findFocus();
                edit_senha.setError("Informe sua senha");
            }
        }else{
            edit_email.findFocus();
            edit_email.setError("Informe seu email");
        }
    }

    private void logar(String email, String senha){
        FirebaseHelper.getAuth().signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String partes[] = edit_email.getText().toString().split("@");

                if(partes[1].equals("clinivet.com")){
                    finish();
                    startActivity(new Intent(this, AdministradorMainActivity.class));
                }else{
                    finish();
                    startActivity(new Intent(this, ClienteMainActivity.class));
                }



            }else{
                String error = task.getException().getMessage();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciaComponentes(){
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);
    }
}