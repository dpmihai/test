package youtube;

/**
 * User: mihai.panaitescu
 * Date: 21-May-2010
 * Time: 15:08:03
 */
public class YouTubeMedia {

    private String location;
    private String type;

    public YouTubeMedia(String location, String type) {
        super();
        this.location = location;
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}