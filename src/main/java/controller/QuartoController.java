package controller;

import dao.BancoDados;
import dao.QuartoDAO;
import model.Quarto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class QuartoController{
    public String salvarQuarto(Quarto quarto){
    	Connection conn = BancoDados.getConexao();
        if (quarto.getNumero() <= 0 || quarto.getTipo().isEmpty() || quarto.getCapacidadeMaxima() <= 0){
            return "Erro: Todos os campos são obrigatórios!";
        }
        try{
            QuartoDAO dao = new QuartoDAO(conn);
            dao.cadastrar(quarto);
            return "Quarto cadastrado com sucesso!";
        }catch (SQLException e){
            return "Erro ao salvar quarto: " + e.getMessage();
        }
    }
    
    public java.util.List<Quarto> buscarQuartos(String valor, String tipoBusca) throws Exception{
    	Connection conn = BancoDados.getConexao();
        if (valor == null || valor.trim().isEmpty()) {
            throw new Exception("Por favor, digite um termo para a busca!");
        }
        
        if ("Número".equals(tipoBusca)){
            try {
                Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                throw new Exception("Para buscar por número, digite um valor inteiro válido!");
            }
        }
        
        QuartoDAO dao = new QuartoDAO(conn);
        java.util.List<Quarto> resultados = dao.buscar(valor, tipoBusca);
        
        if (resultados.isEmpty()){
            throw new Exception("Nenhum quarto encontrado com os critérios informados.");
        }
        
        return resultados;
    }
    
    public int atualizar(Quarto quarto) throws SQLException, IOException{
    	Connection conn = BancoDados.getConexao();
    	return new QuartoDAO(conn).atualizar(quarto);
    }
    
    public int ecluir(int id) throws SQLException, IOException{
    	Connection conn = BancoDados.getConexao();
    	return new QuartoDAO(conn).excluir(id);
    }
    
    public Quarto buscarCodigo(int codigo) throws SQLException, IOException{
    	Connection conn = BancoDados.getConexao();
    	return new QuartoDAO(conn).buscarPorCod(codigo);
    }
    
}