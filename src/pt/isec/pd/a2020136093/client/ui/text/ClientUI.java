package pt.isec.pd.a2020136093.client.ui.text;


import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.utils.PAInput;

import java.io.IOException;
import java.util.ArrayList;

public class ClientUI {
    ManageConnections manageConnections;
    private boolean logged = false;

    public ClientUI(ManageConnections manageConnections) {
        this.manageConnections = manageConnections;
    }

    public void start() {
        int option;
        do {
            option = PAInput.chooseOption("Escolha uma opcao:", "Login", "Registar", "Sair");

            switch (option) {
                case 1 -> {
                    login();
                }
                case 2 -> {
                    try {
                        register();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    exit();
                }
            }

            while (logged && !manageConnections.isAdmin()) {
                option = PAInput.chooseOption("Escolha uma opcao:", "Editar dados registo", "Submeter codigo de presenca", "Consultar presencas", "Gerar ficheiro CSV (registo presencas)", "Logout");

                switch (option) {
                    case 1 -> {
                        editarDados();
                    }
                    case 2 -> {
                        submeterCodigo();
                    }
                    case 3 -> {
                        consultarPresencas();
                    }
                    case 4 -> {
                        gerarFicheiroCSV();
                    }
                    case 5 -> {
                        logout();
                    }
                }
            }

            while(logged && manageConnections.isAdmin()){
                option = PAInput.chooseOption("Escolha uma opcao:", "Criar novo evento", "Editar evento", "Eliminar evento", "Consultar eventos", "Gerar codigo para evento", "Consultar presencas em evento", "Gerar ficheiro CSV (presencas em evento)", "Consultar presencas em eventos (por aluno)", "Gerar ficheiro CSV (presencas do aluno)", "Eliminar presencas de um evento", "Inserir presenca em evento", "Logout");
                switch(option){
                    case 1-> {
                        criarEvento();
                    }
                    case 2-> {
                        editarEvento();
                    }
                    case 3-> {
                        eliminarEvento();
                    }
                    case 4-> {
                        consultarEvento();
                    }
                    case 5-> {
                        gerarCodigoEvento();
                    }
                    case 6-> {
                        consultarPresencasEvento();
                    }
                    case 7-> {
                        gerarFicheiroCSVEvento();
                    }
                    case 8->{
                        consultarPresencasPorAluno();
                    }
                    case 9-> {
                        gerarFicheiroCSVPresencas();
                    }
                    case 10-> {
                        eliminarPresencasEvento();
                    }
                    case 11-> {
                        inserirPresencaEvento();
                    }
                    case 12 -> {
                        logout();
                    }
                    default -> {
                        return;
                    }
                }

            }

        } while (true);
    }

    private void criarEvento() {
        String name = PAInput.readString("Nome do evento: ", false);
        String local = PAInput.readString("Local do evento: ", false);
        String date = PAInput.readString("Data do evento [DD-MM-YYYY]: ", false);
        String timeStart = PAInput.readString("Hora de inicio do evento [HH:MM]: ", false);
        String timeEnd = PAInput.readString("Hora de fim do evento [HH:MM]: ", false);

        manageConnections.criarEvento(name, local, date, timeStart, timeEnd);
    }

    private void editarEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);
        ArrayList<ArrayList<String>> listaEventos = manageConnections.checkEvents();

        for(int i = 0; i < listaEventos.size(); i++){
            if(listaEventos.get(i).get(0).equals(idEvento)){
                System.out.printf("%-3s | %-15s | %-15s | %-15s | %-15s | %s\n","ID","Nome","Local","Data","Hora de inicio","Hora de fim");
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.printf("%-3s | %-15s | %-15s | %-15s | %-15s | %s\n",
                        listaEventos.get(i).get(0), listaEventos.get(i).get(1), listaEventos.get(i).get(2),
                        listaEventos.get(i).get(3), listaEventos.get(i).get(4), listaEventos.get(i).get(5));
                System.out.println("------------------------------------------------------------------------------------------");
            }
        }

        int option = PAInput.chooseOption("Escolha uma opcao:", "Nome", "Local", "Data", "Hora de inicio", "Hora de fim", "Voltar");
        switch (option){
            case 1 -> {
                String name = PAInput.readString("Novo Nome: ", false);
                manageConnections.editEvent(idEvento, 1, name);
            }
            case 2 -> {
                String local = PAInput.readString("Novo Local: ", false);
                manageConnections.editEvent(idEvento, 2, local);
            }
            case 3 -> {
                String date = PAInput.readString("Nova Data: ", false);
                manageConnections.editEvent(idEvento, 3, date);
            }
            case 4 -> {
                String timeStart = PAInput.readString("Nova Hora de inicio: ", false);
                manageConnections.editEvent(idEvento, 4, timeStart);
            }
            case 5 -> {
                String timeEnd = PAInput.readString("Nova Hora de fim: ", false);
                manageConnections.editEvent(idEvento, 5, timeEnd);
            }
            default -> {
                return;
            }
        }
    }

    private void eliminarEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);

        manageConnections.deleteEvent(idEvento);
    }

    private void consultarEvento() {
        ArrayList<ArrayList<String>> listaEventos = manageConnections.checkEvents();

        System.out.printf("%-3s | %-15s | %-10s | %-15s | %-15s | %-15s | %-15s | %s\n","ID","Nome","Código","Local","Data","Hora de inicio","Hora de fim","Número presenças");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < listaEventos.size(); i++) {
            System.out.printf("%-3s | %-15s | %-10s | %-15s | %-15s | %-15s | %-15s | %s\n",
                    listaEventos.get(i).get(0), listaEventos.get(i).get(1), listaEventos.get(i).get(2),
                    listaEventos.get(i).get(3), listaEventos.get(i).get(4), listaEventos.get(i).get(5), listaEventos.get(i).get(6), listaEventos.get(i).get(7));

        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
    }

    private void gerarCodigoEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);
        String timeEvento = PAInput.readString("Tempo de duração do código (em minutos): ", true);

        manageConnections.generateEventCode(idEvento, timeEvento);
    }

    private void consultarPresencasEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);

        ArrayList<ArrayList<String>> lista = manageConnections.checkPresencesEvent(idEvento);

        System.out.printf("%-15s | %-20s | %s\n","Nome","Email","nIdentificacao");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

        for(int i=0; i<lista.size(); i++){
            System.out.printf("%-15s | %-20s | %s\n",lista.get(i).get(0),lista.get(i).get(1),lista.get(i).get(2));
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
    }

    private void gerarFicheiroCSVEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);
        System.out.println(manageConnections.generateCSV_event(idEvento));
    }

    private void consultarPresencasPorAluno() {
        String email = PAInput.readString("Email do aluno: ", true);

        ArrayList<ArrayList<String>> listaPresencas  = manageConnections.checkPresences2(email);

        if(listaPresencas == null)
            System.out.println("Não existem presencas para o aluno com o email "+email);

        else {

            System.out.println("REGISTO DE PRESENÇAS DO ALUNO " + email);
            System.out.printf("%-3s | %-15s | %-15s | %-15s | %-15s | %s\n", "ID", "Nome", "Local", "Data", "Hora de inicio", "Hora de fim");
            System.out.println("-------------------------------------------------------------------------------------------------------");
            for (int i = 0; i < listaPresencas.size(); i++) {
                System.out.printf("%-3s | %-15s | %-15s | %-15s | %-15s | %s\n",
                        listaPresencas.get(i).get(0), listaPresencas.get(i).get(1), listaPresencas.get(i).get(2),
                        listaPresencas.get(i).get(3), listaPresencas.get(i).get(4), listaPresencas.get(i).get(5));

            }
            System.out.println("-------------------------------------------------------------------------------------------------------");
        }
    }

    private void gerarFicheiroCSVPresencas() {
        String email = PAInput.readString("Email do aluno: ", true);
        System.out.println(manageConnections.generateCSV_student(email));
    }

    private void eliminarPresencasEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);
        String email = PAInput.readString("Email do aluno: ", true);

        System.out.println(manageConnections.deletePresence(idEvento, email));
    }

    private void inserirPresencaEvento() {
        String idEvento = PAInput.readString("ID do evento: ", true);
        String email = PAInput.readString("Email do aluno: ", true);

        System.out.println(manageConnections.addPresence(idEvento, email));
    }

    
    
    
    
    
    
    
    
    
    
    
    
    // ALUNO
    private void editarDados() {
        System.out.println("Nome: " + manageConnections.getName() + "\nEmail: " + manageConnections.getEmail() + "\nPassword: " + manageConnections.getPassword() + "\nNumero de Identificacao: " + manageConnections.getNIdentificacao());
        int option = PAInput.chooseOption("Escolha uma opcao:", "Nome", "Email", "Password", "Numero de Identificacao", "Voltar");

        switch (option){
            case 1 -> {
                String name = PAInput.readString("Novo Nome: ", false);
                manageConnections.editData(1, name);
            }
            case 2 -> {
                String email = PAInput.readString("Novo Email: ", true);
                manageConnections.editData(2, email);
            }
            case 3 -> {
                String password = PAInput.readString("Nova Password: ", true);
                manageConnections.editData(3, password);
            }
            case 4 -> {
                String nIdentificacao = PAInput.readString("Novo Numero Identificacao: ", true);
                manageConnections.editData(4, nIdentificacao);
            }

            default -> {
                return;
            }
        }
    }

    private void submeterCodigo() {
        String codigo = PAInput.readString("Codigo: ", true);
        System.out.println(manageConnections.submitCode(codigo));
    }

    private void consultarPresencas() {
        ArrayList<ArrayList<String>> listaPresencas = manageConnections.checkPresences();

        System.out.println("REGISTO DE PRESENÇAS");
        System.out.printf("%-3s | %-15s | %-15s | %-15s | %-15s | %s\n","ID","Nome","Local","Data","Hora de inicio","Hora de fim");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < listaPresencas.size(); i++) {
            System.out.printf("%-3s | %-15s | %-15s | %-15s | %-15s | %s\n",
                    listaPresencas.get(i).get(0), listaPresencas.get(i).get(1), listaPresencas.get(i).get(2),
                    listaPresencas.get(i).get(3), listaPresencas.get(i).get(4), listaPresencas.get(i).get(5));

        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
    }

    private void gerarFicheiroCSV() {
        System.out.println(manageConnections.generateCSV_student_own());
    }


    private void login() {
        String email = PAInput.readString("Email: ", true);
        String password = PAInput.readString("Password: ", true);

        if(manageConnections.login(email, password) == 0)
            logged = true;
       }

    private void register() throws IOException {
        String username = PAInput.readString("Nome: ", false);
        String email = PAInput.readString("Email: ", true);
        String password = PAInput.readString("Password: ", true);
        String nIdentificacao = PAInput.readString("Numero de Identificacao: ", true);

        manageConnections.register(email, password, username, nIdentificacao);
    }




    private void logout(){
       if(manageConnections.logout())
           logged = false;
    }

    private void exit() {
        System.out.println("A sair...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


}
