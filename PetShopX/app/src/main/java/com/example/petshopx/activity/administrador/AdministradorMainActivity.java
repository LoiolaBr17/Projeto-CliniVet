package com.example.petshopx.activity.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.activity.autenticacao.LoginActivity;
import com.example.petshopx.activity.cliente.ClienteMainActivity;
import com.example.petshopx.activity.comum.MinhaContaActivity;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Pet;
import com.example.petshopx.model.Reserva;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AdministradorMainActivity extends AppCompatActivity {

    private ImageButton ib_menu;
    private TextView txtView_nomeUsuario;
    private EditText text_cancelada, text_reservada, text_pendente, text_concluida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_main);

        iniciaComponentes();

        buscaNomeAdministrador();

        configCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buscaSituacaoDasReservas();
    }

    private void buscaSituacaoDasReservas() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("reservas_all");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cancelada=0, reservada=0, pendente=0, concluida=0;
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Reserva reserva = snap.getValue(Reserva.class);

                        if(reserva.getStatus().equals("cancelada")){
                            cancelada+=1;
                        }else if(reserva.getStatus().equals("reservada")){
                            reservada+=1;
                        }else if(reserva.getStatus().equals("pendente")){
                            pendente+=1;
                        }else if(reserva.getStatus().equals("concluida")){
                            concluida+=1;
                        }
                    }
                    text_cancelada.setText(""+cancelada);
                    text_reservada.setText(""+reservada);
                    text_pendente.setText(""+pendente);
                    text_concluida.setText(""+concluida);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void buscaNomeAdministrador() {
        DatabaseReference userNameReference = FirebaseHelper.getDatabaseReference()
                .child("usuarios").child(FirebaseHelper.getAuth().getUid()).child("nome");;

        userNameReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String value = task.getResult().getValue(String.class);
                    txtView_nomeUsuario.setText(value);
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                }
            }
        });
    }

    private void configCliques(){
        ib_menu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, ib_menu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home_administrador, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.menu_reservas){
                    startActivity(new Intent(this, TodasASReservasActivity.class));
                }else if(menuItem.getItemId() == R.id.menu_servicos){
                    startActivity(new Intent(this, MeusServicosActivity.class));
                }else if(menuItem.getItemId() == R.id.menu_minha_conta){
                    startActivity(new Intent(this, MinhaContaActivity.class));
                }else if(menuItem.getItemId() == R.id.menu_sair){
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(AdministradorMainActivity.this, LoginActivity.class));
                    finish();
                }

                return true;
            });

            popupMenu.show();
        });
    }

    private void iniciaComponentes(){
        ib_menu = findViewById(R.id.ib_menu);
        txtView_nomeUsuario = findViewById(R.id.txtView_nomeUsuario);
        text_cancelada = findViewById(R.id.text_cancelada);
        text_reservada = findViewById(R.id.text_reservada);
        text_pendente = findViewById(R.id.text_pendente);
        text_concluida = findViewById(R.id.text_concluida);
    }
}