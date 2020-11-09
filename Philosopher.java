import java.util.Random;
import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    static class Filozof extends Thread {
        static final int MAX = 5;
        static Semaphore[] widelec = new Semaphore[MAX];
        int mojNum;

        public Filozof(int nr) {
            mojNum = nr;
        }

        public void run() {
            while (true) {
			// myslenie
                System.out.println("Mysle ¦ " + mojNum);
                try {
                    Thread.sleep((long) (7000 * Math.random()));
                } catch (InterruptedException e) {
                }
                widelec[mojNum].acquireUninterruptibly(); //przechwycenie L widelca
                widelec[(mojNum + 1) % MAX].acquireUninterruptibly(); //przechwycenie P widelca
			// jedzenie
                System.out.println("Zaczyna jesc " + mojNum);
                try {
                    Thread.sleep((long) (5000 * Math.random()));
                } catch (InterruptedException e) {
                }
                System.out.println("Konczy jesc " + mojNum);
                widelec[mojNum].release(); //zwolnienie L widelca
                widelec[(mojNum + 1) % MAX].release(); //zwolnienie P widelca
            }
        }
    }

    static class Filozof_2 extends Thread {
        static final int MAX = 5;
        static Semaphore[] widelec = new Semaphore[MAX];
        int mojNum;

        public Filozof_2(int nr) {
            mojNum = nr;
        }

        public void run() {
            while (true) {
				// myslenie
                System.out.println("Mysle ¦ " + mojNum);
                try {
                    Thread.sleep((long) (5000 * Math.random()));
                } catch (InterruptedException e) {
                }
                if (mojNum == 0) {
                    widelec[(mojNum + 1) % MAX].acquireUninterruptibly();
                    widelec[mojNum].acquireUninterruptibly();
                } else {
                    widelec[mojNum].acquireUninterruptibly();
                    widelec[(mojNum + 1) % MAX].acquireUninterruptibly();
                }
				// jedzenie
                System.out.println("Zaczyna jesc " + mojNum);
                try {
                    Thread.sleep((long) (3000 * Math.random()));
                } catch (InterruptedException e) {
                }
                System.out.println("Konczy jesc " + mojNum);
                widelec[mojNum].release();
                widelec[(mojNum + 1) % MAX].release();
            }
        }
    }

    static class Filozof_3 extends Thread {
        static final int MAX = 5;
        static Semaphore[] widelec = new Semaphore[MAX];
        int mojNum;
        Random losuj;

        public Filozof_3(int nr) {
            mojNum = nr;
            losuj = new Random(mojNum);
        }

        public void run() {
            while (true) {
				// myslenie
                System.out.println("Mysle ¦ " + mojNum);
                try {
                    Thread.sleep((long) (5000 * Math.random()));
                } catch (InterruptedException e) {
                }
                int strona = losuj.nextInt(2);
                boolean podnioslDwaWidelce = false;
                do {
                    if (strona == 0) {
                        widelec[mojNum].acquireUninterruptibly();
                        if (!(widelec[(mojNum + 1) % MAX].tryAcquire())) {
                            widelec[mojNum].release();
                        } else {
                            podnioslDwaWidelce = true;
                        }
                    } else {
                        widelec[(mojNum + 1) % MAX].acquireUninterruptibly();
                        if (!(widelec[mojNum].tryAcquire())) {
                            widelec[(mojNum + 1) % MAX].release();
                        } else {
                            podnioslDwaWidelce = true;
                        }
                    }
                } while (podnioslDwaWidelce == false);
                System.out.println("Zaczyna jesc " + mojNum);
                try {
                    Thread.sleep((long) (3000 * Math.random()));
                } catch (InterruptedException e) {
                }
                System.out.println("Konczy jesc " + mojNum);
                widelec[mojNum].release();
                widelec[(mojNum + 1) % MAX].release();
            }
        }
    }

    public static void main ( String [] args ) {

        // 1
        for (int i = 0; i < Filozof.MAX; i++) {
            Filozof.widelec[i] = new Semaphore(1);
        }
        for (int i = 0; i < Filozof.MAX; i++) {
            new Filozof(i).start();
        }
        // 2
        for (int i = 0; i < Filozof_2.MAX; i++) {
            Filozof_2.widelec[i] = new Semaphore(1);
        }
        for (int i = 0; i < Filozof_2.MAX; i++) {
            new Filozof(i).start();
        }

        // 3
        for ( int i =0; i<Filozof_3.MAX; i++) {
            Filozof_3.widelec [ i ]=new Semaphore( 1 ) ;
        }
        for ( int i =0; i<Filozof_3.MAX; i++) {
            new Filozof(i).start();
        }

    }
}
