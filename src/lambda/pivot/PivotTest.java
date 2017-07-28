package lambda.pivot;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Print to console a pivot which counts a total sum for two dimensions (Team & Year)
 *
 *   ----------------------------------------
 *             |      ATL|      BBC|      TVR|
 *   ----------------------------------------
 *         1985|  1965000|  1258333|        0|
 *   ----------------------------------------
 *         1986|  1420000|  1803333|        0|
 *   ----------------------------------------
 *         1987|        0|   909000|   300000|
 *   ----------------------------------------
 *
 * Created by Mihai Dinca-Panaitescu on 12.04.2017.
 */
public class PivotTest {

    public static int COLUMN_WIDTH = 9;
    public static String COLUMN_DELIMITER = "|";
    public static String LINE_DELIMITER = "-";

    public static void main(String[] args) {
        try {
            List<Player> list = CsvDataLoader.loadAsList("C:/Projects/test-git/test/players.csv");
            list.forEach(System.out::println);

            Map<YearTeam, List<Player>> map = CsvDataLoader.loadAsPivot("C:/Projects/test-git/test/players.csv");

            // print  headers
            Set<String> teams = map
                    .keySet()
                    .stream()
                    .map(x -> x.getTeamID())
                    .collect(Collectors.toCollection(TreeSet::new));

            int columns = teams.size()+1;

            printLineDelimiter(columns);
            printTeamsHeader(teams);
            printLineDelimiter(columns);

            // print data
            Set<Integer> years = map
                    .keySet()
                    .stream()
                    .map(x -> x.getYear())
                    .collect(Collectors.toSet());

            years
                    .stream()
                    .forEach(y -> {
                        printYear(y);
                        teams.stream().forEach(t -> {
                            YearTeam yt = new YearTeam(y, t);
                            List<Player> players = map.get(yt);
                            long total = players == null ? 0 :
                                    players.stream()
                                           .collect(Collectors.summingLong(Player::getSalary));
                            printTotal(total);
                            System.out.print(COLUMN_DELIMITER);
                        });
                        printLineDelimiter(columns);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printLineDelimiter(int columns) {
        System.out.println();
        System.out.println(String.join("", Collections.nCopies(columns*(COLUMN_WIDTH +1), LINE_DELIMITER)));
    }

    private static void printTeamsHeader(Set<String> teams) {
        System.out.printf("%" + (COLUMN_WIDTH + 1) + "s", COLUMN_DELIMITER);
        teams.stream().forEach(t -> System.out.printf("%" + (COLUMN_WIDTH + 1) + "s", t + COLUMN_DELIMITER));
    }

    private static void printYear(int year) {
        System.out.printf("%" + (COLUMN_WIDTH +1) + "s" , year + COLUMN_DELIMITER);
    }

    private static void printTotal(long total) {
        System.out.printf("%" + COLUMN_WIDTH + "s", total);
    }

}
