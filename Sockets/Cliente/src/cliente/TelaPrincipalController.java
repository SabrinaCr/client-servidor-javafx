package cliente;

import conexao.DadosConexao;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TelaPrincipalController implements Initializable {

    private Socket Client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    @FXML
    private TextField txMens;
    @FXML
    private Button btEnviar;
    @FXML
    private Button btConectar;
    @FXML
    private Button btDesconectar;
    @FXML
    private Label lbStatus;
    @FXML
    private TextField txNome;

   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txMens.setDisable(true);
    }    

    @FXML
    private void clkBtEnviar(ActionEvent event) {
       try { 
            output.writeObject(txMens.getText());
            output.flush(); 
           
        } catch (IOException ex) {            
           Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        } 
       txMens.clear();
    }

    @FXML
    private void clkBtConectar(ActionEvent event) {
         try {
            Client = new Socket("127.0.0.1" , 8081);
            output = new ObjectOutputStream(Client.getOutputStream());
            input = new ObjectInputStream(Client.getInputStream());
            btConectar.setDisable(true);
            btDesconectar.setDisable(false);
            btEnviar.setDisable(false);
            txMens.setDisable(false);
            lbStatus.setText("Conectado");
            
            //--
            DadosConexao dc = new DadosConexao(txNome.getText(), Client.getInetAddress().getHostAddress(), Client.getLocalPort());
            SocketThread th = new SocketThread(Client,output,input,lbStatus, dc);
            Thread newThrd = new Thread(th);
            newThrd.start(); // inicia a thread
            
            
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        } 
    }

    @FXML
    private void clkBtDesconectar(ActionEvent event) 
    {
        try {
            output.writeObject("FIM");
            output.flush(); 
            Client.close(); 
            output.close(); 
            input.close(); 
            btConectar.setDisable(false);
            btDesconectar.setDisable(true);
            btEnviar.setDisable(true);
            txMens.setDisable(true);
            lbStatus.setText("Desconectado");
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        }
            
    }
    
}
