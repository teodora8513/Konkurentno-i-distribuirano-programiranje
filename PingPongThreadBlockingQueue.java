package pingpong;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Ova klasa koristi Blocking Queues za implementiranje apstraktnih metoda acquire()
 * i release() koje vrse sinhronizaciju igre. Igra ulogu konkretne klase u Template Method paternu.
 * 
 */
public class PingPongThreadBlockingQueue extends PingPongThread {
    /*
     * Ovi redovi vrse sinhronizaciju izmedju 2 niti. 
     * Blokirajuce svojstvo reda vidimo pri pozivu metode take() nad praznim redom (other) kako bismo
     * izvrsili simulaciju cekanja
     * 
     * Kako bismo poslali obavestenje onoj niti koja ceka, tj trenutnoj niti, koristimo metodu put()
     *  
     */
    private final LinkedBlockingQueue<Object> mMine;
    private final LinkedBlockingQueue<Object> mOther;

    /**
     * Koja nit poseduje loptu, ona je kontrolna tj glavna nit. Ovim izbegavamo
     * dinamicko alociranje memorije svaki put kada kontrolu prebacimo s jedne niti na drugu.
     */
    private Object mPingPongBall = null;

    /**
     * Konstruktor
     */
    public PingPongThreadBlockingQueue(String stringToPrint,
                                       LinkedBlockingQueue<Object> mine,
                                       LinkedBlockingQueue<Object> other,
                                       int maxIterations) {
        super(stringToPrint, maxIterations);
        mMine = mine;
        mOther = other;
    }

    /**
     * Aquire metoda - poziva take() metodu nad other redom
     */
    @Override
    protected void acquire() {
        try {
            // Blokiraj dok dok ne uzmes loptu od other niti
            // BlockingQueue
            mPingPongBall = mOther.take();
        } catch (InterruptedException e) {
            // Nista
        }
    }

    //Release metoda
    @Override
    protected void release() {
        try {
            // Vrati loptu nazad u  drugi BlockigQueue kako bi je probudio i pokrenuo run metodu
            mMine.put(mPingPongBall);
        } catch (InterruptedException e) {
            // Nista
        }
    }
}
