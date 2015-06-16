package ryrycipe.model.wrapper;

import ryrycipe.model.DropBoxAccount;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Helper class to wrap a list of {@link ryrycipe.model.DropBoxAccount} to save it in a xml file.
 */
@XmlRootElement(name = "accounts")
public class DropBoxAccountWrapper {


    private List<DropBoxAccount> DPAccounts;

    @XmlElement(name = "account")
    public List<DropBoxAccount> getDPAccounts() {
        return DPAccounts;
    }

    public void setDPAccounts(List<DropBoxAccount> DPAccounts) {
        this.DPAccounts = DPAccounts;
    }
}
