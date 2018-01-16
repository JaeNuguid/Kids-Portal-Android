package jae.KidsPortal.Browser;

/**
 * Created by Jae on 1/16/2018.
 */


public class Accounts {

    private Boolean changed = false;
    private String pass;

    public Accounts(Boolean changed, String pass){
        setChanged(changed);
        setPass(pass);
    }

    public Accounts(){

    }
    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
