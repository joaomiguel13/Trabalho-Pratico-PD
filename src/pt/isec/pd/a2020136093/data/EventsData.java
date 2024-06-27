package pt.isec.pd.a2020136093.data;

import java.io.Serializable;

public class EventsData implements Serializable{

    private String id;
    private String name;
    private String code;
    private String local;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String nPresences;

    public EventsData(String id, String name, String code, String local, String date, String timeStart, String timeEnd, String nPresences) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.local = local;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.nPresences = nPresences;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCode(){
        return code;
    }
    public String getLocal() {
        return local;
    }
    public String getDate() {
        return date;
    }
    public String getTimeStart() {
        return timeStart;
    }
    public String getTimeEnd() {
        return timeEnd;
    }
    public String getnPresences() {
        return nPresences;
    }
}
