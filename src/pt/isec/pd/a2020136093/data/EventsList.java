package pt.isec.pd.a2020136093.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventsList implements Serializable {
    private List<EventsData> listEvents;

    public EventsList() {
        listEvents = new ArrayList<>();
    }

    public void addEvent(EventsData event){
        listEvents.add(event);
    }

    public EventsData getEvent(int index){
        return listEvents.get(index);
    }

    public int getSize(){
        return listEvents.size();
    }

    public void deleteEvent(int id){
        listEvents.removeIf(event -> Integer.parseInt(event.getId()) == id);
    }
}
