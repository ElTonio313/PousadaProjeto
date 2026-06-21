package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.QuartoController;
import model.Quarto;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TelaGerenciamentoQuarto extends JFrame{
    private JTextField txtNum, txtTipo, txtValorDiaria, txtCapacidade, txtStatus, txtBusca;
    private JComboBox<String> cbFiltro;
    private JTable tabela;
    private DefaultTableModel modelTabela;
    
    public TelaGerenciamentoQuarto() {
        this.iniciarComponentes();
    }
    
    private void iniciarComponentes() {
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
        btnSalvar.setBounds(20, 245, 150, 30);
        add(btnSalvar);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(20, 295, 150, 30);
        add(btnAtualizar);
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(200, 245, 150, 30);
        add(btnExcluir);
        
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

        btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrar();
			}
		});

        btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
        
        btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Informe o ID do quarto que deseja atualizar", "Atualização", JOptionPane.QUESTION_MESSAGE));
				atualizar(id);
			}
		});
        
        btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Informe o ID do quarto que deseja excluir", "Exclusão", JOptionPane.QUESTION_MESSAGE));
				excluir(id);
				
			}
		});
        

        setVisible(true);
    }
    
    private void cadastrar() {
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
    }

    private void buscar() {
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
    }
    
    private void atualizar(int codigo) {
    	try {
    		Quarto q = new Quarto();
        	q.setCodigo(codigo);
        	q.setNumero(Integer.parseInt(txtNum.getText().trim()));
        	q.setTipo(txtTipo.getText().trim());
        	q.setValorDiaria(Double.parseDouble(txtValorDiaria.getText().trim()));
        	q.setCapacidadeMaxima(Integer.parseInt(txtCapacidade.getText().trim()));
        	q.setStatus(txtStatus.getText().trim());
			int res = new QuartoController().atualizar(q);
			if (res > 0) {
				JOptionPane.showMessageDialog(null, "Atualizado com sucesso", "Atualização", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Erro na atualização", "Atualização", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException | IOException e) {
			System.out.println(e.getMessage());
		}finally {
			limparCampos();
		}
    	
    }
    
    private void excluir(int codigo) {
    	try {
    		Quarto q = buscarCodigo(codigo);
    		if (q.getStatus().equals("Livre")) {
    			int opc = JOptionPane.showConfirmDialog(null, "Deseja mesmo excluir o quarto?", "Exclusão", JOptionPane.YES_NO_OPTION);
    			if (opc == 0) {
    				int res = new QuartoController().ecluir(codigo);
    				JOptionPane.showMessageDialog(null, "Quarto excluido", "Exclusão", JOptionPane.INFORMATION_MESSAGE);
    			}
    		}else {
    			JOptionPane.showMessageDialog(null, "Erro, quarto deve ter Status 'livre'", "Erro", JOptionPane.ERROR_MESSAGE);
    		}
			
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro na exclusão", "Erro", JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
    	
    }
    
    private Quarto buscarCodigo(int codigo) {
    	try {
			return new QuartoController().buscarCodigo(codigo);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaGerenciamentoQuarto frame = new TelaGerenciamentoQuarto();
					frame.setVisible(true); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}