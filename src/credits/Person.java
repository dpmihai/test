package credits;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Author: Mihai Dinca-Panaitescu
 * <p/>
 * User: mihai.panaitescu
 * <p/>
 * Date: Jul 1, 2005 Time: 4:49:18 PM
 */
class Person {

    // person's image
    private String image;
    // person's description - every element is a line in person's card
    private String[] description;

    public Person(String[] description, String image) {
        this.description = description;
        this.image = image;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
