package new1_7;

import java.util.Objects;

public class ObjectsAPI {
    public static void main(String[] args) {
        String [] array1 = {"a", "b", null};
        String [] array2 = {"a", "b", null};

        System.out.println(array1.equals(array2));

        System.out.println(Objects.equals(array1, array2));
        System.out.println(Objects.deepEquals(array1, array2));
        System.out.println(Objects.hash(array1, array2));

        //array1[2] = Objects.requireNonNull(System.getProperty("undefined"), "No NULL please!");
    }
}

