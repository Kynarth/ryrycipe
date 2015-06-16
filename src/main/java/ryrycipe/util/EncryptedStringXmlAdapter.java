package ryrycipe.util;


import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.prefs.Preferences;

/**
 * Adapter to encrypt/decrypt xml strings. Used for dropbox access token.
 */
public class EncryptedStringXmlAdapter extends XmlAdapter<String,String> {

    private final static Logger LOGGER = LoggerFactory.getLogger(EncryptedStringXmlAdapter.class.getName());

    private final PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public EncryptedStringXmlAdapter() {
        Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);
        String key = prefs.get("cryptKey", "");

        if (key == null) {
            LOGGER.error("Couldn't retrieve user's key to encrypt DP acces token.");
        }

        encryptor.setPassword(key);
    }

    /**
     * Encrypt access token.
     *
     * @param v String to convert
     * @return String - decrypt access token
     * @throws Exception
     */
    @Override
    public String unmarshal(String v) throws Exception {
//        return PropertyValueEncryptionUtils.encrypt(v, encryptor);

        if (PropertyValueEncryptionUtils.isEncryptedValue(v))
            return PropertyValueEncryptionUtils.decrypt(v, encryptor);

        return v;
    }

    /**
     * Decrypt access token.
     *
     * @param v String to convert
     * @return String - encrypt access token
     * @throws Exception
     */
    @Override
    public String marshal(String v) throws Exception {
        return PropertyValueEncryptionUtils.encrypt(v, encryptor);
    }
}
