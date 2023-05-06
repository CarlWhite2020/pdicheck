package com.eucleia.tabscanap.bean.model;

public class BlueModel {

    private String btName;
    private String btMac;

    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    public String getBtMac() {
        return btMac;
    }

    public void setBtMac(String btMac) {
        this.btMac = btMac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlueModel model = (BlueModel) o;
        return btMac.equals(model.btMac);
    }

    @Override
    public int hashCode() {
        return btMac.hashCode();
    }
}
