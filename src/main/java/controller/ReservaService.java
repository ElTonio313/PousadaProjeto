package controller;

import java.sql.SQLException;
import java.util.List;

import dao.BancoDados;
import dao.QuartoDAO;
import dao.ReservaDAO;
import model.Cliente;
import model.Quarto;
import model.Reserva;

public class ReservaService {

    private ReservaDAO reservaDAO;

    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
    }

    /**
     * HU9 - Registra uma nova reserva.
     * Valida os dados, checa disponibilidade do quarto no período,
     * grava a reserva e marca o quarto como RESERVADO.
     */
    public Reserva registrarReserva(Cliente cliente, Quarto quarto, java.util.Date dataCheckin,
            java.util.Date dataCheckout, int quantidadeHospedes) throws SQLException {

        if (cliente == null) {
            throw new IllegalArgumentException("Selecione um cliente.");
        }
        if (quarto == null) {
            throw new IllegalArgumentException("Selecione um quarto.");
        }
        if (dataCheckin == null || dataCheckout == null) {
            throw new IllegalArgumentException("Informe as datas de check-in e check-out.");
        }
        if (!dataCheckout.after(dataCheckin)) {
            throw new IllegalArgumentException("A data de check-out deve ser posterior à data de check-in.");
        }
        if (quantidadeHospedes <= 0) {
            throw new IllegalArgumentException("A quantidade de hóspedes deve ser maior que zero.");
        }
        if (quantidadeHospedes > quarto.getCapacidadeMaxima()) {
            throw new IllegalArgumentException(
                "A quantidade de hóspedes excede a capacidade máxima do quarto (" + quarto.getCapacidadeMaxima() + ").");
        }

        boolean disponivel = reservaDAO.verificarDisponibilidade(quarto.getCodigo(), dataCheckin, dataCheckout);
        if (!disponivel) {
            throw new IllegalStateException("O quarto " + quarto.getNumero() + " não está disponível para o período informado.");
        }

        Reserva reserva = new Reserva(cliente, quarto, dataCheckin, dataCheckout, quantidadeHospedes);
        reservaDAO.cadastrar(reserva);

        quarto.setStatus("RESERVADO");
        new QuartoDAO(BancoDados.getConexao()).atualizar(quarto);

        return reserva;
    }

    /**
     * HU10 - Cancela uma reserva existente e libera o quarto (status DISPONÍVEL).
     * A confirmação do usuário (critério 3) deve ser tratada na tela,
     * antes de chamar este método.
     */
    public void cancelarReserva(int codigoReserva) throws SQLException {
        Reserva reserva = reservaDAO.buscarPorCodigo(codigoReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não encontrada.");
        }
        if ("CANCELADA".equals(reserva.getStatus())) {
            throw new IllegalStateException("Esta reserva já está cancelada.");
        }

        reservaDAO.cancelar(codigoReserva);

        Quarto quarto = reserva.getQuarto();
        quarto.setStatus("DISPONÍVEL");
        new QuartoDAO(BancoDados.getConexao()).atualizar(quarto);
    }

    /**
     * HU11 - Lista todas as reservas cadastradas, já com cliente, quarto e status.
     */
    public List<Reserva> listarReservas() throws SQLException {
        return reservaDAO.buscarTodas();
    }
}