package controller;

import dao.ClienteDAO;
import model.Cliente;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteController{
    
    public String salvarCliente(Cliente cliente){
        if (cliente.getNomeCompleto().isEmpty() || cliente.getCpf().isEmpty()) {
            return "Erro: Nome e CPF são obrigatórios!";
        }
        try{
            ClienteDAO dao = new ClienteDAO();
            dao.cadastrar(cliente);
            return "Cliente cadastrado com sucesso!";
        }catch (SQLException e){
            return "Erro ao salvar no banco: " + e.getMessage();
        }
    }

    public List<Cliente> buscarClientes(String termoBusca){
        if (termoBusca == null){
            return new ArrayList<>();
        }
        try{
            ClienteDAO dao = new ClienteDAO();
            return dao.buscar(termoBusca.trim());
        }catch (SQLException e){
            System.out.println("Erro ao buscar no banco: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String atualizarCliente(Cliente cliente){
        if (cliente.getCodigo() <= 0) {
            return "Erro: Selecione um cliente na tabela primeiro!";
        }
        if (cliente.getNomeCompleto().isEmpty() || cliente.getCpf().isEmpty()){
            return "Erro: Nome e CPF são obrigatórios!";
        }
        try{
            ClienteDAO dao = new ClienteDAO();
            dao.atualizar(cliente);
            return "Dados do cliente atualizados com sucesso!";
        }catch (SQLException e) {
            return "Erro ao atualizar no banco: " + e.getMessage();
        }
    }

    public String excluirCliente(int codigo){
        if (codigo <= 0) {
            return "Erro: Selecione um cliente válido para exclusão!";
        }
        try{
            ClienteDAO dao = new ClienteDAO();
            dao.excluir(codigo);
            return "Cliente removido com sucesso!";
        }catch (SQLException e){
            return "Erro ao excluir do banco: " + e.getMessage();
        }
    }
}