package com.eucleia.pdicheck.bean.normal;

import java.io.Serializable;

public class VciVersion implements Serializable {

    private String version;
    private String hint;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
