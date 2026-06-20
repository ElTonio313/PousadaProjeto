package gui;

import javax.swing.*;
import model.Cliente;
import controller.ClienteController;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelaCadastroCliente extends JFrame {
    
    public TelaCadastroCliente() {
        setTitle("Cadastro de Cliente");
        setSize(300, 350);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 20, 80, 25);
        add(lblNome);

        JTextField txtNome = new JTextField();
        txtNome.setBounds(100, 20, 150, 25);
        add(txtNome);

        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setBounds(10, 55, 80, 25);
        add(lblCpf);

        JTextField txtCpf = new JTextField();
        txtCpf.setBounds(100, 55, 150, 25);
        add(txtCpf);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(100, 150, 100, 30);
        add(btnSalvar);

        // Ação do botão
        btnSalvar.addActionListener(e -> {
            try {
                Cliente c = new Cliente();
                c.setNomeCompleto(txtNome.getText());
                c.setCpf(txtCpf.getText());
                
                ClienteController ctrl = new ClienteController();
                String msg = ctrl.salvarCliente(c);
                JOptionPane.showMessageDialog(null, msg);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro nos dados!");
            }
        });

        setVisible(true);
    }
    public static void main(String[] args) {
        new TelaCadastroCliente();
    }
}
