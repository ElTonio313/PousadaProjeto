package controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import dao.BancoDados;
import dao.HospedagemDAO;
import dao.QuartoDAO;
import dao.ReservaDAO;
import model.Hospedagem;
import model.Quarto;
import model.Reserva;

public class HospedagemService {

    private HospedagemDAO hospedagemDAO;
    private ReservaDAO reservaDAO;

    public HospedagemService() {
        this.hospedagemDAO = new HospedagemDAO();
        this.reservaDAO = new ReservaDAO();
    }

    /**
     * HU12 - Realiza o check-in de uma reserva.
     * Critério 1: só reservas ATIVAS (existentes e não canceladas), sem
     * check-in anterior, podem gerar hospedagem.
     * Critério 2: o quarto é atualizado para OCUPADO.
     * Critério 3: a data/hora atual é registrada no momento do check-in.
     */
    public Hospedagem realizarCheckin(int codigoReserva) throws SQLException {
        Reserva reserva = reservaDAO.buscarPorCodigo(codigoReserva);

        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não encontrada.");
        }
        if (!"ATIVA".equals(reserva.getStatus())) {
            throw new IllegalStateException(
                "Apenas reservas ativas podem gerar check-in. Esta reserva está " + reserva.getStatus() + ".");
        }

        boolean jaTemCheckin = hospedagemDAO.buscarPorReserva(codigoReserva) != null;
        if (jaTemCheckin) {
            throw new IllegalStateException("Já existe um check-in registrado para esta reserva.");
        }

        Hospedagem hospedagem = new Hospedagem(reserva, new Date());
        hospedagemDAO.cadastrar(hospedagem);

        Quarto quarto = reserva.getQuarto();
        quarto.setStatus("OCUPADO");
        new QuartoDAO(BancoDados.getConexao()).atualizar(quarto);

        return hospedagem;
    }

    /**
     * Reservas que podem aparecer na tela de check-in: ativas e sem hospedagem ainda.
     */
    public List<Reserva> listarReservasElegiveisParaCheckin() throws SQLException {
        return reservaDAO.listarReservasAtivasSemHospedagem();
    }

    public List<Hospedagem> listarHospedagens() throws SQLException {
        return hospedagemDAO.buscarTodas();
    }
}