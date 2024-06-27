package pt.isec.pd.a2020136093.utils;

import java.io.Serializable;

public class REQUEST_ADMIN_TO_SERVER implements Serializable {
    public static final long serialVersionUID = 1L;

    public boolean isAdmin = true;

    public int msgCode;
    public int id;
    public String name;
    public String local;
    public String date;
    public String timeStart;
    public String timeEnd;

    public String emailToManagePresence;

    public int eventTime;


    public int eventCode;
}
