/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aluno
 */
public class SocketThreadPrincipal implements Runnable
{   TelaPrincipalController jf;
    ServerSocket server;
    Socket sock;
    String mens,ip;
    ArrayList<Socket> listaSock = new ArrayList();
    ObjectOutputStream output;
    ObjectInputStream input;
    int a=0;
    SocketThreadPrincipal(TelaPrincipalController jf, ServerSocket server)
    {   this.server=server;
        this.jf=jf;    }
    public void run() 
    {  
        a=1;
        while(jf.isAtivo()) //decide se o processo deve continuar
        { 
            try
            {  
                sock = server.accept();
         
                // mostra o ip de um novo cliente na tabela de conectados
                String ip = sock.getInetAddress().getHostAddress();
                listaSock.add(sock);

                output = new ObjectOutputStream(sock.getOutputStream());
                input = new ObjectInputStream(sock.getInputStream());


                /*Platform.runLater(new Runnable() {
                @Override
                public void run() {
                     jf.getListaConectados().getItems().add(ip);
                  }
                });*/
                Platform.runLater(()->{jf.getListaConectados().getItems().add(ip);});

                // lança uma trhread para tratar exclusivamente esse cliente
                SocketThread th=new SocketThread(jf,sock,output,input);
                Thread newThrd = new Thread(th);
                newThrd.start(); // inicia a thread
              }catch(Exception e){ System.out.println(e.getMessage());   }
        }
        System.out.println("interrompeu");        
    }   

    public int getA() {
        return a;
    }

    public ArrayList<Socket> getListaSock() {
        return listaSock;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }
    
}
