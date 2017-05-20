/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import conexao.DadosConexao;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import javafx.scene.control.Label;

/**
 *
 * @author ERI
 */
public class SocketThread  implements Runnable{
    private Socket Client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Label status;
    private DadosConexao dc;
    
    SocketThread(Socket conexao,ObjectOutputStream output, ObjectInputStream input,Label status, DadosConexao dc)
    {   this.Client=conexao;
        this.input=input;
        this.output=output;
        this.status=status;
        this.dc = dc;
    }
     public void run() 
    {   
        try
       {
            do
            {
                String processos="";
                try
                {   
                    Process p = Runtime.getRuntime().exec("tasklist");
                    InputStream input = p.getInputStream();
                    BufferedInputStream reader = new BufferedInputStream(input);
                    Scanner sc = new Scanner(input);
                    processos="";
                    
                    processos += "IP: " + Client.getInetAddress().getHostAddress() + ",";
                    processos += "Porta: " + Client.getLocalPort() + ",";
                    processos += "Nome: " + dc.getNome() + ",";
                    while(sc.hasNext())
                         processos += sc.nextLine() + "\n";
                    
                } catch (Exception e) { 
                    System.out.println(e); 
                }

                String s = (String)input.readObject();
                if (s.equals("LP")) {
                   output.writeObject(processos);
                   output.flush(); 
                }
                   try{Thread.sleep(1000);}catch(Exception e){}
            } while (status.getText().equals("Conectado"));
            
                // fecha as conexões
                output.close(); input.close(); 
                Client.close();
        }catch(Exception e){
                System.out.println(e.getMessage());
        }
    }   
}
