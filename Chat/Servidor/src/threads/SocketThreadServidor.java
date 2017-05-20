/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import model.Cliente;
import model.Mensagem;
import servidor.TelaPrincipalController;
import servidor.TelaPrincipalController;


/**
 *
 * @author aluno
 */
public class SocketThreadServidor implements Runnable
{   
    TelaPrincipalController tela;
    Socket sockClienteRemove;
    ServerSocket serverListner;
    ArrayList<Cliente> clientes;
    String mensagem, ip, porta, nome, action;
    StringBuilder flag;
    ObjectOutputStream output;
    ObjectInputStream input;
    //DefaultTableModel modTabMens,modTabCon;
    
    SocketThreadServidor(TelaPrincipalController tela, Cliente clienteRemove, ArrayList<Cliente> clientes, ObjectOutputStream output, ObjectInputStream input, StringBuilder flag)
    {   
        this.flag = flag;
        this.sockClienteRemove = clienteRemove.getSocket();
        this.tela = tela;
        this.input = input;
        this.output = output;
        this.clientes = clientes;
        //modTabMens=(DefaultTableModel)jf.getTabMensagens().getModel();
        //modTabCon=(DefaultTableModel)jf.getTabConectados().getModel();
        ip = sockClienteRemove.getInetAddress().getHostAddress();
        porta = sockClienteRemove.getLocalPort() + "";
    }
    
    public void run() 
    {   
        try
        {
            do
            {  
                String v[] = ((String)input.readObject()).split("#");
                
                action = v[0];
                ip = v[1];
                nome = v[2];
                porta = v[3];
                
                String protocoloLogout = "<@LOGOUT#IPCLIENT=" + sockClienteRemove.getInetAddress() + ">";
                for(Cliente c: clientes){
                    output = new ObjectOutputStream(c.getSocket().getOutputStream());
                    output.writeObject(protocoloLogout);
                }

                // remove cliente 
                tela.getTabelaConectados().getItems().remove(sockClienteRemove);
                flag.setLength(0);
                flag.append("false");

                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println("Erro: " + e);
                }
                
            } while (flag.equals("true"));
            
            // cliente desconectou, começa processo de retirar o ip do cliente da tabela
            int i = 0;
            ObservableList<Cliente> clientes = tela.getTabelaConectados().getItems();
            while(!clientes.get(i).getIp().equals(ip) && i < clientes.size())
                i++;
            clientes.remove(i);
            tela.getTabelaConectados().getItems().remove(i);
            
            // fecha as conexões
            output.close(); input.close(); 
            sockClienteRemove.close();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }   
    
}
