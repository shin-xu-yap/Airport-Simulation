import static java.lang.Thread.sleep;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class AirportSimulation{
    public static int numPlanesServed;
    public static int numPassengersBoarded;
    public static int maxWaitingTime;
    public static int avgWaitingTime;
    public static int minWaitingTime;
    
    public static class Constants {
        public static final int NUM_PLANES = 6;
        public static final int NUM_GATES = 3;
        public static final int NUM_REFUEL_TRUCKS = 1;
    }
    
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> q = new LinkedBlockingDeque<>();
//        PriorityQueue<Integer> q = new PriorityQueue<>();

        Plane p1 = new Plane(1,q);
        Plane p2 = new Plane(2,q);
        Plane p3 = new Plane(3,q);
        Plane p4 = new Plane(4,q);
        Plane p5 = new Plane(5,q);
        Plane p6 = new Plane(6,q);
        
        p1.setPriority(1);
        p2.setPriority(1);
        p3.setPriority(10);
        p4.setPriority(1);
        p5.setPriority(1);
        p6.setPriority(1);

        p1.start();p2.start();
        sleep(1000);
        p3.start();p4.start();p5.start();p6.start();
        //p1.start();p1.join();p2.start();p2.join();sleep(1000);p3.start();p3.join();p4.start();p4.join();p5.start();p5.join();p6.start();p6.join();
        p1.join();p2.join();p3.join();p4.join();p5.join();p6.join();
    };
    
    public static void printStatistics(){
        System.out.println("\nNumber of planes served: ");
        System.out.println("Number of passengers boarded: " + (Passenger.receivePassengers + Passenger.disembarkPassengers));
        System.out.println("Maximum waiting time: ");
        System.out.println("Average waiting time: ");
        System.out.println("Minimum waiting time: ");
    };
}