package model;

import java.util.Date;

public class Hospedagem {
    private int codigo;
    private Reserva reserva;
    private Date dataHoraCheckin;
    private String status;

    public Hospedagem() {}

    public Hospedagem(Reserva reserva, Date dataHoraCheckin) {
        this.reserva = reserva;
        this.dataHoraCheckin = dataHoraCheckin;
        this.status = "EM_ANDAMENTO";
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Reserva getReserva() {
        return reserva;
    }
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Date getDataHoraCheckin() {
        return dataHoraCheckin;
    }
    public void setDataHoraCheckin(Date dataHoraCheckin) {
        this.dataHoraCheckin = dataHoraCheckin;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}