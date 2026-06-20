package model;

import java.util.Date;

public class Cliente{
    private int codigo;
    private String nomeCompleto;
    private String cpf;
    private String telefone;
    private String email;
    private Date dataNascimento;
    
    public Cliente() {}

    public Cliente(String nomeCompleto, String cpf, String telefone, String email, Date dataNascimento){
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public int getCodigo(){
    	return codigo; 
    }
    public void setCodigo(int codigo){
    	this.codigo = codigo;
    }
    public String getNomeCompleto(){ 
    	return nomeCompleto; 
    }
    public void setNomeCompleto(String nomeCompleto){
    	this.nomeCompleto = nomeCompleto; 
    }
    public String getCpf(){ 
    	return cpf; 
    }
    public void setCpf(String cpf){
    	this.cpf = cpf; 
    }

    public String getTelefone(){
        return telefone;
    }
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public Date getDataNascimento(){
        return dataNascimento;
    }
    public void setDataNascimento(Date dataNascimento){
        this.dataNascimento = dataNascimento;
    }
}