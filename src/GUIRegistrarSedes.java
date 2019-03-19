import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase permite mostrar una interfaz sencilla para el registro
 * de nuevas sedes en la base de datos de la empresa.
 * 
 *  Los campos requeridos son: 'Id', 'Direccion', 'Telefono' e 'Id del Encargado''.
 *  
 *  El resultado del registro satisfactorio o las fallas se notifican
 *  por medio cuadros de dialogo.
 */


@SuppressWarnings("serial")
public class GUIRegistrarSedes extends JFrame{

	private Container contenedor;
	private JLabel id, direccion, telefono, encargado, nombre;
	private JTextField idIn, direccionIn, telefonoIn, nombreIn;
	private JComboBox encargadoIn;
	private JButton registrar, cancelar;
	private JSeparator separator_1, separator_2;
	private Font font;
	private BaseDeDatos bd;
	private ActionListener listener;

	public GUIRegistrarSedes(BaseDeDatos bdIn){
	    super("REGISTRAR SEDE");

		font = new Font("Tahoma", Font.PLAIN, 14);
        bd = bdIn;
		crearComponentes();
	}


	//Funcion que crea la interfaz y sus componentes
	private void crearComponentes() {

		contenedor = getContentPane();
		contenedor.removeAll();
		getContentPane().setLayout(null);

		JPanel panelUsuario = new JPanel();
		panelUsuario.setBounds(0, 0, 394, 475);
		contenedor.add(panelUsuario);
		panelUsuario.setLayout(null);

		nombre = new JLabel("Nombre:");
		nombre.setFont(font);
		nombre.setBounds(21, 143, 105, 32);
		panelUsuario.add(nombre);

		nombreIn = new JTextField();
		nombreIn.setFont(font);
		nombreIn.setBounds(136, 144, 234, 32);
		panelUsuario.add(nombreIn);

		direccion = new JLabel("Direccion:");
		direccion.setFont(font);
		direccion.setBounds(21, 186, 105, 32);
		panelUsuario.add(direccion);

		direccionIn = new JTextField();
		direccionIn.setFont(font);
		direccionIn.setBounds(136, 186, 234, 32);
		panelUsuario.add(direccionIn);

		telefono =  new JLabel("Telefono:");
		telefono.setFont(font);
		telefono.setBounds(21, 229, 105, 32);
		panelUsuario.add(telefono);

		telefonoIn = new JTextField();
		telefonoIn.setFont(font);
		telefonoIn.setBounds(136, 230, 234, 32);
		panelUsuario.add(telefonoIn);

		encargado = new JLabel("Encargado:");
		encargado.setFont(font);
		encargado.setBounds(21, 272, 105, 32);
		panelUsuario.add(encargado);

		encargadoIn = new JComboBox<>(bd.cambiarDimension(
				bd.consultarUsuarios("Activo",null,"cedula,nombres")));
		encargadoIn.setSelectedItem(null);
		encargadoIn.setFont(font);
		encargadoIn.setBounds(136, 274, 234, 32);
		panelUsuario.add(encargadoIn);

		listener = new ManejadorDeBotones();

		cancelar = new JButton("Cancelar");
		cancelar.setFont(font);
		cancelar.setBounds(120, 351, 113, 28);
		cancelar.addActionListener(listener);
		panelUsuario.add(cancelar);

		registrar = new JButton("Registrar Sede");
		registrar.setFont(font);
		registrar.setBounds(243, 351, 127, 28);
		registrar.addActionListener(listener);
		panelUsuario.add(registrar);

		separator_2 = new JSeparator();
		separator_2.setBounds(21, 338, 349, 2);
		panelUsuario.add(separator_2);

		separator_1 = new JSeparator();
		separator_1.setBounds(21, 88, 349, 2);
		panelUsuario.add(separator_1);

		JLabel lblNewLabel = new JLabel("");
		URL filePath = this.getClass().getResource("/images/sede.png");
		lblNewLabel.setIcon(new ImageIcon(filePath));
		lblNewLabel.setBounds(21, 11, 66, 66);
		panelUsuario.add(lblNewLabel);
		setResizable(false);
		setSize(400,426);
		setVisible(true);
		setLocationRelativeTo(null);
	}


	//Funcion que valida si algun campo a registrar esta vacio
	private boolean validar1(){
		boolean val=true;
		val = (idIn.getText().compareTo("")==0 ||
				direccionIn.getText().compareTo("")==0 ||
				telefonoIn.getText().compareTo("")==0 ||
				nombreIn.getText().compareTo("")==0) ? false : true;

		return val;
	}


	//Funcion para validar el dominio de los datos ingresados
	private String validar2(){
		String mensaje = "";

		if(!validarDatoEntero(idIn))
			mensaje = mensaje + " El id de la sede debe ser un numero entero \n";

		Pattern patron = Pattern.compile("[^A-Za-z0-9 #-]");
		Matcher direccion = patron.matcher(direccionIn.getText());
		if(direccion.find()|| direccionIn.getText().length()>40) 
			mensaje = mensaje + " Digite una direccion valida \n";

		Matcher nombre = patron.matcher(nombreIn.getText());
        if(nombre.find()|| nombreIn.getText().length()>40)
            mensaje = mensaje + " Digite un nombre valido \n";

		patron = Pattern.compile("[^0-9]");
		Matcher tel = patron.matcher(telefonoIn.getText());
		if(tel.find()|| telefonoIn.getText().length()>40|| telefonoIn.getText().length()<7) 
			mensaje = mensaje + " Digite un numero de telefono valido \n";

		if(mensaje.compareTo("")==0)
			mensaje="true";

		return mensaje;
	}




	//Funcion que valida s� un dato ingresado a traves de un JTextField es entero
	private boolean validarDatoEntero(JTextField dato) {
		boolean val = true;
		try {
			Integer.parseInt(dato.getText());

		} catch (NumberFormatException excepcion) {
			val = false;
		}
		return val;
	}


	//Manejador de eventos para los botones del apartado Registrar-Sede
	//Si se presiona <cancelar>, la interfaz del menu se cierra
	//Si se presiona <registrar>, se valida que:
	//		no hayan campos vacios, los datos esten dentro del dominio y 
	//		las id ingresadas esten disponibles 
	private class ManejadorDeBotones implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if(actionEvent.getSource() == cancelar){
				dispose();
			}
			else if(actionEvent.getSource() == registrar){
				if(validar1()) {
					if(validar2().compareTo("true")==0) {
						String encargado=null;
						if(encargadoIn.getSelectedItem()!=null){
							if (!(encargadoIn.getSelectedItem().toString().compareTo("")==0)){
								encargado=encargadoIn.getSelectedItem().toString();
								encargado=encargado.substring(0,encargado.indexOf("-"));
							}
						}

						boolean var = bd.registraSede( idIn.getText(), direccionIn.getText(),
								telefonoIn.getText(),encargado,nombreIn.getText());
						if (var) JOptionPane.showMessageDialog(null, "Sede registrada exitosamente");
						else JOptionPane.showMessageDialog(null, "Error al actualizar usuario.");
						dispose();
					}
					else JOptionPane.showMessageDialog(null, validar2());
				}
				else JOptionPane.showMessageDialog(null, "Digite todos los campos");
			}

		}
	}
}



