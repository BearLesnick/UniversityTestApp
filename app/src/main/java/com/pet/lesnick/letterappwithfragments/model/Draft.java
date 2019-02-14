package com.pet.lesnick.letterappwithfragments.model;

public class Draft {
    private String content;
    private int id;
    private String header;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Draft) {
            Draft object = (Draft) obj;
            if (this.id == object.getId() && header == object.getHeader() && content == object.getContent()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
