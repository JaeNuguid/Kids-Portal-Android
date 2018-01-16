package jae.KidsPortal.Browser;

/**
 * Created by Jae on 1/16/2018.
 */

public class Configs {

    private String color;
    private String color2;
    private String homepage;
    private int index;
    private Boolean protect;
    private Boolean secure;
    private String status;
    private String user;
    private String pass;
    public Configs(String color, String color2, String homepage, int index,String pass, boolean protect, boolean secure, String status, String user){
        setColor(color);
        setColor2(color2);
        setHomepage(homepage);
        setIndex(index);
        setProtect(protect);
        setSecure(secure);
        setStatus(status);
        setUser(user);
        setPass(pass);
    }

    public Configs(){

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Boolean getProtect() {
        return protect;
    }

    public void setProtect(Boolean protect) {
        this.protect = protect;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}