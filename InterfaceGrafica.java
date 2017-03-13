package br.com.redes;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class InterfaceGrafica extends JFrame {

    public  JTextArea textArea;
    private JButton btnEnviar;
    private JTextField textFieldCampo;
    private JScrollPane scrollPane;
    private ImageIcon imageIcon;
    private JLabel lbConfiguracao;
    private Socket socket;
    private PrintStream escritor;
    private Scanner leitor;
    private final int porta = 12345;
    private String nome = "Sem nome";
    private enum Oline {ONLINE}
    
    public InterfaceGrafica() throws HeadlessException {
        nome = JOptionPane.showInputDialog(null,"Por favor, digite seu nome.");
        inicializarComponentes();
        definirEventos();
    }

    private void inicializarComponentes() {
        
        setLayout(null);
        setTitle(nome+" Chat "+Oline.ONLINE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(418, 310);
        setResizable(false);
        setLocationRelativeTo(null);
        configurarRede();
        Font fonte = new Font("serif", Font.BOLD, 20);
        imageIcon = new ImageIcon("C:\\Users\\Evandro\\Documents\\NetBeansProjects\\AppConectaServidor\\src\\br\\com\\redes\\cadastro_cidades.gif");
        textArea = new JTextArea();
        textArea.setEditable(false);
        textFieldCampo = new JTextField();
        btnEnviar = new JButton("Enviar", imageIcon);
        btnEnviar.setMnemonic('E');
        btnEnviar.setToolTipText("Enviar mensagem");
        scrollPane = new JScrollPane(textArea);
        lbConfiguracao = new JLabel("Configurações.");
        textFieldCampo.setToolTipText("Digite sua mensagem e tecle enter para enviar.");
        textArea.setFont(fonte);
        textFieldCampo.setFont(fonte);
        scrollPane.setBounds(0, 0, 410, 200);
        btnEnviar.setBounds(310, 205, 100, 58);
        textFieldCampo.setBounds(0, 205, 300, 60);
        lbConfiguracao.setBounds(0, 234, 90, 80);
        btnEnviar.addActionListener(new Enviar());
        add(btnEnviar);
        add(textFieldCampo);
        add(scrollPane);
        add(lbConfiguracao);
    }

    private void definirEventos() {
        lbConfiguracao.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ConfigurarCores cores = new ConfigurarCores();
                cores.definirEventos();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lbConfiguracao.setText("<html><u>Configurações.</html></u>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lbConfiguracao.setText("Configurações.");
            }
        });
        
        textFieldCampo.addActionListener((e)->{
            escritor.println(textFieldCampo.getText());
            escritor.flush();
            textFieldCampo.setText("");
            textFieldCampo.requestFocus();
        });
    }
    
    private void configurarRede() {
        try {
            socket = new Socket("127.0.0.1",porta);
            escritor = new PrintStream(socket.getOutputStream());
            leitor = new Scanner(socket.getInputStream());
            new Thread(new EscutaServidor()).start();
        } catch (IOException ex) {
            Logger.getLogger(InterfaceGrafica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class EscutaServidor implements Runnable {
        @Override
        public void run() {
            String texto;
            while((texto = leitor.nextLine()) !=null){
                textArea.append(texto+"\n");
            }
        }
    }
    
    private class Enviar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            escritor.println(textFieldCampo.getText());
            escritor.flush();
            textFieldCampo.setText("");
            textFieldCampo.requestFocus();
        }
        
    }
    
    public static void main(String[] args) {
        
        InterfaceGrafica interfaceGrafica = new InterfaceGrafica();
        interfaceGrafica.setVisible(true);
    }

    public class ConfigurarCores extends JFrame {

        private JComboBox comboBox;

        public ConfigurarCores() {
            inicializarComponentes();
        }

        private void inicializarComponentes() {
            setTitle("Configurações de cores");
            setResizable(false);
            setSize(300, 50);
            setLocationRelativeTo(null);
            String[] nomeCores = {"BLUE", "CYAN", "GREEN", "RED", "PINK"};
            comboBox = new JComboBox(nomeCores);
            comboBox.setBounds(0, 0, 30, 30);
            add(comboBox);
            setVisible(true);

        }

        private void definirEventos() {

            comboBox.addActionListener((e) -> {

                switch (comboBox.getSelectedIndex()) {
                    case 0:
                        textFieldCampo.setForeground(Color.BLUE);break;
                    case 1: 
                        textFieldCampo.setForeground(Color.CYAN);break;
                    case 2:
                        textFieldCampo.setForeground(Color.GREEN);break;                        
                    case 3:
                        textFieldCampo.setForeground(Color.RED);break;
                    case 4:
                        textFieldCampo.setForeground(Color.PINK);break;
                
                }

            });
        }
    }
}
