package model;

public class Quarto {
    private int codigo;
    private int numero;
    private String tipo;
    private double valor_diaria;
    private String status;
    private int capacidadeMaxima;

    public Quarto() {}

    public int getCodigo() {
    	return codigo; 
    }
    public void setCodigo(int codigo) {
    	this.codigo = codigo;
    	}
    public int getNumero(){
    	return numero; 
    }
    public void setNumero(int numero){ 
    	this.numero = numero; 
    }
    public String getTipo(){
    	return tipo; 
    }
    public void setTipo(String tipo) { this.tipo = tipo; 
    }
    public double getValorDiaria(){
    	return valor_diaria; 
    }
    public void setValorDiaria(double valor_diaria){
    	this.valor_diaria = valor_diaria; 
    }
    public String getStatus(){
    	return status; 
    }
    public void setStatus(String status){
    	this.status = status; 
    }
    
    public int getCapacidadeMaxima(){ 
    	return capacidadeMaxima;
    }
    public void setCapacidadeMaxima(int capacidadeMaxima){
    	this.capacidadeMaxima = capacidadeMaxima; 
    }
}
