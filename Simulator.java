import java.util.Random;

public class Simulator extends Thread {
    static class Tractor {

        static int EMPTY_TANK = 0;
        static int FIELD = 1;
        static int START = 2;
        static int WORK = 3;
        static int END_OF_WORK = 4;
        static int ACCIDENT = 5;
        static int FILL = 1000;


        int number;
        int fertilizers;
        int condition;
        FIELD p;
        Random rand;

        public Tractor(int numer, int fertilizers, FIELD p) {
            this.number = numer;
            this.fertilizers = fertilizers;
            this.condition = WORK;
            this.p = p;
            rand = new Random();
        }

        public void run(){
            while(true){
                if(condition == FIELD){
                    if(rand.nextInt(2)==1){
                        condition =START;
                        fertilizers=FILL;
                        System.out.println("Siac nawozy jedzie, traktor "+ number);
                        condition =p.start(number);
                    }
                    else{
                        System.out.println("Postoje sobie jeszcze troche");
                    }
                }
                else if(condition ==START){
                    System.out.println("Wyjechal, traktor "+ number);
                    condition = WORK;
                }
                else if(condition == WORK){
                    fertilizers-=rand.nextInt(500);
                    System.out.println("Traktor "+ number +" na polu");
                    if(fertilizers<=EMPTY_TANK){
                        condition = END_OF_WORK;
                    }
                    else try{
                        sleep(rand.nextInt(1000));
                    }
                    catch (Exception e){}
                }
                else if(condition == END_OF_WORK){
                    System.out.println("Traktor wraca do domu "+ number +" ilosc nawozow w siewniku "+fertilizers);
                    condition =p.load();
                    if(condition == END_OF_WORK){
                        fertilizers-=rand.nextInt(500);
                        System.out.println("Reszta nawozow w siewniku:  "+fertilizers);
                        if(fertilizers<=0) condition = ACCIDENT;
                    }
                }
                else if(condition == ACCIDENT){
                    System.out.println("Awaria traktora "+ number);
                    p.reduce();
                }
            }
        }
    }
    static class FIELD {
        static int FIELD =1;
        static int START=2;
        static int WORK =3;
        static int END_OF_WORK =4;
        static int ACCIDENT =5;
        int numberOfSeeders;
        int numberOfOccupied;
        int numberOfTractors;
        FIELD(int numberOfSeeders,int numberOfTractors){
            this.numberOfSeeders = numberOfSeeders;
            this.numberOfTractors = numberOfTractors;
            this.numberOfOccupied = 0;
        }
        synchronized int start(int numer){
            numberOfOccupied--;
            System.out.println("Siewnik podpiety do traktora. Mozna ruszac " + numer);
            return START;
        }
        synchronized int load(){
            try{
                sleep(1000);
            }
            catch(Exception ie){
            }
            if(numberOfOccupied < numberOfSeeders){
                numberOfOccupied++;
                System.out.println("Traktor sprawny, podpiety i gotowy, mozna jechac na pole "+ numberOfOccupied);
                return FIELD;
            }
            else
            {return END_OF_WORK;}
        }
        synchronized void reduce(){
            numberOfTractors--;
            System.out.println("AWARIA");
            if(numberOfTractors == numberOfSeeders) System.out.println("Ilosc trkatorow jaka sama jak siewnikow");
        }
    }

    static class Glowna {
        static int numberOfTractors = 10;
        static int numberOfSeeders = 5;
        static FIELD field;

        public Glowna() {
        }

        public static void main(String[] args) {
            field = new FIELD(numberOfSeeders, numberOfTractors);
            for (int i = 0; i < numberOfTractors; i++)
                new Tractor(i, 2000, field).run();
        }
    }
}
