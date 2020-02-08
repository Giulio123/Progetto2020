package it.guaraldi.to_dotaskmanager.data.remote;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Msg {

    @SerializedName("msg")
    private String msg;

    public Msg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
