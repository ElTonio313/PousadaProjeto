package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.QuartoController;
import model.Quarto;
import java.util.List;

public class TelaGerenciamentoQuarto extends JFrame{
    private JTextField txtNum, txtTipo, txtValorDiaria, txtCapacidade, txtStatus, txtBusca;
    private JComboBox<String> cbFiltro;
    private JTable tabela;
    private DefaultTableModel modelTabela;

    public TelaGerenciamentoQuarto() {
        setTitle("Gerenciamento de Quartos");
        setSize(850, 380);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblTituloCad = new JLabel("Cadastrar Novo Quarto");
        lblTituloCad.setBounds(20, 15, 200, 25);
        add(lblTituloCad);

        txtNum = criarCampo("Número:", 55);
        txtTipo = criarCampo("Tipo:", 90);
        txtValorDiaria = criarCampo("Valor da diária:", 125);
        txtCapacidade = criarCampo("Capacidade:", 160);
        txtStatus = criarCampo("Status:", 195);
        txtStatus.setText("Livre");

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(120, 245, 150, 30);
        add(btnSalvar);

        JLabel lblFiltro = new JLabel("Buscar por:");
        lblFiltro.setBounds(400, 20, 80, 25);
        add(lblFiltro);

        String[] opcoes = {"Número", "Tipo", "Status"};
        cbFiltro = new JComboBox<>(opcoes);
        cbFiltro.setBounds(480, 20, 100, 25);
        add(cbFiltro);

        txtBusca = new JTextField();
        txtBusca.setBounds(590, 20, 120, 25);
        add(txtBusca);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(720, 20, 90, 25);
        add(btnBuscar);

        String[] colunas = {"Código", "Número", "Tipo", "Valor Diária", "Capacidade", "Status"};
        modelTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modelTabela);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(400, 60, 410, 215);
        add(scrollPane);

        btnSalvar.addActionListener(e -> {
            try{
                Quarto q = new Quarto();
                q.setNumero(Integer.parseInt(txtNum.getText()));
                q.setTipo(txtTipo.getText());
                q.setValorDiaria(Double.parseDouble(txtValorDiaria.getText()));
                q.setCapacidadeMaxima(Integer.parseInt(txtCapacidade.getText()));
                q.setStatus(txtStatus.getText());

                String msg = new QuartoController().salvarQuarto(q);
                JOptionPane.showMessageDialog(this, msg);
                limparCampos();
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, "Erro nos dados: " + ex.getMessage());
            }
        });

        btnBuscar.addActionListener(e -> {
            modelTabela.setRowCount(0);
            String termo = txtBusca.getText();
            String tipoBusca = (String) cbFiltro.getSelectedItem();

            try{
                QuartoController controller = new QuartoController();
                List<Quarto> quartos = controller.buscarQuartos(termo, tipoBusca);

                for (Quarto q : quartos){
                    modelTabela.addRow(new Object[]{
                        q.getCodigo(),
                        q.getNumero(),
                        q.getTipo(),
                        "R$ " + q.getValorDiaria(),
                        q.getCapacidadeMaxima(),
                        q.getStatus()
                    });
                }
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void limparCampos(){
        txtNum.setText("");
        txtTipo.setText("");
        txtValorDiaria.setText("");
        txtCapacidade.setText("");
        txtStatus.setText("Livre");
    }

    private JTextField criarCampo(String label, int y){
        JLabel lbl = new JLabel(label);
        lbl.setBounds(20, y, 100, 25);
        add(lbl);
        JTextField txt = new JTextField();
        txt.setBounds(120, y, 150, 25);
        add(txt);
        return txt;
    }

    public static void main(String[] args){
        new TelaGerenciamentoQuarto();
    }
}