package new1_7;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
/**
 * The Phaser class is another mechanism provided by the Java language that
 * controls the execution of concurrent tasks divided in phases. All the threads must
 * finish one phase before they can continue with the next one.
 * 
 * @author Mihai Dinca-Panaitescu
 * @date 20.02.2013
 */
public class PhaserExample {

	public static void main(String[] args) {

		MyPhaser phaser = new MyPhaser();

		Student students[] = new Student[5];
		for (int i = 0; i < students.length; i++) {
			students[i] = new Student(phaser);
			phaser.register();
		}

		Thread threads[] = new Thread[students.length];
		for (int i = 0; i < students.length; i++) {
			threads[i] = new Thread(students[i], "Student " + i);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.printf("Main: The phaser has finished: %s.\n",phaser.isTerminated());
	}

}

class MyPhaser extends Phaser {

	@Override
	protected boolean onAdvance(int phase, int registeredParties) {
		switch (phase) {
		case 0:
			return studentsArrived();
		case 1:
			return finishFirstExercise();
		case 2:
			return finishSecondExercise();
		case 3:
			return finishExam();
		default:
			return true;
		}
	}

	private boolean studentsArrived() {
		System.out.printf("Phaser: The exam are going to start. The students are ready.\n");
		System.out.printf("Phaser: We have %d students.\n", getRegisteredParties());
		return false;
	}

	private boolean finishFirstExercise() {
		System.out.printf("Phaser: All the students have finished the first exercise.\n");
		System.out.printf("Phaser: It's time for the second one.\n");
		return false;
	}

	private boolean finishSecondExercise() {
		System.out.printf("Phaser: All the students have finished the second exercise.\n");
		System.out.printf("Phaser: It's time for the third one.\n");
		return false;
	}

	private boolean finishExam() {
		System.out.printf("Phaser: All the students have finished the exam.\n");
		System.out.printf("Phaser: Thank you for your time.\n");
		return true;
	}
}

class Student implements Runnable {

	private Phaser phaser;

	public Student(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		System.out.printf("%s: Has arrived to do the exam. %s\n", Thread.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the first exercise. %s\n", Thread.currentThread().getName(), new Date());
		doExercise1();
		System.out.printf("%s: Has done the first exercise. %s\n", Thread.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the second exercise. %s\n", Thread.currentThread().getName(), new Date());
		doExercise2();
		System.out.printf("%s: Has done the second exercise. %s\n", Thread.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the third exercise. %s\n", Thread.currentThread().getName(), new Date());
		doExercise3();
		System.out.printf("%s: Has finished the exam. %s\n", Thread.currentThread().getName(), new Date());
		phaser.arriveAndAwaitAdvance();
	}

	private void doExercise1() {
		try {
			long duration = (long) (Math.random() * 10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doExercise2() {
		try {
			long duration = (long) (Math.random() * 15);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doExercise3() {
		try {
			long duration = (long) (Math.random() * 20);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
