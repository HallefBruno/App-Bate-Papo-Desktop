
package br.com.redes;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private final int porta = 12345;
    private List<PrintStream> escritores = new ArrayList<>();
    
    public Servidor() {
        try {
            ServerSocket server = new ServerSocket(porta);

            while(true){
                Socket s = server.accept();
                new Thread(new EscutaCliente(s)).start();
                PrintStream  ps = new PrintStream(s.getOutputStream());
                escritores.add(ps);
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void encaminharParatodos(String texto){
        for (PrintStream escritore : escritores) {
            escritore.println(texto);
            escritore.flush();
        }
    }
    
    private class EscutaCliente implements Runnable{

        Scanner leitor;
        
        public EscutaCliente(Socket socket) {
            try {
                leitor = new Scanner(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        @Override
        public void run() {
            String texto ;
            while((texto = leitor.nextLine())!= null ){
                System.out.println("Recebeu "+texto);
                encaminharParatodos(texto);
            }
        }
    }

    public static void main(String[] args) {
        new Servidor();
    }
}
