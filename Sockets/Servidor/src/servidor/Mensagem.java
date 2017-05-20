/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author Aluno
 */
public class Mensagem 
{
    private String ip, mens, porta;

    public Mensagem(String ip, String mens) {
        this.ip = ip;
        this.mens = mens;
    }

    public String getIp() {
        return ip;
    }
    
    public String getPorta() {
        return porta;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMens() {
        return mens;
    }

    public void setMens(String mens) {
        this.mens = mens;
    }
    
    
}
