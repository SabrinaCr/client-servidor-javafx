/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import threads.SocketThreadServidor;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import servidor.TelaPrincipalController;
import servidor.TelaPrincipalController;

/**
 *
 * @author aluno
 */
public class SocketThreadPrincipalServidor implements Runnable
{   
    TelaPrincipalController jf;
    ServerSocket server;
    Socket sock;
    String mens;
    String ip;
    String porta;
    String nome;
    StringBuilder flag;
    ArrayList<Cliente> listaClientes = new ArrayList();
    ObjectOutputStream output;
    ObjectInputStream input;
    int a = 0;
    
    public SocketThreadPrincipalServidor(TelaPrincipalController jf, ServerSocket server)
    {   
        this.server = server;
        this.jf = jf;    
    }
    
    public void run() 
    {  
        a = 1;
        flag = new StringBuilder("true");
        while(jf.isAtivo()) //decide se o processo deve continuar
        { 
            try
            {  
                sock = server.accept();

                output = new ObjectOutputStream(sock.getOutputStream());
                input = new ObjectInputStream(sock.getInputStream());
                
                // 
                String protocolo = input.readObject().toString();
                String v[] = protocolo.split("#");
                ip = v[1];
                nome = v[2];
                porta = v[3];
                
                // protocolo clientes conectados
                String protocoloAccept = "<#@NEWUSER";
                for (Cliente s : listaClientes) {
                    protocoloAccept += "#IPCLIENT=" + s.getSocket().getInetAddress()
                                    + "#NOME=" + s.getNome()
                                    + "#PORTA=" + s.getPorta();
                }
                protocoloAccept +=">";
                
                for (Cliente s : listaClientes) {
                    output = new ObjectOutputStream(s.getSocket().getOutputStream());
                    output.writeObject(protocoloAccept);
                }
                
                // protocolo novo usuário
                protocolo = protocolo.replace("CONNECT", "ACCEPT");
                output = new ObjectOutputStream(sock.getOutputStream());
                output.writeObject(protocolo);
                
                Cliente c = new Cliente(ip, nome, porta);
                c.setSocket(new Socket("127.0.0.1", 8081));
                
                // adicona novo cliente na lista 
                Platform.runLater(()->{jf.getTabelaConectados().getItems().add(c);});
                listaClientes.add(c);
                
                // lança uma trhread para tratar exclusivamente esse cliente
                SocketThreadServidor th = new SocketThreadServidor(jf, c, listaClientes, output, input, flag);
                Thread newThrd = new Thread(th);
                newThrd.start(); // inicia a thread

            }catch(Exception e){ 
                System.out.println(e.getMessage());   
            }
        }
        System.out.println("Interrompeu");        
    }   

    public int getA() {
        return a;
    }

    public ArrayList<Cliente> getListaSock() {
        return listaClientes;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }
    
}
