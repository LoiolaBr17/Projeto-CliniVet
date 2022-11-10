package com.example.petshopx.activity.comum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.petshopx.R;
import com.example.petshopx.activity.administrador.AdministradorMainActivity;
import com.example.petshopx.activity.autenticacao.LoginActivity;
import com.example.petshopx.activity.cliente.ClienteMainActivity;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Reserva;
import com.example.petshopx.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseHelper.getAutenticado()){
                    String idUser = FirebaseHelper.getAuth().getUid();

                    DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                            .child("usuarios");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot snap : snapshot.getChildren()){
                                    Usuario usuario = snap.getValue(Usuario.class);

                                    if(usuario.getId().equals(idUser)){
                                        String partes[] = usuario.getEmail().split("@");

                                        if(partes[1].equals("clinivet.com")){
                                            startActivity(new Intent(SplashScreenActivity.this, AdministradorMainActivity.class));
                                        }else{
                                            startActivity(new Intent(SplashScreenActivity.this, ClienteMainActivity.class));
                                        }
                                        finish();
                                    }

                                }

                            }else{
                                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1000);
    }
}