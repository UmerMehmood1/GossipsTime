package com.app.gossipstime;


public class MessageModal {
    String message_id;
    String message;
    String search;
    String delete;
    String time;
    String suid;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    String ruid;
    String url;
    String type;
    String mno;
    int reaction;
    int reactionrec;


    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }



    public MessageModal(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getReaction() {
        return reaction;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }

    public int getReactionrec() {
        return reactionrec;
    }

    public void setReactionrec(int reactionrec) {
        this.reactionrec = reactionrec;
    }


}
