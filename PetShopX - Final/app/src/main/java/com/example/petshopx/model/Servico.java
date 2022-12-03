package com.example.petshopx.model;

import com.example.petshopx.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Servico implements Serializable {
    private String id;
    private String nome;
    private String pets;
    private String descricao;
    private String preco;

    public Servico() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }

    public void salvar(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("servicos")
                .child(this.getId());
        reference.setValue(this);
    }

    public void deletar(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("servicos")
                .child(this.getId());
        reference.removeValue();
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

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
