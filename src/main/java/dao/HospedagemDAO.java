package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.Hospedagem;
import model.Quarto;
import model.Reserva;

public class HospedagemDAO {

    public void cadastrar(Hospedagem hospedagem) throws SQLException {
        String sql = "INSERT INTO hospedagens (reserva_codigo, data_hora_checkin, status) VALUES (?, ?, ?)";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, hospedagem.getReserva().getCodigo());
            stmt.setTimestamp(2, new Timestamp(hospedagem.getDataHoraCheckin().getTime()));
            stmt.setString(3, hospedagem.getStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    hospedagem.setCodigo(rs.getInt(1));
                }
            }
        }
    }

    public Hospedagem buscarPorReserva(int codigoReserva) throws SQLException {
        String sql = montarSelectBase() + " WHERE h.reserva_codigo = ?";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoReserva);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarHospedagem(rs);
                }
            }
        }
        return null;
    }

    public List<Hospedagem> buscarTodas() throws SQLException {
        List<Hospedagem> lista = new ArrayList<>();
        String sql = montarSelectBase() + " ORDER BY h.codigo DESC";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarHospedagem(rs));
            }
        }
        return lista;
    }

    private String montarSelectBase() {
        return "SELECT h.codigo AS hospedagem_codigo, h.data_hora_checkin, h.status AS hospedagem_status, "
             + "r.codigo AS reserva_codigo, r.data_checkin, r.data_checkout, r.quantidade_hospedes, r.status AS reserva_status, "
             + "c.codigo AS cliente_codigo, c.nome_completo, c.cpf, c.telefone, c.email, c.data_nascimento, "
             + "q.codigo AS quarto_codigo, q.numero, q.tipo, q.valor_diaria, q.capacidade_maxima, q.status AS quarto_status "
             + "FROM hospedagens h "
             + "JOIN reservas r ON h.reserva_codigo = r.codigo "
             + "JOIN clientes c ON r.cliente_codigo = c.codigo "
             + "JOIN quartos q ON r.quarto_codigo = q.codigo";
    }

    private Hospedagem montarHospedagem(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setCodigo(rs.getInt("cliente_codigo"));
        cliente.setNomeCompleto(rs.getString("nome_completo"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDataNascimento(rs.getDate("data_nascimento"));

        Quarto quarto = new Quarto();
        quarto.setCodigo(rs.getInt("quarto_codigo"));
        quarto.setNumero(rs.getInt("numero"));
        quarto.setTipo(rs.getString("tipo"));
        quarto.setValorDiaria(rs.getDouble("valor_diaria"));
        quarto.setCapacidadeMaxima(rs.getInt("capacidade_maxima"));
        quarto.setStatus(rs.getString("quarto_status"));

        Reserva reserva = new Reserva();
        reserva.setCodigo(rs.getInt("reserva_codigo"));
        reserva.setCliente(cliente);
        reserva.setQuarto(quarto);
        reserva.setDataCheckin(rs.getDate("data_checkin"));
        reserva.setDataCheckout(rs.getDate("data_checkout"));
        reserva.setQuantidadeHospedes(rs.getInt("quantidade_hospedes"));
        reserva.setStatus(rs.getString("reserva_status"));

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setCodigo(rs.getInt("hospedagem_codigo"));
        hospedagem.setReserva(reserva);
        hospedagem.setDataHoraCheckin(rs.getTimestamp("data_hora_checkin"));
        hospedagem.setStatus(rs.getString("hospedagem_status"));

        return hospedagem;
    }
}