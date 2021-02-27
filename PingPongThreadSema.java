package pingpong;

import java.util.concurrent.Semaphore;

/**
 * 
 *Ova klasa koristi JAva Semafore da implementira apstraktne klase nasledjene od PingPongThread klase:
 * acquire() i release() vrse sinhronizaciju igre. Klasa ima ulogu konkretne klase u Template Method paternu.
 */
class PingPongThreadSema extends PingPongThread {
    /**
     * Imamo 2 semafora
     */
    private final Semaphore mMine;
    private final Semaphore mOther;

    //Konstruktor
    public PingPongThreadSema(String stringToPrint, 
                              Semaphore mine,
                              Semaphore other,
                              int maxIterations) {
        super(stringToPrint, maxIterations);
        mMine = mine;
        mOther = other;
    }

    /**
     * Aquire() metoda
     */
    @Override
    protected void acquire() {
        // Potrebna je dozvola od ovog semafora, pa je blokiran dok ga ne dobije
        mMine.acquireUninterruptibly();
    }

    /**
     * Release() metoda
     */
    @Override
    protected void release() {
        // Pusti drugi semafor
        mOther.release();
    }
}
