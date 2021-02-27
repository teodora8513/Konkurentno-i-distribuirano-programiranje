package pingpong;

/**
 *
 * Ova klasa koristi binarne semafore da bi implementirala apstraktne metode acquire() i
 * release() koje sluze za sinhronizaciju igre.
 * Ova klasa ima ulogu konkretne klase u Template Method paternu.
 */
class PingPongThreadMonObj extends PingPongThread {
    /**
     * Pravimo klasu binarnog semafora
     */
    static public class BinarySemaphore {
        /**
         * Belezi da li je semafor zakljucan ili otkljucan.
         */
        private boolean mUnLocked;
    
        /**
         * Konstruktor
         */
        public BinarySemaphore(boolean unlocked) {
            mUnLocked = unlocked;
        }

        /**
         * Acquire metoda
         */
        public void acquire() {
            synchronized(this) {
                while (!mUnLocked)
                    try {
                        //Cekaj dok notify() ne kaze da je mUnlock == true
                        wait();
                    } catch (InterruptedException e) {
                        //Nista
                    }
                mUnLocked = false;//Postavljamo na zakljucan
            }
        }

        /**
         * Pustanje binarnog semafora
         */
        public void release() {
            synchronized(this) {
                // Pusti semafor i notify() nit koju ceka
                mUnLocked = true;
                notify();
            }
        }
    }

    /**
     * 2 binarna semafora
     */
    private final BinarySemaphore mMine;
    private final BinarySemaphore mOther;

    public PingPongThreadMonObj(String stringToPrint,
                                BinarySemaphore mine,
                                BinarySemaphore other,
                                int maxIterations) {
        super(stringToPrint, maxIterations);
        mMine = mine; 
        mOther = other; 
    }

    
    @Override
    protected void acquire() {
        mMine.acquire();
    }

   
    @Override
    protected void release() {
        // Pusti drugi semafor

        mOther.release();
    }
}
