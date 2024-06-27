package pt.isec.pd.a2020136093.client.data;

import java.io.Serializable;

public class ClientData implements Serializable {
    private Boolean isAdmin;
    private String name;
    private String email;
    private String password;
    private String nIdentificacao;

    public ClientData() {

    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNIdentificacao() {
        return nIdentificacao;
    }

    public void setNIdentificacao(String nIdentificacao) {
        this.nIdentificacao = nIdentificacao;
    }

}
