package model;

import java.util.Date;

public class Reserva {
    private int codigo;
    private Cliente cliente;
    private Quarto quarto;
    private Date dataCheckin;
    private Date dataCheckout;
    private int quantidadeHospedes;
    private String status;

    public Reserva() {}

    public Reserva(Cliente cliente, Quarto quarto, Date dataCheckin, Date dataCheckout, int quantidadeHospedes) {
        this.cliente = cliente;
        this.quarto = quarto;
        this.dataCheckin = dataCheckin;
        this.dataCheckout = dataCheckout;
        this.quantidadeHospedes = quantidadeHospedes;
        this.status = "ATIVA";
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Quarto getQuarto() {
        return quarto;
    }
    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public Date getDataCheckin() {
        return dataCheckin;
    }
    public void setDataCheckin(Date dataCheckin) {
        this.dataCheckin = dataCheckin;
    }

    public Date getDataCheckout() {
        return dataCheckout;
    }
    public void setDataCheckout(Date dataCheckout) {
        this.dataCheckout = dataCheckout;
    }

    public int getQuantidadeHospedes() {
        return quantidadeHospedes;
    }
    public void setQuantidadeHospedes(int quantidadeHospedes) {
        this.quantidadeHospedes = quantidadeHospedes;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}