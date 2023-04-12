public class Passenger extends Thread {
    static int disembarkPassengers = 0;
    static int receivePassengers = 0;
    static int numPlanesServed;
    static int maxWaitingTime;
    static int avgWaitingTime;
    static int minWaitingTime;
    
    static void disembarkPassenger(int id)throws InterruptedException{
        int numPassengers = (int)(Math.random()*(50-10+1)+10);
        for(int i = 1; i < numPassengers-1; i++){
            disembarkPassengers++;
            System.out.println("Passenger " + i + " is disembarking from plane " + id);
            Thread.sleep(100);
        }
        System.out.println("\n");
    }
    
    static void refuelAndRefillSupplies() throws InterruptedException{
        System.out.println("Successfully refuel and refill supplies");
    }
    
    static void receivePassenger(int id) throws InterruptedException{
        int numPassengers = (int)(Math.random()*(50-10+1));
        for(int i = 1; i < numPassengers-1; i++){
            receivePassengers++;
            System.out.println("Passenger " + i + " is entering to plane " + id);
            Thread.sleep(100);
        }
        System.out.println("\n");
    }
}