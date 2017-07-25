package lambda.pivot;

/**
 * Created by Mihai Dinca-Panaitescu on 12.04.2017.
 */
public class Player {

    private int year;
    private String teamID;
    private String lgID;
    private String playerID;
    private int salary;

    public Player(int year, String teamID, String lgID, String playerID, int salary) {
        this.year = year;
        this.teamID = teamID;
        this.lgID = lgID;
        this.playerID = playerID;
        this.salary = salary;
    }

    public int getYear() {
        return year;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getLgID() {
        return lgID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (year != player.year) return false;
        if (salary != player.salary) return false;
        if (teamID != null ? !teamID.equals(player.teamID) : player.teamID != null) return false;
        if (lgID != null ? !lgID.equals(player.lgID) : player.lgID != null) return false;
        return !(playerID != null ? !playerID.equals(player.playerID) : player.playerID != null);

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + (teamID != null ? teamID.hashCode() : 0);
        result = 31 * result + (lgID != null ? lgID.hashCode() : 0);
        result = 31 * result + (playerID != null ? playerID.hashCode() : 0);
        result = 31 * result + salary;
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "year=" + year +
                ", teamID='" + teamID + '\'' +
                ", lgID='" + lgID + '\'' +
                ", playerID='" + playerID + '\'' +
                ", salary=" + salary +
                '}';
    }
}
