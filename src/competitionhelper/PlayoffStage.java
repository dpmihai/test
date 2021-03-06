package competitionhelper;


/**
 *  Must start with a number of users which is power(2)
 *  If this number is different than power(2) then a preliminary round must be done
 *  (some players are automatically pushed to first round, others must take preliminary round)
 *  
 *  2^N <= u < 2^(N+1)
 *  must play preliminary round 2*(u-2^N) users
 *  automatically to first round 2^(N+1)-u users
 * 
 * @author mihai.panaitescu
 *
 */
public class PlayoffStage {
	
	private String[] users;
	private int count;
	private int N;

	public PlayoffStage(String[] users) {		
		this.users = users;
		this.count = users.length;
		this.N = findN();
	}
	
	public boolean hasPreliminaryRound() {		
		return count != Math.pow(2, N);
	}
	
	public int getAutoQualified() {
		Double d = Math.pow(2, N+1);
		return d.intValue() - count;
	}
	
	public int getPreliminaryPlayers() {
		Double d = Math.pow(2, N);
		return 2 * (count - d.intValue());
	}
	
	public String getStageName() {
		if (hasPreliminaryRound()) {
			return "Tur Preliminar";
		}
		Double d = Math.pow(2, N);
		int val = d.intValue();
		if (val == 32) {
			return "Saisprezecimi";
		} else if (val == 16) {
			return "Optimi";
		} else if (val == 8) {
			return "Sferturi";
		} else if (val == 4) {
			return "Semifinale";
		} else if (val == 2) {
			return "Finala";
		}
		return "Runda de " + val;
	}
	
	// find N so 2^N <= u < 2^(N+1)
	private int findN() {
		Double d = Math.log(count)/Math.log(2);
		int N = d.intValue();
		//System.out.println("Users=" + count + " N="+N);
		return N;
	}
	
	public static void main(String[] args) {
		PlayoffStage ps = new PlayoffStage(new String[] {"john", "mike", "claudia", "maria", "gabi", "teo", "teo.jr", "horia", "daniel", "bogdand", "cosmin"});
		System.out.println("hasPreliminaryRound=" + ps.hasPreliminaryRound());
		System.out.println("autoQualified=" + ps.getAutoQualified());
		System.out.println("preliminaryPlayers=" + ps.getPreliminaryPlayers());
		System.out.println("stageName=" + ps.getStageName());
	}
	
	

}
