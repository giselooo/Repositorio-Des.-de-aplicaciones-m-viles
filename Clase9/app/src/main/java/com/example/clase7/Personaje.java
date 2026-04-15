package com.example.clase7;

public class Personaje {
    String name;
    String desc;
    String photo;
    int atack;
    int def;

    public Personaje(String name, String desc, String photo, int atack, int def) {
        this.name = name;
        this.desc = desc;
        this.photo = photo;
        this.atack = atack;
        this.def = def;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAtack() {
        return atack;
    }

    public void setAtack(int atack) {
        this.atack = atack;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }
}
