package competitionhelper;

import java.util.Arrays;
import java.util.Random;

public class PlayoffManager {
	
	private String[] users;
	private PlayoffGame[] games;
	private boolean finished = false;
	private Object finishedLock = new Object();

	public PlayoffManager(String[] users) {		
		this.users = users;		
	}
	
	public void compute() {
		String[]  stageUsers = new String[users.length];
		System.arraycopy(users, 0, stageUsers, 0, users.length);
		
		while (stageUsers.length > 1) {
		
			PlayoffStage ps = new PlayoffStage(stageUsers);
			
			String[] shuffled = shuffleUsers(stageUsers);
			String[] players;
			String[] autoQualified = new String[0];
			if (ps.hasPreliminaryRound()) {
				// preliminary round 
				int playersNo = ps.getPreliminaryPlayers();
				players = new String[playersNo];
				autoQualified = new String[stageUsers.length - playersNo];
				System.arraycopy(shuffled, 0, players, 0, playersNo);
				System.arraycopy(shuffled, playersNo, autoQualified, 0, stageUsers.length - playersNo);
			} else {
				// all users are qualified to first round			
				players = shuffled;						
			}
			
			games = new PlayoffGame[players.length/2];
			
			System.out.println("\n--- " + ps.getStageName() + " ---");
			System.out.println("Participanti: " + Arrays.asList(players));
			if (ps.hasPreliminaryRound()) {
				System.out.println("Calificati automat : " + Arrays.asList(autoQualified));
			}
			System.out.println();
			int k =0;
			for (int i=0, size=players.length; i<size-1; i+=2) {
				games[k] = new PlayoffGame(players[i], players[i+1]);				
				k++;						
			}
			
			if (!games[0].played()) {				
				try {
					synchronized(this) {
						//waitForScores.wait();
						wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//setScores(games);
			
			if (ps.hasPreliminaryRound()) {
				stageUsers = concatArrays(autoQualified, getQualifiedUsers(games));
			} else {
				stageUsers = getQualifiedUsers(games);
			}
			
			for (int i=0, size=games.length; i<size; i++) {				
				System.out.println(games[i].toPrettyString());
			}	
								
		}
		
		synchronized(finishedLock) {
			finished = true;
		}
		System.out.println("\n--- Castigator ---");
		System.out.println(stageUsers[0]);
	}
	
	private boolean find(String[] array, String element) {
		for (int i=0, size=array.length; i<size; i++) {
			if ((array[i] != null) && array[i].equals(element)) {
				return true;
			}
		}
		return false;
	}

	// Implementing Fisher–Yates shuffle
	private void shuffleArray(String[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			String a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}
	
	private String[] shuffleUsers(String[] users) {
		int playersNo = users.length;
		String[] players = new String[playersNo];
		System.arraycopy(users, 0, players, 0, playersNo); 
		shuffleArray(players);
		return players;
	}
	
	private void setScores(PlayoffGame[] games) {
		for (int i=0, size=games.length; i<size; i++) {
			games[i].setHostScore(new Random().nextInt(15));
			games[i].setAwayScore(new Random().nextInt(15));
		}
	}
	
	private String[] getQualifiedUsers(PlayoffGame[] games) {
		String[] result = new String[games.length];
		for (int i=0, size=games.length; i<size; i++) {
			if (games[i].getHostScore() > games[i].getAwayScore()) {
				result[i] = games[i].getHostUser();
			} else if (games[i].getHostScore() < games[i].getAwayScore()) {
				result[i] = games[i].getAwayUser();
			} else {
				int draw = new Random().nextInt(2);
				if (draw == 0) {
					result[i] = games[i].getHostUser();
				} else {
					result[i] = games[i].getAwayUser();
				}
				games[i].setAet(true);
				games[i].setAetWinner(result[i]);
			}
		}
		return result;
	}
	
	public String[] concatArrays(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	
	public PlayoffGame[] getGames() {
		return games;
	}
	
	public synchronized void notifyForScores() {		
		notify();
	}
	
	public boolean isFinished() {
		synchronized(finishedLock) { 
			return finished;
		}
	}

	public static void main(String[] args) {
		
		final PlayoffManager pm =new PlayoffManager(new String[] {"john", "mike", "claudia", "maria", "gabi", "teo", "teo.jr", "horia", "daniel", "bogdand", "cosmin", "simona", "bogdan"}); 
		
		Runnable managerRunnable = new Runnable() {
			
			@Override
			public void run() {
				pm.compute(); 
			}
		};
		new Thread(managerRunnable).start();
		
		Runnable scoresRunnable = new Runnable() {
			
			@Override
			public void run() {
				if (pm.isFinished()) {
					return;
				}
				//System.out.println("\n.... setScores\n");
				pm.setScores(pm.getGames());
				pm.notifyForScores();
			}
		};
				
		while (!pm.isFinished()) {			
			try {
				Thread.currentThread().sleep(4000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			new Thread(scoresRunnable).start();
		}
	}
}
