import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.GroupLayout.Alignment;

public class GUIRegistrarProd extends JFrame {

    private final Font font;
    private final BaseDeDatos bd;
    private JSeparator separator_2, separator_1;
    private Container contenedor;
    private JLabel  precio, descripcion,nombre;
    private JTextField  precioIn, descripcionIn,nombreIn;
    private JButton cancelar, crear;


    public GUIRegistrarProd(BaseDeDatos bdIn) {
        super("CREAR PRODUCTO"); 
//        this.getContentPane().setSize(400,400);

        font = new Font("Tahoma", Font.PLAIN, 14); 
        bd = bdIn;
        InitGUI();
    }

    private void InitGUI() {
        contenedor = getContentPane();
        contenedor.removeAll();

        JPanel panelUsuario = new JPanel();
        panelUsuario.setBounds(0, 0, 394, 379);
        panelUsuario.setLayout(null);

        nombre = new JLabel("Nombre: ");
        nombre.setFont(font);
        nombre.setBounds(21, 140, 105, 32);
        panelUsuario.add(nombre);

        nombreIn = new JTextField();
        nombreIn.setFont(font);
        nombreIn.setBounds(136, 141, 234, 32);
        panelUsuario.add(nombreIn);

        precio = new JLabel("Precio:");
        precio.setFont(font);
        precio.setBounds(21, 183, 105, 32);
        panelUsuario.add(precio);

        precioIn = new JTextField();
        precioIn.setFont(font);
        precioIn.setBounds(136, 184, 234, 32);
        panelUsuario.add(precioIn);

        descripcion = new JLabel("Descripcion:");
        descripcion.setFont(font);
        descripcion.setBounds(21, 226, 105, 32);
        panelUsuario.add(descripcion);

        descripcionIn = new JTextField();
        descripcionIn.setFont(font);
        descripcionIn.setBounds(136, 227, 234, 32);
        panelUsuario.add(descripcionIn);

        ActionListener listener = new ManejadorDeBotones();

        cancelar = new JButton("Cancelar");
        cancelar.setFont(font);
        cancelar.setBounds(92, 322, 120, 28);
        cancelar.addActionListener(listener);
        panelUsuario.add(cancelar);

        crear = new JButton("Crear Producto");
        crear.setFont(font);
        crear.setBounds(222, 322, 148, 28);
        crear.addActionListener(listener);
        panelUsuario.add(crear);

        separator_2 = new JSeparator();
        separator_2.setBounds(21, 309, 349, 2);
        panelUsuario.add(separator_2);

        separator_1 = new JSeparator();
        separator_1.setBounds(21, 88, 349, 2);
        panelUsuario.add(separator_1);

        JLabel lblNewLabel = new JLabel("");
        URL filePath = this.getClass().getResource("/images/create.png");
        getContentPane().setLayout(null);
        lblNewLabel.setIcon(new ImageIcon(filePath));
        lblNewLabel.setBounds(21, 11, 66, 66);
        panelUsuario.add(lblNewLabel);
        getContentPane().add(panelUsuario);

        setResizable(false);
        setSize(400, 406);
        setVisible(true);
        setLocationRelativeTo(null);
    }


    private class ManejadorDeBotones implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (actionEvent.getSource() == crear){
                boolean var;
                String descrip = null;
                if (descripcionIn.getText().compareTo("")!=0)descrip=descripcionIn.getText();

                var= bd.registrarProducto(nombreIn.getText(),precioIn.getText(),descrip);
                if (var) {
                    JOptionPane.showMessageDialog(null, "Producto creado exitosamente");
                    dispose();
                } else JOptionPane.showMessageDialog(null, "Error al crear producto.");
            }

            if (actionEvent.getSource() == cancelar)
                dispose();
        }
    }
}
