package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import controller.ClienteController;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TelaGerenciamentoCliente extends JFrame{

    private JTextField txtCodigo, txtNome, txtCpf, txtTelefone, txtEmail, txtDataNascimento, txtBusca;
    private JTable tabela;
    private DefaultTableModel modelTabela;
    
    private List<Cliente> listaClientesAtual = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public TelaGerenciamentoCliente(){
        setTitle("Gerenciamento Geral de Clientes");
        setSize(850, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblBusca = new JLabel("Buscar por Nome/CPF/Cód:");
        lblBusca.setBounds(20, 20, 180, 25);
        add(lblBusca);

        txtBusca = new JTextField();
        txtBusca.setBounds(200, 20, 220, 25);
        add(txtBusca);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(430, 20, 100, 25);
        add(btnBuscar);

        String[] colunas = {"Cód", "Nome", "CPF", "Telefone"};
        modelTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelTabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBounds(400, 60, 410, 320);
        add(scroll);

        txtCodigo = criarCampo("Código:", 60);
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(new Color(230, 230, 230));
        
        txtNome = criarCampo("Nome Completo*:", 95);
        txtCpf = criarCampo("CPF*:", 130);
        txtTelefone = criarCampo("Telefone:", 165);
        txtEmail = criarCampo("E-mail:", 200);
        txtDataNascimento = criarCampo("Data Nasc (dd/mm/aaaa):", 235);

        JButton btnCadastrar = new JButton("Cadastrar Novo");
        btnCadastrar.setBounds(20, 285, 160, 30);
        add(btnCadastrar);

        JButton btnAtualizar = new JButton("Salvar Alterações");
        btnAtualizar.setBounds(200, 285, 160, 30);
        add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir Cadastro");
        btnExcluir.setForeground(Color.RED);
        btnExcluir.setBounds(20, 330, 160, 30);
        add(btnExcluir);

        JButton btnLimpar = new JButton("Limpar Tela");
        btnLimpar.setBounds(200, 330, 160, 30);
        add(btnLimpar);

        // -----------------------------------------------------------
        
        btnBuscar.addActionListener(e ->{
            atualizarTabela(txtBusca.getText());
        });

        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada >= 0 && linhaSelecionada < listaClientesAtual.size()){
                Cliente selecionado = listaClientesAtual.get(linhaSelecionada);
                txtCodigo.setText(String.valueOf(selecionado.getCodigo()));
                txtNome.setText(selecionado.getNomeCompleto());
                txtCpf.setText(selecionado.getCpf());
                txtTelefone.setText(selecionado.getTelefone());
                txtEmail.setText(selecionado.getEmail());
                txtDataNascimento.setText(selecionado.getDataNascimento() != null ? sdf.format(selecionado.getDataNascimento()) : "");
            }
        });

        btnCadastrar.addActionListener(e -> {
            try{
                Cliente c = obterDadosFormulario();
                String msg = new ClienteController().salvarCliente(c);
                JOptionPane.showMessageDialog(this, msg);
                limparCampos();
                atualizarTabela("");
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, "Erro nos dados: " + ex.getMessage());
            }
        });

        btnAtualizar.addActionListener(e ->{
            if (txtCodigo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na lista ao lado para alterar!");
                return;
            }
            try{
                Cliente c = obterDadosFormulario();
                c.setCodigo(Integer.parseInt(txtCodigo.getText()));
                
                String msg = new ClienteController().atualizarCliente(c);
                JOptionPane.showMessageDialog(this, msg);
                limparCampos();
                atualizarTabela(""); 
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            if (txtCodigo.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Selecione um cliente na lista para excluir!");
                return;
            }
            int conf = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do cliente " + txtNome.getText() + "?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION){
                int cod = Integer.parseInt(txtCodigo.getText());
                String msg = new ClienteController().excluirCliente(cod);
                JOptionPane.showMessageDialog(this, msg);
                limparCampos();
                atualizarTabela("");
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        setVisible(true);
    }

    private void atualizarTabela(String termo){
        modelTabela.setRowCount(0);
        ClienteController ctrl = new ClienteController();
        
        listaClientesAtual = ctrl.buscarClientes(termo);
        
        if (listaClientesAtual.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum cliente retornado para os critérios informados.");
            return;
        }

        for (Cliente c : listaClientesAtual){
            modelTabela.addRow(new Object[]{
                c.getCodigo(),
                c.getNomeCompleto(),
                c.getCpf(),
                c.getTelefone()
            });
        }
    }

    private Cliente obterDadosFormulario() throws Exception{
        Cliente c = new Cliente();
        c.setNomeCompleto(txtNome.getText().trim());
        c.setCpf(txtCpf.getText().trim());
        c.setTelefone(txtTelefone.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        try{
            c.setDataNascimento(sdf.parse(txtDataNascimento.getText().trim()));
        }catch (java.text.ParseException e){
            throw new Exception("Formato de data inválido! Digite dd/mm/aaaa.");
        }
        return c;
    }

    private void limparCampos(){
        txtCodigo.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtDataNascimento.setText("");
        tabela.clearSelection();
    }

    private JTextField criarCampo(String label, int y){
        JLabel lbl = new JLabel(label);
        lbl.setBounds(20, y, 150, 25);
        add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(170, y, 200, 25);
        add(txt);
        return txt;
    }

    public static void main(String[] args){
        new TelaGerenciamentoCliente();
    }
}