package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteDAO{
    
    public void cadastrar(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO clientes (nome_completo, cpf, telefone, email, data_nascimento) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, cliente.getNomeCompleto());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setDate(5, new java.sql.Date(cliente.getDataNascimento().getTime()));
            stmt.executeUpdate();
        }
    }
    
    public List<Cliente> buscar(String termoBusca) throws SQLException{
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nome_completo LIKE ? OR cpf LIKE ? OR codigo = ?";
        
        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, "%" + termoBusca + "%");
            stmt.setString(2, "%" + termoBusca + "%");
            
            try{
                int codigo = Integer.parseInt(termoBusca);
                stmt.setInt(3, codigo);
            } catch (NumberFormatException e) {
                stmt.setInt(3, -1);
            }
            
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    Cliente cliente = new Cliente();
                    cliente.setCodigo(rs.getInt("codigo"));
                    cliente.setNomeCompleto(rs.getString("nome_completo"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setDataNascimento(rs.getDate("data_nascimento"));
                    lista.add(cliente);
                }
            }
        }
        return lista; 
    }

    public void atualizar(Cliente cliente) throws SQLException{
        String sql = "UPDATE clientes SET nome_completo = ?, cpf = ?, telefone = ?, email = ?, data_nascimento = ? WHERE codigo = ?";
        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, cliente.getNomeCompleto());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setDate(5, new java.sql.Date(cliente.getDataNascimento().getTime()));
            stmt.setInt(6, cliente.getCodigo()); 
            stmt.executeUpdate();
        }
    }

    public void excluir(int codigo) throws SQLException{
        String sql = "DELETE FROM clientes WHERE codigo = ?";
        try (Connection conn = BancoDados.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }
}