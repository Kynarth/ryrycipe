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
     * Define if the user entered the access token for the account.
     */
    private boolean isAuthenticated = false;

    public DropBoxAccount() {}

    public DropBoxAccount(String name) {
        this.name = name;
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
