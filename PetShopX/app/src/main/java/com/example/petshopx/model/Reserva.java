package com.example.petshopx.model;

import com.example.petshopx.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Reserva implements Serializable {
    private String id;
    private String idpet;
    private String idDono;
    private String idServico;
    private String petNome;
    private String donoNome;
    private String servicoNome;
    private String telefoneDono;
    private String imgPet;
    private String dia;
    private String hora;
    private String status;

    public Reserva(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        this.setId(reference.push().getKey());
    }

    public void salvar(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("reservas")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        reference.setValue(this);

        DatabaseReference reference2 = FirebaseHelper.getDatabaseReference()
                .child("reservas_all")
                .child(this.getId());
        reference2.setValue(this);
    }

    public void cancelarReserva(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("reservas")
                .child(this.getIdDono())
                .child(this.getId());
        reference.setValue(this);

        DatabaseReference reference2 = FirebaseHelper.getDatabaseReference()
                .child("reservas_all")
                .child(this.getId());
        reference2.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdpet() {
        return idpet;
    }

    public void setIdpet(String idpet) {
        this.idpet = idpet;
    }

    public String getIdDono() {
        return idDono;
    }

    public void setIdDono(String idDono) {
        this.idDono = idDono;
    }

    public String getIdServico() {
        return idServico;
    }

    public void setIdServico(String idServico) {
        this.idServico = idServico;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPetNome() {
        return petNome;
    }

    public void setPetNome(String petNome) {
        this.petNome = petNome;
    }

    public String getDonoNome() {
        return donoNome;
    }

    public void setDonoNome(String donoNome) {
        this.donoNome = donoNome;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public String getTelefoneDono() {
        return telefoneDono;
    }

    public void setTelefoneDono(String telefoneDono) {
        this.telefoneDono = telefoneDono;
    }

    public String getImgPet() {
        return imgPet;
    }

    public void setImgPet(String imgPet) {
        this.imgPet = imgPet;
    }
}
