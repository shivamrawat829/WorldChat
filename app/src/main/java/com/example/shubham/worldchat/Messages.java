package com.example.shubham.worldchat;


public class Messages {

    private  String message;
    private String from;

    public Messages(String from,String message){
        this.from=from;
        this.message=message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public  Messages()
    {}


}