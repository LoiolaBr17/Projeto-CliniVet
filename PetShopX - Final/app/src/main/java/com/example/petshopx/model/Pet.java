package com.example.petshopx.model;

import com.example.petshopx.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Pet implements Serializable {
    private String id;
    private String nome;
    private String tipo;
    private String descricao;
    private String idade;
    private String peso;
    private String altura;
    private String urlImagem;

    public Pet() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }

    public void salvar(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("pets")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        reference.setValue(this);
    }

    public void deletar(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("pets")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        reference.removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                StorageReference storageReference = FirebaseHelper.getStorageReference()
                        .child("imagens")
                        .child("pets")
                        .child(this.getId() + ".jpeg");
                storageReference.delete();
            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
