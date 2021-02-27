package pingpong;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ova klasa koristi Java Conditions i Condition objekte da implementira apstraktne metode
 * acquire() i release() koje vrse sinhronizaciju igre.
 * Igra ulogu konkretne klase u Template Method paternu.
 */
class PingPongThreadCond extends PingPongThread {
    /**
     * Imamo dva uslova
     */
    private final Condition mMine;
    private final Condition mOther;

    /**
     * Monitor lock
     */
    private final ReentrantLock mLock;

    /**
     * Id other niti
     */
    public long mOtherThreadId = 0;

    /**
     * Niti ciji je trenutno red za izvrsavanje
     */
    private static long mTurnOwner;

    //Konstruktror 
    public PingPongThreadCond(String stringToPrint,
                              ReentrantLock lock,
                              Condition mine,
                              Condition other,
                              boolean isOwner,
                              int maxIterations) {
        super(stringToPrint, maxIterations);
        mLock = lock;
        mMine = mine;
        mOther = other;
        if (isOwner)
            mTurnOwner = this.getId();
    }

    /**
     * Aquire metoda
     */
    @Override
    protected void acquire() {
        mLock.lock();

        try {
            //Proverava da li je na njega red
            while (mTurnOwner != this.getId()) 
                mMine.awaitUninterruptibly();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Release metoda
     */
    @Override
    protected void release() {
        mLock.lock();
        
        try {
            // Prenosi na drugu nit red zamenom id-a
            mTurnOwner = mOtherThreadId;
            mOther.signal();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * Prati id niti otherThread
     */
    public void setOtherThreadId(long otherThreadId) {
        mOtherThreadId = otherThreadId;
    }
}

