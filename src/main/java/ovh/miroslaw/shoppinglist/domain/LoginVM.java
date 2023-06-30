package ovh.miroslaw.shoppinglist.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    private Boolean rememberMe;

    public LoginVM() {
    }
    public LoginVM(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
               "username='" + username + '\'' +
               ", rememberMe=" + rememberMe +
               '}';
    }
}
