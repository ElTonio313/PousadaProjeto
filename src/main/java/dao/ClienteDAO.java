package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            System.out.println("Cliente cadastrado com sucesso no banco!");
        }
    }
}