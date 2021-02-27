package pingpong;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 *
 * Ova klasa implementira program koji simuliranje igranje stonog tenisa
 * tako sto se na konzoli naizmenicno stampaju Ping i Pong poruke u zavisnosti
 * na kog je igraca red da igra.
 * Koristi Template Method, Strategy, i Factory Method paterne objasnjene u dokumentaciji.
 *        
 */
public class PlayPingPong implements Runnable {
    /**
     * Maksimalan broj poteza u igri.
     */
    private final int mMaxIterations;

    /**
     * Biramo izmedju nacina sinhronizacije: "SEMA", "COND", "MONOBJ",
     * "QUEUE".
     */
    private final String mSyncMechanism;

    /**
     * Broj igraca je 2, zato imamo najvise dve niti.
     */
    private final static int MAX_PING_PONG_THREADS = 2;

    /**
     * Ping i Pong igrac.
     */
    private final static int PING_THREAD = 0;
    private final static int PONG_THREAD = 1;

    /**
     * Konstruktor klase
     */
    public PlayPingPong(int maxIterations,
                        String syncMechanism) {
        // Postavljamo broj iteracija.
        mMaxIterations = maxIterations;

        // Postavljamo mehanizam sinhronizacije.
        mSyncMechanism = syncMechanism;
    }

    /**
     * Run metoda koja se poziva iz main metode
     */
    public void run() {
        //Singlton patern - instanca igre
        PlatformStrategy.instance().begin();

        // Stampa string za pocetak igre 
        PlatformStrategy.instance().print("Tri...Cetiri...Sad!");

        // Pravi ping i pong niti 
        PingPongThread pingPongThreads[] =
            new PingPongThread[MAX_PING_PONG_THREADS];

        // Pravi niti prilagodjenje mehanizmu sinhronizacije
        makePingPongThreads(mSyncMechanism,
                            pingPongThreads);

        // Startovanje niti poziva run() metodu
        for (PingPongThread thread : pingPongThreads)
            thread.start();

        // Cekamo da se zavrsi pre napustanja play() metode.
        PlatformStrategy.instance().awaitDone();

        // Gotovo
        PlatformStrategy.instance().print("Gotovo!");
    }

    /**
     * Factory method dizajnira instance PingPongThread klase u zavisnosti 
     * koji mehanizam sinhronizacije koristimo
     */
    private void makePingPongThreads(String syncMechanism,
                                     PingPongThread[] pingPongThreads) {
        if (syncMechanism.equals("SEMA")) {
            // Pravi Java semafore
            // "ping" and "pong" in the correct alternating order.
            Semaphore pingSema =
                new Semaphore(1); // Ping semafor je otkljucan.
            Semaphore pongSema =
                new Semaphore(0); //Pong semafor je zakljucan.

            pingPongThreads[PING_THREAD] = 
                new PingPongThreadSema("ping",
                                       pingSema,
                                       pongSema,
                                       mMaxIterations);
            pingPongThreads[PONG_THREAD] =
                new PingPongThreadSema("pong",
                                       pongSema,	//Kako bismo omogucili sinhronizaciju
                                       pingSema,
                                       mMaxIterations);
        } else if (syncMechanism.equals("MONOBJ")) {
            // Pravi Binary Semafore koristeci monitor objekte, koji omogucavaju sinhronizaciju igraca
            PingPongThreadMonObj.BinarySemaphore pingSema =
                new PingPongThreadMonObj.BinarySemaphore(true); // Otkljucan
            PingPongThreadMonObj.BinarySemaphore pongSema =
                new PingPongThreadMonObj.BinarySemaphore(false);	//Zakljucan

            pingPongThreads[PING_THREAD] =
                new PingPongThreadMonObj("ping",
                                         pingSema,
                                         pongSema,
                                         mMaxIterations);
            pingPongThreads[PONG_THREAD] =
                new PingPongThreadMonObj("pong",
                                         pongSema,
                                         pingSema,
                                         mMaxIterations);
        } else if (syncMechanism.equals("COND")) {
            // Pravi ReentrantLock i uslove koji omogucavaju sinhronizaciju.
            ReentrantLock lock = new ReentrantLock();
            Condition pingCond = lock.newCondition();
            Condition pongCond = lock.newCondition();

            PingPongThreadCond pingThread =
                new PingPongThreadCond("ping",
                                       lock,
                                       pingCond,
                                       pongCond,
                                       true,
                                       mMaxIterations);
            PingPongThreadCond pongThread = 
                new PingPongThreadCond("pong",
                                       lock,
                                       pongCond,
                                       pingCond,
                                       false,
                                       mMaxIterations);
            // Niti razmenjuju id-jeve
            pingThread.setOtherThreadId(pongThread.getId());
            pongThread.setOtherThreadId(pingThread.getId());

            pingPongThreads[PING_THREAD] = pingThread;
            pingPongThreads[PONG_THREAD] = pongThread;

        } else if (syncMechanism.equals("QUEUE")) {
            // PRavi LinkedBlockingQueues koji omogucava sinhronizaciju igraca.
            LinkedBlockingQueue<Object> pingQueue =
                new LinkedBlockingQueue<Object>();
            LinkedBlockingQueue<Object> pongQueue =
                new LinkedBlockingQueue<Object>();
            Object pingPongBall = new Object(); //pravimo pinPongBall objekat

            try {
                // Stavljamo pingPongBall objekat u red
                pongQueue.put(pingPongBall);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            pingPongThreads[PING_THREAD] =
                new PingPongThreadBlockingQueue("ping",
                                                pingQueue,
                                                pongQueue,
                                                mMaxIterations);
            pingPongThreads[PONG_THREAD] = 
                new PingPongThreadBlockingQueue("pong",
                                                pongQueue,
                                                pingQueue,
                                                mMaxIterations);
        }
    }
}
