package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDados{
    
    private static final String URL = "jdbc:mysql://localhost:3306/pousada_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection conexao;

    public static Connection getConexao(){
        try{
            if (conexao == null || conexao.isClosed()){
                conexao = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("SUCESSO: Conexão com o banco de dados estabelecida!");
            }
        }catch (SQLException e){
            System.err.println("ERRO: Não foi possível conectar ao banco de dados.");
            e.printStackTrace();
        }
        return conexao;
    }

    public static void fecharConexao(){
        try{
            if (conexao != null && !conexao.isClosed()){
                conexao.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        }catch (SQLException e){
            System.err.println("Erro ao fechar a conexão com o banco de dados.");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        Connection conn = BancoDados.getConexao();
        if (conn != null){
            BancoDados.fecharConexao();
        }
    }
}