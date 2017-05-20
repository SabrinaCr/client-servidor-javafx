/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author aluno
 */
public class SocketThread implements Runnable
{   TelaPrincipalController tela;
    Socket conexao;
    String mens,ip;
    ObjectOutputStream output;
    ObjectInputStream input;
    //DefaultTableModel modTabMens,modTabCon;
    
    SocketThread(TelaPrincipalController tela, Socket conexao,ObjectOutputStream output, ObjectInputStream input)
    {   
        this.conexao=conexao;
        this.tela=tela;
        this.input=input;
        this.output=output;
        //modTabMens=(DefaultTableModel)jf.getTabMensagens().getModel();
        //modTabCon=(DefaultTableModel)jf.getTabConectados().getModel();
        ip=conexao.getInetAddress().getHostAddress();
    }
    public void run() 
    {   
        try
       {
            do
            {  
                String v[] = ((String)input.readObject()).split(",");
                
                String ip = v[0];
                String porta = v[1];
                String nome = v[3];
                String mens = v[4];
//                mens = ( String ) input.readObject();  // ler mensagem
                tela.getTabMensagens().getItems().add(new Mensagem(ip, mens));
                
                try{Thread.sleep(1000);}catch(Exception e){}
                /// opcionalmente pode devolver uma mensagem ao cliente
                //output.writeObject("Servidor XYZ recebeu a informacao"); 
            } while ( !mens.equals( "FIM" ));
            // cliente desconectou, começa processo de retirar o ip do cliente da tabela
            int i=0;
            while(!tela.getListaConectados().getItems().get(i).toString().equals(ip) && i <tela.getListaConectados().getItems().size())
                i++;
            tela.getListaConectados().getItems().remove(i);
            
// fecha as conexões
            output.close(); input.close(); 
            conexao.close();
        }catch(Exception e){
                System.out.println(e.getMessage());
        }
    }   
    
}
