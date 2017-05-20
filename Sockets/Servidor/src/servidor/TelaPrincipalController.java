/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class TelaPrincipalController implements Initializable {
    
    ServerSocket server;
    Thread newThrd;
    SocketThreadPrincipal th;
    boolean ativo=false;  
    
    @FXML
    private Button btAbrir;
    @FXML
    private Button btFechar;
    @FXML
    private Button btSair;
    @FXML
    private Label lbStatus;
    @FXML
    private ListView<String> list;
    @FXML
    private TableView <Mensagem> tabela;
    @FXML
    private TableColumn colIp;
    @FXML
    private TableColumn colMens;
    @FXML
    private TableColumn tcsPorta;
    @FXML
    private TableColumn tcsNome;

     public boolean isAtivo() {
        return ativo;
    }
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       colIp.setCellValueFactory(new PropertyValueFactory<>("ip"));
       colMens.setCellValueFactory(new PropertyValueFactory<>("mens"));
       tcsPorta.setCellValueFactory(new PropertyValueFactory<> ("porta"));
       tcsNome.setCellValueFactory(new PropertyValueFactory<> ("nome"));
    }    

    @FXML
    private void clkBtAbrir(ActionEvent event) 
    {
        try {
            server = new ServerSocket(8081,100);  // porta e n. max conexões
            th=new SocketThreadPrincipal(this,server);
            newThrd = new Thread(th);
            newThrd.start(); // inicia a thread
            ativo=true;
            btAbrir.setDisable(true);
            btFechar.setDisable(false);
            lbStatus.setText("Servidor em "+server.getInetAddress().getCanonicalHostName());
         } catch (IOException ex) {
           Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        }     
    }

    @FXML
    private void clkBtFechar(ActionEvent event) {
        try {
           server.close();
           newThrd.interrupt();
           ativo=false; // seta o flag e faz com que todas as threads servidoras sejam finalizadas
           btAbrir.setDisable(false);
           btFechar.setDisable(true);
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void clkBtSair(ActionEvent event) {
        if(server!=null) clkBtFechar(event);
        Platform.exit();
    }
    public TableView getTabMensagens()
    {  
        return tabela;
    }
    
    public ListView getListaConectados()
    {  
        return list;
    }

    @FXML
    private void clkLista(MouseEvent event) {
        try
        {
            ObjectOutputStream output = th.getOutput();
            ObjectInputStream input = th.getInput();
            output.writeObject("LP");
        }catch(Exception e){
                System.out.println(e.getMessage());
        }
    }
    
}
