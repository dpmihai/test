package lambda.pivot;

/**
 * Created by Mihai Dinca-Panaitescu on 12.04.2017.
 *
 * Defines the class that we use as the container for the pivot columns. These are the columns that the
 * data is grouped by. If using SQL, these columns would appear in the “GROUP BY” clause.
 */
public class YearTeam
{
    private int year;
    private String teamID;

    public YearTeam(int year,String teamID) {
        this.year = year;
        this.teamID = teamID;
    }

    public int getYear() {
        return year;
    }

    public String getTeamID() {
        return teamID;
    }

    @Override
    public boolean equals(Object other)
    {
        if ( other == null ) return false;
        if ( this == other ) return true;
        if ( other instanceof YearTeam ) {
            YearTeam yt = (YearTeam)other;
            if ( year == yt.year && teamID.equals(yt.teamID) )
                return true;
        }
        return false;
    }
    @Override
    public int hashCode()
    {
        int hash = 1;
        hash = hash * 17 + year;
        hash = hash * 31 + teamID.hashCode();
        return hash;
    }
    @Override
    public String toString()
    {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append('[').append(year).append(", ").append(teamID)
                .append(']');
        return sbuf.toString();
    }
}
