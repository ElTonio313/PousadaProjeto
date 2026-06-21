package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.Quarto;
import model.Reserva;

public class ReservaDAO {

    public void cadastrar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (cliente_codigo, quarto_codigo, data_checkin, data_checkout, quantidade_hospedes, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reserva.getCliente().getCodigo());
            stmt.setInt(2, reserva.getQuarto().getCodigo());
            stmt.setDate(3, new java.sql.Date(reserva.getDataCheckin().getTime()));
            stmt.setDate(4, new java.sql.Date(reserva.getDataCheckout().getTime()));
            stmt.setInt(5, reserva.getQuantidadeHospedes());
            stmt.setString(6, reserva.getStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reserva.setCodigo(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Verifica se o quarto está livre no período informado, considerando
     * apenas reservas com status ATIVA. Duas reservas se sobrepõem quando
     * uma começa antes da outra terminar e termina depois da outra começar.
     */
    public boolean verificarDisponibilidade(int quartoCodigo, java.util.Date dataCheckin, java.util.Date dataCheckout) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservas "
                   + "WHERE quarto_codigo = ? AND status = 'ATIVA' "
                   + "AND data_checkin < ? AND data_checkout > ?";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quartoCodigo);
            stmt.setDate(2, new java.sql.Date(dataCheckout.getTime()));
            stmt.setDate(3, new java.sql.Date(dataCheckin.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }

    public void cancelar(int codigoReserva) throws SQLException {
        String sql = "UPDATE reservas SET status = 'CANCELADA' WHERE codigo = ?";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoReserva);
            stmt.executeUpdate();
        }
    }

    public Reserva buscarPorCodigo(int codigo) throws SQLException {
        String sql = montarSelectBase() + " WHERE r.codigo = ?";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarReserva(rs);
                }
            }
        }
        return null;
    }

    public List<Reserva> buscarTodas() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = montarSelectBase() + " ORDER BY r.codigo DESC";

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarReserva(rs));
            }
        }
        return lista;
    }

    private String montarSelectBase() {
        return "SELECT r.codigo AS reserva_codigo, r.data_checkin, r.data_checkout, "
             + "r.quantidade_hospedes, r.status AS reserva_status, "
             + "c.codigo AS cliente_codigo, c.nome_completo, c.cpf, c.telefone, c.email, c.data_nascimento, "
             + "q.codigo AS quarto_codigo, q.numero, q.tipo, q.valor_diaria, q.capacidade_maxima, q.status AS quarto_status "
             + "FROM reservas r "
             + "JOIN clientes c ON r.cliente_codigo = c.codigo "
             + "JOIN quartos q ON r.quarto_codigo = q.codigo";
    }

    private Reserva montarReserva(ResultSet rs) throws SQLException {
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

        return reserva;
    }
}