import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

/**
 * Esta clase permite mostrar una interfaz sencilla para la consulta de
 * empleados en la base de datos de la empresa.
 * 
 * Los criterios de la consulta son: 'Id', 'Nombres' o 'Apellidos'.
 * 
 * El resultado de la consulta se muestra en una tabla.
 */

@SuppressWarnings("serial")
public class GUIConsultarUser extends JFrame {

	// Variables y componentes de la GUI
	private BaseDeDatos bd;
	private JLabel titulo, fondoAzul, fondoGris, icono, criterio;
	private JComboBox busqueda;
	private JButton salir, consultar;
	private JSeparator separador;
	private Font font;
	private ActionListener listener;
	private int pX, pY;
	private JLabel fondoNegro;

	// Creacion y despliegue de la interfaz de consulta
	public GUIConsultarUser(BaseDeDatos bdIn) {

		String[] users = bdIn.cambiarDimension(bdIn.consultarUsuarios(null, null, "cedula,nombres"));

		if (users.length != 0) {

			// Inicializacion de base de datos y fuente
			bd = bdIn;
			font = new Font("Tahoma", Font.PLAIN, 14);

			// Configuraciones de la ventana principal
			getContentPane().setForeground(Color.BLACK);
			getContentPane().setLayout(null);
			this.setUndecorated(true);
			listener = new ManejadorDeBotones();
			manejadorDesplazamientoVentana(this);

			// Boton para iniciar el proceso de consulta
			consultar = new JButton("Consultar");
			consultar.setOpaque(true);
			consultar.setBackground(new Color(227, 227, 227));
			consultar.setFont(font);
			consultar.setBounds(433, 112, 101, 32);
			consultar.addActionListener(listener);
			getContentPane().add(consultar);

			busqueda = new JComboBox<>(users);
			busqueda.setFont(font);
			busqueda.setBounds(104, 113, 323, 32);
			getContentPane().add(busqueda);

			criterio = new JLabel("Usuario");
			criterio.setFont(font);
			criterio.setBounds(37, 112, 69, 32);
			getContentPane().add(criterio);

			separador = new JSeparator();
			separador.setBounds(21, 181, 513, 4);
			getContentPane().add(separador);

			salir = new JButton("Salir");
			salir.setOpaque(true);
			salir.setBackground(new Color(227, 227, 227));
			salir.setFont(font);
			salir.setBounds(445, 192, 89, 32);
			salir.addActionListener(listener);
			getContentPane().add(salir);

			// Icono a la izquierda del titulo
			icono = new JLabel("");
			icono.setIcon(new ImageIcon(GUIConsultarUser.class.getResource("/images/buscar.png")));
			icono.setBounds(11, 1, 48, 90);
			getContentPane().add(icono);

			// Etiqueta del titulo de la ventana
			titulo = new JLabel("BUSCAR USUARIO");
			titulo.setFont(new Font("Tahoma", Font.BOLD, 16));
			titulo.setForeground(Color.WHITE);
			titulo.setBounds(69, 28, 175, 32);
			getContentPane().add(titulo);

			// -- Fondos azul y gris -- //
			fondoAzul = new JLabel("");
			fondoAzul.setBounds(1, 1, 559, 90);
			fondoAzul.setOpaque(true);
			fondoAzul.setBackground(new Color(45, 118, 232));
			getContentPane().add(fondoAzul);
			fondoGris = new JLabel("");
			fondoGris.setBounds(1, 89, 559, 174);
			fondoGris.setOpaque(true);
			fondoGris.setBackground(new Color(227, 227, 227));// Gris
			getContentPane().add(fondoGris);

			// Fondo negro para las margenes
			fondoNegro = new JLabel("");
			fondoNegro.setBackground(Color.BLACK);
			fondoNegro.setOpaque(true);
			fondoNegro.setBounds(0, 0, 561, 264);
			getContentPane().add(fondoNegro);

            setResizable(false);
			setSize(561, 264);
			setVisible(true);
			setLocationRelativeTo(null);
		} else {
			JOptionPane.showMessageDialog(null, "Actualmente no hay usuarios creados");
		}
	}

	// Manejador de eventos sobre los botones del menu de consulta
	private class ManejadorDeBotones implements ActionListener {

		public void actionPerformed(ActionEvent actionEvent) {
			String str = busqueda.getSelectedItem().toString();
			str = str.substring(0, str.indexOf("-"));

			if (actionEvent.getSource() == consultar) {
				String[][] resultado = bd.consultarUsuarios("Id", str,
						"cedula, nombres, apellidos, direccion, numero, email, tipo_usuario,  sede, activo");
				resultadosConsultaGUI(resultado);
			} else if (actionEvent.getSource() == salir) {

                dispose();
            }
		}
	}

	// Despliegue de la interfaz con el resultado de la consulta
	private void resultadosConsultaGUI(String[][] board) {

		if (board == null || board.length == 0)
			JOptionPane.showMessageDialog(null, "La busqueda no produjo resultados.");

		else {
			JFrame frame = new JFrame();
			String column[] = { "id", "Nombres", "Apellidos", "Direccion", "Telefono", "Correo electronico",
					"Tipo de usuario", "Sede", "Activo" };
			JTable table = new JTable(board, column);
			table.setRowMargin(0);
			table.setFont(font);
			JScrollPane sp = new JScrollPane(table);
			frame.getContentPane().add(sp);
			frame.setSize(1200, 400);
			frame.setVisible(true);
		}
	}

	// Manejador del desplazamiento de la ventana causado por el arrastre del
	// mouse
	private void manejadorDesplazamientoVentana(JFrame frame) {
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				pX = me.getX();
				pY = me.getY();
			}
		});
		frame.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
			}
		});
	}
}
