package instatag.com.firebase;

/**
 * Created by RahulReign on 04-11-2017.
 */

public class UserInformation {
    private  String name,email,pnone_no;

    public UserInformation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPnone_no() {
        return pnone_no;
    }

    public void setPnone_no(String pnone_no) {
        this.pnone_no = pnone_no;
    }
}
