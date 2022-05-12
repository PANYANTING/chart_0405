package com.example.chart_0405;

import java.util.ArrayList;
import java.util.HashMap;

public class Detail {
    private String Id;
    private String Date;
    private String Name;
    private String Type;
    private String Fee;
    private String Status;

    public Detail(){
    }

    public Detail(String name, String type, String fee, String status){
        Name = name;
        Type = type;
        Fee = fee;
        Status = status;
    }

    public Detail(ArrayList<String> a){
        Id = a.get(0);
        Date = a.get(1);
        Name = a.get(2);
        Type = a.get(3);
        Fee = a.get(4);
        Status = a.get(5);

    }



    //Getter
    public String getId(){return Id;}

    public String getDate(){return Date;}

    public String getName() { return Name;}

    public String getFee() {
        return Fee;
    }

    public String getType(){return Type;}

    public String getStatus(){return Status;}

    //Setter

    public void setName(String name) {
        Name = name;
    }

    public void setFee(String fee) {
        Fee = fee;
    }
}
