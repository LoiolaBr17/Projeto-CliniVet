package com.example.petshopx.activity.cliente;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petshopx.R;
import com.example.petshopx.helper.FirebaseHelper;
import com.example.petshopx.model.Pet;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FormAdicionarPet extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;

    private EditText edit_nome_pet, edit_tipo_pet, edit_descricao_pet, edit_idade, edit_peso, edit_altura;

    private ImageView img_pet;
    private String caminhoImagem;
    private Bitmap imagem;

    private ProgressBar progressBar;

    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_adicionar_pet);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            pet = (Pet) bundle.getSerializable("pet");

            configDados();
        }

        configCliques();
    }

    private void configDados(){
        Picasso.get().load(pet.getUrlImagem()).into(img_pet);
        edit_nome_pet.setText(pet.getNome());
        edit_tipo_pet.setText(pet.getTipo());
        edit_descricao_pet.setText(pet.getDescricao());
        edit_idade.setText(pet.getIdade());
        edit_peso.setText(pet.getPeso());
        edit_altura.setText(pet.getAltura());
    }

    public void verificaPermissaoGaleria(View view){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormAdicionarPet.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissaoGaleria(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void showDialogPermissaoGaleria(PermissionListener listener, String[] permissoes){
        TedPermission.create()
                .setPermissionListener(listener)
                .setDeniedTitle("Permissão negadas")
                .setDeniedMessage("Você negou as permissões para acessar a galeria do disposivito, deseja permitir ?")
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    private void configCliques(){
        findViewById(R.id.ib_salvar).setOnClickListener(view -> validaDados());
        findViewById(R.id.ib_voltar).setOnClickListener(view -> finish());
    }

    private void validaDados(){
        String nomePet = edit_nome_pet.getText().toString();
        String tipoPet = edit_tipo_pet.getText().toString();
        String descricaoPet = edit_descricao_pet.getText().toString();
        String idadePet = edit_idade.getText().toString();
        String pesoPet = edit_peso.getText().toString();
        String alturaPet = edit_altura.getText().toString();

        if(!nomePet.isEmpty()){
            if(!tipoPet.isEmpty()){
                if(!descricaoPet.isEmpty()){
                    if(!idadePet.isEmpty()){
                        if(!pesoPet.isEmpty()){
                            if(!alturaPet.isEmpty()){

                                if(pet == null) pet = new Pet();

                                pet.setNome(nomePet);
                                pet.setTipo(tipoPet);
                                pet.setDescricao(descricaoPet);
                                pet.setIdade(idadePet);
                                pet.setPeso(pesoPet);
                                pet.setAltura(alturaPet);

                                if(caminhoImagem != null){
                                    salvarImagemPet();
                                }else{
                                    if(pet.getUrlImagem() != null){
                                        pet.salvar();
                                    }else{
                                        Toast.makeText(this, "Selecione uma imagem para o pet.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }else{
                                edit_altura.requestFocus();
                                edit_altura.setError("Informe a altura do pet.");
                            }
                        }else{
                            edit_peso.requestFocus();
                            edit_peso.setError("Informe o peso do pet.");
                        }
                    }else{
                        edit_idade.requestFocus();
                        edit_idade.setError("Informe a idade do pet.");
                    }
                }else{
                    edit_descricao_pet.requestFocus();
                    edit_descricao_pet.setError("Informe a descrição do pet.");
                }
            }else{
                edit_tipo_pet.requestFocus();
                edit_tipo_pet.setError("Informe o tipo do pet.");
            }
        }else{
            edit_nome_pet.requestFocus();
            edit_nome_pet.setError("Informe o nome do pet.");
        }
    }

    private void salvarImagemPet(){
        progressBar.setVisibility(View.VISIBLE);

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("pets")
                .child(pet.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                String urlImagem = task.getResult().toString();
                pet.setUrlImagem(urlImagem);
                pet.salvar();

                finish();
            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void iniciaComponentes(){
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Cadastro de pet");

        progressBar = findViewById(R.id.progressBar);

        edit_nome_pet = findViewById(R.id.edit_nome_pet);
        edit_tipo_pet = findViewById(R.id.edit_tipo_pet);
        edit_descricao_pet = findViewById(R.id.edit_descricao_pet);
        edit_idade = findViewById(R.id.text_idade);
        edit_peso = findViewById(R.id.text_peso);
        edit_altura = findViewById(R.id.text_altura);
        img_pet = findViewById(R.id.img_pet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_GALERIA){
                Uri localImagemSelecionada = data.getData();
                caminhoImagem = localImagemSelecionada.toString();

                if(Build.VERSION.SDK_INT < 28){
                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), localImagemSelecionada);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), localImagemSelecionada);
                    try {
                        imagem = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                img_pet.setImageBitmap(imagem);
            }
        }
    }
}