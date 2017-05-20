/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;


public class DadosConexao {
    private String nome, iplcliente;
    int porta;

    public DadosConexao(String nome, String iplcliente, int porta) {
        this.nome = nome;
        this.iplcliente = iplcliente;
        this.porta = porta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIplcliente() {
        return iplcliente;
    }

    public void setIplcliente(String iplcliente) {
        this.iplcliente = iplcliente;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    
}
