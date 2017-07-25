package lambda.pivot;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by Mihai Dinca-Panaitescu on 12.04.2017.
 */
public class PivotTest {

    public static void main(String[] args) {
        try {
            List<Player> list = CsvDataLoader.loadAsList("C:/Projects/Test/src/resources/players.csv");
            list.forEach(System.out::println);

            Map<YearTeam, List<Player>> map = CsvDataLoader.loadAsPivot("C:/Projects/Test/src/resources/players.csv");

            // print  headers
            Set<String> teams = map
                    .keySet()
                    .stream()
                    .map(x -> x.getTeamID())
                    .collect(Collectors.toCollection(TreeSet::new));
            System.out.print(',');
            teams.stream().forEach(t -> System.out.print(t + ","));
            System.out.println();

            // print data
            Set<Integer> years = map
                    .keySet()
                    .stream()
                    .map(x -> x.getYear())
                    .collect(Collectors.toSet());

            years
                    .stream()
                    .forEach(y -> {
                        System.out.print(y + ",");
                        teams.stream().forEach(t -> {
                            YearTeam yt = new YearTeam(y, t);
                            List<Player> players = map.get(yt);
                            if ( players != null ) {
                                long total = players
                                        .stream()
                                        .collect(Collectors.summingLong(Player::getSalary));
                                System.out.print(total);
                            }
                            System.out.print(',');
                        });
                        System.out.println();
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
