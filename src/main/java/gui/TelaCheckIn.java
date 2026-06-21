package gui;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JButton;

import model.Hospedagem;
import model.Reserva;
import controller.HospedagemService;

public class TelaCheckIn extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat FORMATO_DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private JPanel contentPane;
	private DefaultTableModel modelReservas;
	private JTable tabelaReservas;
	private DefaultTableModel modelHospedagens;
	private JTable tabelaHospedagens;

	private HospedagemService hospedagemService;
	// Mesma ideia da tela de reservas: mantém a mesma ordem das linhas
	// da tabela de reservas elegíveis, para sabermos qual Reserva
	// corresponde à linha selecionada pelo usuário.
	private List<Reserva> reservasElegiveis;

	public TelaCheckIn() {
		setTitle("Check-in");
		this.hospedagemService = new HospedagemService();
		this.reservasElegiveis = new ArrayList<>();
		this.iniciarComponentes();
		this.atualizarTabelas();
	}

	private void iniciarComponentes() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800, 250, 900, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Realizar Check-in");
		lblTitulo.setBounds(10, 10, 200, 25);
		contentPane.add(lblTitulo);

		JLabel lblReservas = new JLabel("Reservas disponíveis para check-in:");
		lblReservas.setBounds(10, 40, 300, 20);
		contentPane.add(lblReservas);

		String[] colunasReservas = {"Reserva", "Cliente", "Quarto", "Check-in Previsto", "Check-out Previsto"};
		modelReservas = new DefaultTableModel(colunasReservas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabelaReservas = new JTable(modelReservas);
		tabelaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollReservas = new JScrollPane(tabelaReservas);
		scrollReservas.setBounds(10, 65, 860, 180);
		contentPane.add(scrollReservas);

		JButton btnCheckin = new JButton("Realizar Check-in");
		btnCheckin.setBounds(10, 255, 200, 30);
		btnCheckin.addActionListener(e -> realizarCheckin());
		contentPane.add(btnCheckin);

		JLabel lblHospedagens = new JLabel("Hospedagens realizadas:");
		lblHospedagens.setBounds(10, 300, 300, 20);
		contentPane.add(lblHospedagens);

		String[] colunasHospedagens = {"Cliente", "Quarto", "Data/Hora Check-in", "Status"};
		modelHospedagens = new DefaultTableModel(colunasHospedagens, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tabelaHospedagens = new JTable(modelHospedagens);
		JScrollPane scrollHospedagens = new JScrollPane(tabelaHospedagens);
		scrollHospedagens.setBounds(10, 325, 860, 150);
		contentPane.add(scrollHospedagens);

		setLocationRelativeTo(null);
	}

	/**
	 * HU12 - Realiza o check-in da reserva selecionada na tabela de
	 * reservas disponíveis: cria a hospedagem com data/hora atual
	 * e marca o quarto como OCUPADO.
	 */
	private void realizarCheckin() {
		int linhaSelecionada = tabelaReservas.getSelectedRow();
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(this, "Selecione uma reserva para realizar o check-in.",
					"Atenção", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Reserva reserva = reservasElegiveis.get(linhaSelecionada);

		try {
			Hospedagem hospedagem = hospedagemService.realizarCheckin(reserva.getCodigo());

			JOptionPane.showMessageDialog(this,
					"Check-in realizado com sucesso em " + FORMATO_DATA_HORA.format(hospedagem.getDataHoraCheckin())
							+ ".\nQuarto " + reserva.getQuarto().getNumero() + " agora está OCUPADO.");

			atualizarTabelas();

		} catch (IllegalArgumentException | IllegalStateException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Não foi possível realizar o check-in",
					JOptionPane.WARNING_MESSAGE);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados.",
					"Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	/**
	 * Recarrega as duas tabelas: reservas ainda elegíveis para check-in,
	 * e o histórico de hospedagens já realizadas.
	 */
	private void atualizarTabelas() {
		try {
			reservasElegiveis = hospedagemService.listarReservasElegiveisParaCheckin();
			modelReservas.setRowCount(0);
			for (Reserva r : reservasElegiveis) {
				modelReservas.addRow(new Object[]{
						r.getCodigo(),
						r.getCliente().getNomeCompleto(),
						r.getQuarto().getNumero(),
						FORMATO_DATA.format(r.getDataCheckin()),
						FORMATO_DATA.format(r.getDataCheckout())
				});
			}

			List<Hospedagem> hospedagens = hospedagemService.listarHospedagens();
			modelHospedagens.setRowCount(0);
			for (Hospedagem h : hospedagens) {
				modelHospedagens.addRow(new Object[]{
						h.getReserva().getCliente().getNomeCompleto(),
						h.getReserva().getQuarto().getNumero(),
						FORMATO_DATA_HORA.format(h.getDataHoraCheckin()),
						h.getStatus()
				});
			}

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Erro ao carregar os dados.",
					"Erro", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaCheckIn frame = new TelaCheckIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}