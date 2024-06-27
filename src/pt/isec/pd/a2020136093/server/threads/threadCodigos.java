package pt.isec.pd.a2020136093.server.threads;

import pt.isec.pd.a2020136093.server.model.jdbc.ManageDB;
import pt.isec.pd.a2020136093.utils.Codigo;

public class threadCodigos extends Thread{
    ManageDB manageDB;
    Codigo codigo;
    public threadCodigos(ManageDB manageDB, Codigo codigo) {
        this.manageDB = manageDB;
        this.codigo = codigo;
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(60000);     // 1 min

                codigo.setTempo(codigo.getTempo() - 1);
                if(codigo.getTempo() == 0){
                    manageDB.removeCodigo(codigo);

                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
