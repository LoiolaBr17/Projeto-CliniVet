package com.example.petshopx.activity.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.activity.cliente.DetalhesPetActivity;
import com.example.petshopx.activity.cliente.FormAdicionarPet;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Pet;
import com.example.petshopx.model.Reserva;
import com.example.petshopx.model.Servico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EditarStatusReservaActivity extends AppCompatActivity {

    Reserva reserva;
    TextView text_servico, text_dia, text_horario, text_pet,text_dono,text_telefone;
    private Spinner reserva_spinner;
    String[] statusOptions = new String[]{"cancelada","reservada","pendente","concluida"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_status_reserva);

        iniciaComponentes();

        reserva = (Reserva) getIntent().getSerializableExtra("reserva");

        //config status spinner
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                statusOptions);

        reserva_spinner.setAdapter(adapterStatus);

        configDados();

        configCliques();
    }

    private void configDados(){
        if(reserva != null){
            text_servico.setText(reserva.getServicoNome());
            text_dia.setText(reserva.getDia());
            text_horario.setText(reserva.getHora());
            text_pet.setText(reserva.getPetNome());
            text_dono.setText(reserva.getDonoNome());
            text_telefone.setText(reserva.getTelefoneDono());

            if(reserva.getStatus().equals("cancelada")){
                reserva_spinner.setSelection(0);
            }else if(reserva.getStatus().equals("reservada")){
                reserva_spinner.setSelection(1);
            }else if(reserva.getStatus().equals("pendente")){
                reserva_spinner.setSelection(2);
            }else if(reserva.getStatus().equals("concluida")){
                reserva_spinner.setSelection(3);
            }

        }else{
            Toast.makeText(this, "Não foi possível recuperar as informações.", Toast.LENGTH_SHORT).show();
        }
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());

        reserva_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String novoStatus="";
                if(reserva_spinner.getSelectedItem().equals("pendente")){
                    novoStatus = "pendente";
                    reserva.setStatus("pendente");

                }else if(reserva_spinner.getSelectedItem().equals("reservada")){
                    novoStatus = "reservada";
                    reserva.setStatus("reservada");
                }else if(reserva_spinner.getSelectedItem().equals("cancelada")){
                    novoStatus = "cancelada";
                    reserva.setStatus("cancelada");
                }else if(reserva_spinner.getSelectedItem().equals("concluida")){
                    novoStatus = "concluida";
                    reserva.setStatus("concluida");
                }

                DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                        .child("reservas")
                        .child(reserva.getIdDono())
                        .child(reserva.getId());
                reference.setValue(reserva);

                DatabaseReference reference2 = FirebaseHelper.getDatabaseReference()
                        .child("reservas_all")
                        .child(reserva.getId());
                reference2.setValue(reserva);

                Toast.makeText(EditarStatusReservaActivity.this, "O status da reserva foi atualizado para "+novoStatus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void iniciaComponentes(){
        text_servico = findViewById(R.id.text_servico);
        text_dia = findViewById(R.id.text_dia);
        text_horario = findViewById(R.id.text_horario);
        text_pet = findViewById(R.id.text_pet);
        text_dono = findViewById(R.id.text_dono);
        text_telefone = findViewById(R.id.text_telefone);
        reserva_spinner = findViewById(R.id.reserva_spinner);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Detalhes da Reserva");
    }

    public void detalhesPet(View view){
        Intent intent = new Intent(this, DetalhesPetActivity.class);
        intent.putExtra("pet", reserva.getIdpet());
        intent.putExtra("dono", reserva.getIdDono());
        startActivity(intent);
    }

    public void detalhesServico(View view){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("servicos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Servico servico = snap.getValue(Servico.class);
                        if(servico.getId().equals(reserva.getIdServico())){
                            Intent intent = new Intent(EditarStatusReservaActivity.this, DetalhesServicoActivity.class);
                            intent.putExtra("servico", servico);
                            startActivity(intent);
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