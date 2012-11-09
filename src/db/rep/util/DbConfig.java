package db.rep.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Decebal Suiu
 */
public class DbConfig {

    private String url;
    private String user;
    private String password;

    public DbConfig() {
    }

    public DbConfig(String url, String user, String passwd) {
        this.url = url;
        this.user = user;
        this.password = passwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("url", url)
                .append("user", user)
                .append("password", password)
                .toString();
    }

}
