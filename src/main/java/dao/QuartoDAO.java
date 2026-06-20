package dao;

import java.sql.*;
import model.Quarto;
import java.util.ArrayList;
import java.util.List;

public class QuartoDAO {
    public void cadastrar(Quarto quarto) throws SQLException{
        String sql = "INSERT INTO quartos (numero, tipo, valor_diaria, capacidade_maxima, status) VALUES (?, ?, ?, ?, ?)";
        
        try(Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, quarto.getNumero());
            stmt.setString(2, quarto.getTipo());
            stmt.setDouble(3, quarto.getValorDiaria());
            stmt.setInt(4, quarto.getCapacidadeMaxima());
            stmt.setString(5, quarto.getStatus());
            
            stmt.executeUpdate();
        }
    }
    public List<Quarto> buscar(String valor, String tipoBusca) throws SQLException{
        List<Quarto> lista = new ArrayList<>();
        String sql = "SELECT * FROM quartos WHERE ";
        
        if ("Número".equals(tipoBusca)){
            sql += "numero = ?";
        } else if ("Tipo".equals(tipoBusca)){
            sql += "tipo LIKE ?";
        } else if ("Status".equals(tipoBusca)){
            sql += "status LIKE ?";
        }

        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            
            if ("Número".equals(tipoBusca)) {
                stmt.setInt(1, Integer.parseInt(valor));
            } else {
                stmt.setString(1, "%" + valor + "%");
            }
            
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    Quarto q = new Quarto();
                    q.setCodigo(rs.getInt("codigo"));
                    q.setNumero(rs.getInt("numero"));
                    q.setTipo(rs.getString("tipo"));
                    q.setValorDiaria(rs.getDouble("valor_diaria"));
                    q.setCapacidadeMaxima(rs.getInt("capacidade_maxima"));
                    q.setStatus(rs.getString("status"));
                    lista.add(q);
                }
            }
        }
        return lista;
    }
}