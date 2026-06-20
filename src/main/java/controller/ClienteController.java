package controller;

import dao.ClienteDAO;
import model.Cliente;
import java.sql.SQLException;

public class ClienteController{
    
    public String salvarCliente(Cliente cliente){
        if (cliente.getNomeCompleto().isEmpty() || cliente.getCpf().isEmpty()) {
            return "Erro: Nome e CPF são obrigatórios!";
        }
        
        try{
            ClienteDAO dao = new ClienteDAO();
            dao.cadastrar(cliente);
            return "Cliente cadastrado com sucesso!";
        } catch (SQLException e) {
            return "Erro ao salvar no banco: " + e.getMessage();
        }
    }
}