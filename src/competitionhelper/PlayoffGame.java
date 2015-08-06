package competitionhelper;

public class PlayoffGame {
	
	private String hostUser;
	private String awayUser;
	private int hostScore;
	private int awayScore;
	private boolean aet; // after extra time
	private String aetWinner;
	
	public PlayoffGame(String hostUser, String awayUser) {
		super();
		this.hostUser = hostUser;
		this.awayUser = awayUser;
		this.hostScore = -1;
		this.awayScore = -1;
	}

	public String getHostUser() {
		return hostUser;
	}

	public String getAwayUser() {
		return awayUser;
	}

	public int getHostScore() {
		return hostScore;
	}

	public void setHostScore(int hostScore) {
		this.hostScore = hostScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
	
	public boolean isAet() {
		return aet;
	}

	public void setAet(boolean aet) {
		this.aet = aet;
	}
	
	public String getAetWinner() {
		return aetWinner;
	}

	public void setAetWinner(String aetWinner) {
		this.aetWinner = aetWinner;
	}

	public boolean played() {
		return (hostScore != -1) && (awayScore != -1);
	}

	@Override
	public String toString() {
		return "PlayoffGame [hostUser=" + hostUser + ", awayUser=" + awayUser
				+ ", hostScore=" + hostScore + ", awayScore=" + awayScore + ", aet=" + aet + ", aetWinner=" + aetWinner + "]";
	}
	
	public String toPrettyString() {
		StringBuilder sb = new StringBuilder();
		sb.append(fill(hostUser,10, true)).append(" - ").append(fill(awayUser, 10, false));
		sb.append("\t").append(fill(String.valueOf(hostScore), 2, true)).append(" : ").append(fill(String.valueOf(awayScore),2, false));
		if (aet) {
			sb.append("\t\t-> ").append(aetWinner);
			sb.append("  (11m)");
		} else {
			String winner = hostScore > awayScore ? hostUser : awayUser;
			sb.append("\t\t-> ").append(winner);
		}
		return sb.toString();
	}
	
	private String fill(String s, int characters, boolean prefix) {
		int len = s.length();
		if (len < characters) {
			int no = characters - len;
			StringBuilder sb = new StringBuilder();
			if (prefix) {
				for (int i=0; i<no; i++) {
					sb.append(" ");
				}
			}
			sb.append(s);
			if (!prefix) {
				for (int i=0; i<no; i++) {
					sb.append(" ");
				}
			}
			return sb.toString();
		}
		return s;
	}
	
	

}
