/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import model.Cliente;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author AZUS
 */
public class TelaPrincipalController implements Initializable {
    
    private Label label;
    @FXML
    private TextField txNome;
    @FXML
    private Button btnConectar;
    @FXML
    private Button btnDesconectar;
    @FXML
    private Label lblStatus;
    @FXML
    private TextArea txMensagem;
    @FXML
    private Button btnEnviar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ListView<?> list;
    @FXML
    private TableView<Cliente> tabelaOn;
    @FXML
    private TableColumn colNome;
    @FXML
    private TableColumn colIP;
    @FXML
    private TableColumn colPorta;
    
    private Socket Client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNome.setCellValueFactory(new PropertyValueFactory<> ("nome"));
        colIP.setCellValueFactory(new PropertyValueFactory<>("ip"));
        txMensagem.setDisable(true);
        btnEnviar.setDisable(true);
        btnDesconectar.setDisable(true);
        btnCancelar.setDisable(true);
        lblStatus.setText("Desconectado");
    }    

    @FXML
    private void evtConectar(ActionEvent event) {
        try 
        {
            Client = new Socket("127.0.0.1" , 8081);
            ServerSocket clientListner = new ServerSocket(8081);
            
            output = new ObjectOutputStream(Client.getOutputStream());
            input = new ObjectInputStream(Client.getInputStream());
            btnConectar.setDisable(true);
            btnDesconectar.setDisable(false);
            btnEnviar.setDisable(false);
            txMensagem.setDisable(false);
            lblStatus.setText("Conectado");
            
            String protocolo = "<#@CONNECT#IPCLIENT="+ Client.getInetAddress() 
                                       + "#NOME="+ txNome.getText() +""
                                       + "#PORTA="+ Client.getLocalPort()+">";
            output.writeObject(protocolo);
            //tabelaOn.getItems().addAll((ArrayList<Cliente>)(input.readObject()));
            //--
//            dc = new DadosConexao(txNome.getText(), Client.getInetAddress().getHostAddress(), Client.getLocalPort());
            SocketThreadCliente th = new SocketThreadCliente(Client, clientListner, lblStatus);
            Thread newThrd = new Thread(th);
            newThrd.start(); // inicia a thread
            

        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        } 
    }

    @FXML
    private void evtDesconectar(ActionEvent event) {
        try {
            String protocoloDisconnect = "<#@DISCONNECT#IPCLIENT="+ Client.getInetAddress() +">";
           
            output.writeObject(protocoloDisconnect);
            output.flush(); 
            
            Client.close(); 
            output.close(); 
            input.close(); 
            
            btnConectar.setDisable(false);
            btnDesconectar.setDisable(true);
            btnEnviar.setDisable(true);
            txMensagem.setDisable(true);
            lblStatus.setText("Desconectado");
            
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void evtEnviar(ActionEvent event) {
        try { 
            String protocoloSend = "<#@MSG#IPORIGEM="+ Client.getInetAddress() + "#TXT="+ txMensagem.getText() +">";
            output.writeObject(protocoloSend);
            output.flush(); 
        } catch (IOException ex) {            
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        } 
       txMensagem.clear();
    }
    
    @FXML
    private void evtCancelar(ActionEvent event) {
    }
    
}
