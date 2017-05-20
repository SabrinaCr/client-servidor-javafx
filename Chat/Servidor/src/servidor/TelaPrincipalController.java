/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import threads.SocketThreadPrincipalServidor;
import model.Mensagem;
import model.Cliente;
import java.io.IOException;
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

/**
 *
 * @author AZUS
 */
public class TelaPrincipalController implements Initializable {
    
    private Label label;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnEncerrar;
    @FXML
    private Button btnSair;
    @FXML
    private TableView<Mensagem> tabela;
    @FXML
    private TableColumn colNome;
    @FXML
    private TableColumn colMensagem;
    @FXML
    private TableView<Cliente> tabelaOnline;
    @FXML
    private TableColumn colIP;
    @FXML
    private TableColumn colPorta;
    @FXML
    private TableColumn colNomeOn;
    @FXML
    private Label lblStatus;
    
    boolean ativo = false;
    ServerSocket server;
    Thread newThrd;
    SocketThreadPrincipalServidor th;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));
        colNome.setCellValueFactory(new PropertyValueFactory<> ("nome"));
        colNomeOn.setCellValueFactory(new PropertyValueFactory<> ("nome"));
        colPorta.setCellValueFactory(new PropertyValueFactory<> ("porta"));
        colIP.setCellValueFactory(new PropertyValueFactory<>("ip"));
    }    

    @FXML
    private void evtIniciar(ActionEvent event) {
        try {
            server = new ServerSocket(8081, 100);  // porta e n. max conexões
            th = new SocketThreadPrincipalServidor(this, server);
            newThrd = new Thread(th);
            newThrd.start(); // inicia a thread
            ativo = true;
            btnIniciar.setDisable(true);
            btnEncerrar.setDisable(false);
            lblStatus.setText("Servidor em " + server.getInetAddress().getCanonicalHostName());
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        } 
    }

    @FXML
    private void evtEncerrar(ActionEvent event) {
        try {
            server.close();
            newThrd.interrupt();
            ativo = false; // seta o flag e faz com que todas as threads servidoras sejam finalizadas
            btnIniciar.setDisable(false);
            btnEncerrar.setDisable(true);
        } catch (IOException ex) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText(ex.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void evtSair(ActionEvent event) {
        if(server != null) 
            evtEncerrar(event);
        Platform.exit();
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public TableView getTabelaMensagens()
    {  
        return tabela;
    }
    
    public TableView getTabelaConectados()
    {  
        return tabelaOnline;
    }
    
}
