package pt.isec.pd.a2020136093.utils;

import pt.isec.pd.a2020136093.data.EventsList;

import java.io.Serializable;
import java.util.ArrayList;

public class RESPONSE_SERVER_TO_CLIENT_OR_ADMIN implements Serializable {
    public static final long serialVersionUID = 1L;

    public String response;
    public boolean resultado;

    public ArrayList<String> clientData = new ArrayList<>();

    public ArrayList<ArrayList<String>> clientEventsData = new ArrayList<>();
    public ArrayList<ArrayList<String>> presencesADMIN = new ArrayList<>();
    public EventsList eventsList = new EventsList();

}
