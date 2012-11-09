package swingx;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Jan 16, 2008
 * Time: 3:01:19 PM
 */
public class Permission {

    private String name;
    private String initials;
    private String description;
    private boolean selected;

    public Permission(String name, String initials) {
        this.name = name;
        this.initials = initials;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getInitials() {
        return initials;
    }

    public void set(boolean selected) {
        this.selected = selected;
    }

    public boolean isSet() {
        return selected;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }


}
