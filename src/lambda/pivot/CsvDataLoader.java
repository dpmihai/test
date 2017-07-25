package lambda.pivot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Mihai Dinca-Panaitescu on 12.04.2017.
 */
public class CsvDataLoader {

    public static List<Player> loadAsList(String fileName) throws Exception {
        List<Player> players = new ArrayList<Player>();
        Pattern pattern = Pattern.compile(",");
        try (BufferedReader in = new BufferedReader(new FileReader(fileName));){
            players = in
                    .lines()
                    .skip(1)
                    .map(line -> {
                        String[] arr = pattern.split(line);
                        return new Player(Integer.parseInt(arr[0]),
                                arr[1],
                                arr[2],
                                arr[3],
                                Integer.parseInt(arr[4]));
                    })
                    .collect(Collectors.toList());
        }
        return players;
    }

    public static Map<YearTeam, List<Player>> loadAsPivot(String fileName) throws Exception {
        Pattern pattern = Pattern.compile(",");
        Map<YearTeam, List<Player>> grouped = new HashMap<YearTeam, List<Player>>();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName));) {
            grouped = in
                    .lines()
                    .skip(1)
                    .map(line -> {
                        String[] arr = pattern.split(line);
                        return new Player(Integer.parseInt(arr[0]),
                                arr[1],
                                arr[2],
                                arr[3],
                                Integer.parseInt(arr[4]));
                    })
                    .collect(Collectors.groupingBy(x -> new YearTeam(x.getYear(), x.getTeamID())));
        }
        return grouped;
    }
}
