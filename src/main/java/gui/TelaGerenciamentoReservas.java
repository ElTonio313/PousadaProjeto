package gui;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import dao.BancoDados;
import dao.ClienteDAO;
import dao.QuartoDAO;
import model.Cliente;
import model.Quarto;
import model.Reserva;
import controller.ReservaService;

public class TelaGerenciamentoReservas extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
	static {
		FORMATO_DATA.setLenient(false);
	}

	private JPanel contentPane;
	private JTextField txtCliente;
	private JTextField txtQuarto;
	private JTextField txtCheckin;
	private JTextField txtCheckout;
	private JTextField txtQntHosp;
	private DefaultTableModel modelTabela;
	private JTable tabela;

	private ReservaService reservaService;
	// Mantém a mesma ordem das linhas da tabela, para sabermos a qual
	// Reserva (com código no banco) corresponde a linha selecionada pelo usuário.
	private List<Reserva> reservasListadas;

	public TelaGerenciamentoReservas() {
		setTitle("reserva");
		this.reservaService = new ReservaService();
		this.reservasListadas = new ArrayList<>();
		this.iniciarComponentes();
		this.atualizarTabela();
	}

	private void iniciarComponentes() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(850, 300, 850, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Realizar Reserva");
		lblTitulo.setBounds(10, 10, 100, 25);
		contentPane.add(lblTitulo);

		JLabel lblClientte = new JLabel("Cliente (código)");
		lblClientte.setBounds(10, 42, 100, 25);
		contentPane.add(lblClientte);

		txtCliente = new JTextField();
		txtCliente.setBounds(110, 42, 150, 25);
		contentPane.add(txtCliente);
		txtCliente.setColumns(10);

		JLabel lblQuarto = new JLabel("Quarto (código)");
		lblQuarto.setBounds(10, 77, 100, 25);
		contentPane.add(lblQuarto);

		JLabel lblCheckin = new JLabel("Check-In");
		lblCheckin.setBounds(10, 112, 100, 25);
		contentPane.add(lblCheckin);

		JLabel lblCheckout = new JLabel("Check-Out");
		lblCheckout.setBounds(10, 147, 100, 25);
		contentPane.add(lblCheckout);

		JLabel lblQuantidaDeHspedes = new JLabel("Quant Hóspedes");
		lblQuantidaDeHspedes.setBounds(10, 182, 100, 25);
		contentPane.add(lblQuantidaDeHspedes);

		txtQuarto = new JTextField();
		txtQuarto.setColumns(10);
		txtQuarto.setBounds(110, 77, 150, 25);
		contentPane.add(txtQuarto);

		txtCheckin = new JTextField();
		txtCheckin.setColumns(10);
		txtCheckin.setBounds(110, 112, 150, 25);
		txtCheckin.setToolTipText("Formato: dd/MM/yyyy");
		contentPane.add(txtCheckin);

		txtCheckout = new JTextField();
		txtCheckout.setColumns(10);
		txtCheckout.setBounds(110, 147, 150, 25);
		txtCheckout.setToolTipText("Formato: dd/MM/yyyy");
		contentPane.add(txtCheckout);

		txtQntHosp = new JTextField();
		txtQntHosp.setColumns(10);
		txtQntHosp.setBounds(110, 182, 150, 25);
		contentPane.add(txtQntHosp);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBounds(10, 234, 150, 30);
		btnRegistrar.addActionListener(e -> registrarReserva());
		contentPane.add(btnRegistrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(192, 234, 150, 30);
		btnCancelar.addActionListener(e -> cancelarReservaSelecionada());
		contentPane.add(btnCancelar);

		String[] colunas = {"Cliente", "Quarto", "Check-in", "Check-Out", "Status"};
		modelTabela = new DefaultTableModel(colunas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// Impede que o usuário edite o resultado direto na tabela;
				// edições devem passar pelos métodos do Service.
				return false;
			}
		};
		tabela = new JTable(modelTabela);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(tabela);
		scroll.setBounds(400, 20, 410, 320);
		getContentPane().add(scroll);


		setLocationRelativeTo(null);
	}

	/**
	 * HU9 - Lê os campos do formulário, busca o Cliente e o Quarto pelo
	 * código informado, e delega a criação da reserva para o Service.
	 */
	private void registrarReserva() {
		try {
			int codigoCliente = Integer.parseInt(txtCliente.getText().trim());
			int codigoQuarto = Integer.parseInt(txtQuarto.getText().trim());
			Date dataCheckin = FORMATO_DATA.parse(txtCheckin.getText().trim());
			Date dataCheckout = FORMATO_DATA.parse(txtCheckout.getText().trim());
			int quantidadeHospedes = Integer.parseInt(txtQntHosp.getText().trim());

			Cliente cliente = buscarClientePorCodigo(codigoCliente);
			if (cliente == null) {
				JOptionPane.showMessageDialog(this, "Cliente não encontrado para o código informado.",
						"Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Quarto quarto = new QuartoDAO(BancoDados.getConexao()).buscarPorCod(codigoQuarto);
			if (quarto == null) {
				JOptionPane.showMessageDialog(this, "Quarto não encontrado para o código informado.",
						"Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			reservaService.registrarReserva(cliente, quarto, dataCheckin, dataCheckout, quantidadeHospedes);

			JOptionPane.showMessageDialog(this, "Reserva registrada com sucesso!");
			limparCampos();
			atualizarTabela();

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this,
					"Código do cliente, código do quarto e quantidade de hóspedes devem ser números.",
					"Erro", JOptionPane.ERROR_MESSAGE);
		} catch (ParseException ex) {
			JOptionPane.showMessageDialog(this, "Datas inválidas. Use o formato dd/MM/yyyy.",
					"Erro", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException | IllegalStateException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Não foi possível registrar",
					JOptionPane.WARNING_MESSAGE);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados.",
					"Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	/**
	 * HU10 - Cancela a reserva correspondente à linha selecionada na tabela,
	 * pedindo confirmação antes (critério 3).
	 */
	private void cancelarReservaSelecionada() {
		int linhaSelecionada = tabela.getSelectedRow();
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(this, "Selecione uma reserva na tabela para cancelar.",
					"Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Reserva reserva = reservasListadas.get(linhaSelecionada);

		if ("CANCELADA".equals(reserva.getStatus())) {
			JOptionPane.showMessageDialog(this, "Esta reserva já está cancelada.",
					"Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"Deseja realmente cancelar a reserva de " + reserva.getCliente().getNomeCompleto()
						+ " (Quarto " + reserva.getQuarto().getNumero() + ")?",
				"Confirmar cancelamento",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (confirmacao != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			reservaService.cancelarReserva(reserva.getCodigo());
			JOptionPane.showMessageDialog(this, "Reserva cancelada com sucesso.");
			atualizarTabela();
		} catch (IllegalArgumentException | IllegalStateException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Não foi possível cancelar",
					JOptionPane.WARNING_MESSAGE);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Erro ao cancelar a reserva.",
					"Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	/**
	 * HU11 - Recarrega a tabela com todas as reservas cadastradas.
	 * Chamado na abertura da tela e após qualquer registro/cancelamento.
	 */
	private void atualizarTabela() {
		try {
			reservasListadas = reservaService.listarReservas();

			modelTabela.setRowCount(0);

			for (Reserva r : reservasListadas) {
				modelTabela.addRow(new Object[]{
						r.getCliente().getNomeCompleto(),
						r.getQuarto().getNumero(),
						FORMATO_DATA.format(r.getDataCheckin()),
						FORMATO_DATA.format(r.getDataCheckout()),
						r.getStatus()
				});
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Erro ao carregar as reservas.",
					"Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private Cliente buscarClientePorCodigo(int codigo) throws SQLException {
		ClienteDAO clienteDAO = new ClienteDAO();
		List<Cliente> encontrados = clienteDAO.buscar(String.valueOf(codigo));
		for (Cliente c : encontrados) {
			if (c.getCodigo() == codigo) {
				return c;
			}
		}
		return null;
	}

	private void limparCampos() {
		txtCliente.setText("");
		txtQuarto.setText("");
		txtCheckin.setText("");
		txtCheckout.setText("");
		txtQntHosp.setText("");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaGerenciamentoReservas frame = new TelaGerenciamentoReservas();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}