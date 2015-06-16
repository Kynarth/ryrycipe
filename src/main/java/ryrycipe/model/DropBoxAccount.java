package ryrycipe.model;

/**
 * Represents a DropBox Account
 * @see <a href="https://www.dropbox.com">DropBox</a>
 */
public class DropBoxAccount {

    /**
     * Account name.
     */
    private String name;

    /**
     * Token to get access to the associated dropbox.
     */
    private String accessToken;

    /**
     * Define if the user entered the access token for the account.
     */
    private boolean isAuthenticated = false;

    public DropBoxAccount() {}

    public DropBoxAccount(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
