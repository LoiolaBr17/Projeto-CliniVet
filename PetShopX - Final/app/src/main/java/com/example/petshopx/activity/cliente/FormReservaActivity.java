package com.example.petshopx.activity.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Pet;
import com.example.petshopx.model.Reserva;
import com.example.petshopx.model.Servico;
import com.example.petshopx.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormReservaActivity extends AppCompatActivity {

    Servico servico;
    private TextView nome_servico;

    private DatePickerDialog dataPickerDialog;
    private Button dateButton,btn_reservar;

    private List<Pet> petList = new ArrayList<>();
    private List<Pet> petListData = new ArrayList<>();

    String[] items;
    String[] horarios = new String[]{"7:00","8:00","9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00"};
    ArrayList<String> pets = new ArrayList<>();
    private Spinner spinner,hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reserva);

        iniciaComponentes();

        configCliques();

        servico = (Servico) getIntent().getSerializableExtra("servico");
        nome_servico.setText(servico.getNome());

        // date button
        initDataPicker();
        dateButton = findViewById(R.id.dataPickerButton);
        dateButton.setText(getTodaysDate());

        //select pet
        recuperaPets();

        //config hora
        ArrayAdapter<String> adapterhora = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                horarios);

        hora.setAdapter(adapterhora);
    }

    private void recuperaPets(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("pets")
                .child(FirebaseHelper.getIdFirebase());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    petList.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Pet pet = snap.getValue(Pet.class);
                        petList.add(pet);
                    }


                    for(Pet pet : petList){
                        pets.add(pet.getNome());
                        petListData.add(pet);
                    }

                    items = new String[pets.size()];

                    for(int i=0; i<pets.size();i++){
                        items[i] = pets.get(i);
                    }

                }else{
                    items = new String[]{"Não há pets cadastrados"};

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        items);

                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(view -> {
            finish();
        });
        btn_reservar.setOnClickListener(view -> {
            if(spinner.getSelectedItem().toString().equals("Não há pets cadastrados")){
                Toast.makeText(this, "adicione um pet para prosseguir com a reserva", Toast.LENGTH_SHORT).show();
            }else{
                confirmarReserva();
            }
        });
    }

    private void confirmarReserva(){
        Reserva reserva = new Reserva();

        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Usuario usuario = snap.getValue(Usuario.class);
                        if(usuario.getId().equals(FirebaseHelper.getIdFirebase())){
                            reserva.setDonoNome(usuario.getNome());
                            reserva.setTelefoneDono(usuario.getTelefone());
                        }
                    }

                    for(Pet pet : petListData){
                        if(pet.getNome() == spinner.getSelectedItem().toString()){
                            reserva.setIdpet(pet.getId());
                            reserva.setPetNome(pet.getNome());
                            reserva.setImgPet(pet.getUrlImagem());
                        }
                    }

                    reserva.setIdDono(FirebaseHelper.getIdFirebase());

                    reserva.setIdServico(servico.getId());
                    reserva.setServicoNome(servico.getNome());

                    reserva.setDia(dateButton.getText().toString());

                    reserva.setHora(hora.getSelectedItem().toString());

                    reserva.setStatus("pendente");

                    reserva.salvar();

                    Toast.makeText(FormReservaActivity.this, "Reserva realizada", Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void iniciaComponentes(){
        nome_servico = findViewById(R.id.nome_servico);
        dateButton = findViewById(R.id.dataPickerButton);
        btn_reservar = findViewById(R.id.btn_reservar);
        spinner = findViewById(R.id.spinner);
        hora = findViewById(R.id.hora);

        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Nova Reserva");
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDataString(day, month, year);
    }

    public void initDataPicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);
                m += 1;
                int d = cal.get(Calendar.DAY_OF_MONTH);

                if(year>=y){
                    if(year == y){
                        if(month>=m){
                            if(month>m){
                                String date = makeDataString(day,month,year);
                                dateButton.setText(date);
                            }

                            if(month==m){
                                if(day>=d){
                                    String date = makeDataString(day,month,year);
                                    dateButton.setText(date);
                                }else{
                                    Toast.makeText(FormReservaActivity.this, "Esse dia é anterior ao atual.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else{
                            Toast.makeText(FormReservaActivity.this, "Esse mês é anterior ao atual.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(year > y){
                        String date = makeDataString(day,month,year);
                        dateButton.setText(date);
                    }

                }else{
                    Toast.makeText(FormReservaActivity.this, "Esse ano é anterior ao atual.", Toast.LENGTH_SHORT).show();
                }

            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dataPickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private String makeDataString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;

    }

    private String getMonthFormat(int month){
        if(month == 1) return "Jan";
        if(month == 2) return "Fev";
        if(month == 3) return "Mar";
        if(month == 4) return "Abr";
        if(month == 5) return "Mai";
        if(month == 6) return "Jun";
        if(month == 7) return "Jul";
        if(month == 8) return "Ago";
        if(month == 9) return "Set";
        if(month == 10) return "Out";
        if(month == 11) return "Nov";
        if(month == 12) return "Dez";
        return "Jan";
    }

    public void openDataPicker(View view){
        dataPickerDialog.show();
    }
}