import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plane extends Thread implements Runnable{
    int id, gateNo;
    int limit = 2;
    Queue<Integer> q = null;
    
    // define semaphore
    static Semaphore runway = new Semaphore(1);
    static Semaphore accessToGate = new Semaphore(2);
    static Semaphore refuel = new Semaphore(1);

    public Plane(int id, Queue<Integer> queue) {
        this.id = id;
        this.q = queue;
    }
        
    public String currentDateTime(){
        LocalDateTime myDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDate.format(formatter);
        return formattedDate;
    }

    public void request(int id, int gateNo){
        try{
            runway.acquire();
            System.out.println(currentDateTime());
            System.out.println("ATC: Plane " + id + " is requesting to land.\n");
            Thread.sleep((long)Math.random());
            runway.release();
            accessToGate.acquire();

            if(accessToGate.availablePermits() == 0){
                System.out.println("ATC: All gates are full! Please wait for a moment...\n");
                sleep(2000);
            }

            try{
                Thread.sleep(1000);
                runway(id);
                int a = accessToGate(id, gateNo);
                gateNo = a;
            }finally{
                releaseGate(id,gateNo);
                runway(id);
                System.out.println(currentDateTime());
                System.out.println("ATC: Plane: " + id + " left!");
            }
        }catch(InterruptedException e){}
    }
 
    public void runway(int id) throws InterruptedException{
        runway.acquire();
        System.out.println("----------------------------------------------------------------------");
        System.out.println(currentDateTime());
        System.out.println("ATC: Runway is empty!!!\n");
        System.out.println(currentDateTime());
        System.out.println("ATC: Plane " + id + " has accessed to runway!\n");
        Thread.sleep(1000);
        
        System.out.println(currentDateTime());
        System.out.println("ATC: Plane " + id + " has left runway\n");
        System.out.println("----------------------------------------------------------------------");
        runway.release();
    }
    
    public synchronized int accessToGate(int id, int gateNo) throws InterruptedException{
        while(this.q.size() == limit){
            wait();
        }
       
        System.out.println(currentDateTime());
        q.add(id); 
        gateNo = q.size();
        System.out.println("Plane " + id+ " has landed at gate " + gateNo + "!\n");
        System.out.println(currentDateTime());
        Passenger.disembarkPassenger(id);
        refuel.acquire();
        Passenger.refuelAndRefillSupplies();
        refuel.release();
        System.out.println(currentDateTime());
        Passenger.receivePassenger(id);
        System.out.println(currentDateTime());
        System.out.println("Successfully refuel and refill supplies for plane " + id + "\n");
        
        if(this.q.size() == 1){
            notify();
        }
        return gateNo;
    }
    
    public synchronized void releaseGate(int id, int gateNo) throws InterruptedException{
        while(this.q.isEmpty()){
            wait();
        }
        
        System.out.println(currentDateTime());
        System.out.println("Plane " + id+ " had left at gate " + gateNo + "!\n");
        q.remove(id);
        Thread.sleep(1000);
        
        if(this.q.size() == limit){
            notify();
        }
    }

    @Override
    public void run() {
        try{
            if(id == 3){    
                System.out.println(currentDateTime() +
                        "\nEMERGENCY LANDING!!!\nPlane " + id + "'s fuel shortage!!!\n");
                try {
                    runway(id);
                    int a = accessToGate(id, gateNo);
                    gateNo = a;
                } finally{
                   releaseGate(id,gateNo);
                   System.out.println(currentDateTime());
                   System.out.println("ATC: Plane: " + id + " left!");
                }
            }else{
                request(id, gateNo);
                accessToGate.release();
            }
        }catch(InterruptedException e){}
//        if(id == 3){
//            try{
//                runway.acquire();
//                System.out.println("EMERGENCY LANDING!!!");
//                System.out.println("Plane " + id + "'s fuel shortage!!!");
//                runway.release();
//            }catch(InterruptedException e){}
//        }
        

        if(accessToGate.availablePermits() == 2){
            AirportSimulation.printStatistics();
        }
    }
}