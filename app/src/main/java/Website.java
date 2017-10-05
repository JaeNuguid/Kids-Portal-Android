package jae.KidsPortal.Browser;

/**
 * Created by Jae on 10/3/2017.
 */

public class Website {

    private String title;
    private String url;
    private String date;

    public Website(String title, String url, String date){
        this.title= title;
        this.url =url;
        this.date = date;
    }

    public Website(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
