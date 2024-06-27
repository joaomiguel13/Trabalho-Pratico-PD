package pt.isec.pd.a2020136093.utils;

import java.io.Serializable;

public class REQUEST_CLIENT_TO_SERVER implements Serializable {
    public static final long serialVersionUID = 1L;

    public boolean isAdmin = false;

    public int msgCode;
    public String name;
    public String email;
    public String password;
    public String nIdentificacao;

    public String eventCode;

    public String novoEmail;
}

