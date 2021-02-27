package pingpong;

/**
 *Ova klasa igra ulogu aprstraktne klase u Template Method paternu
 */
abstract class PingPongThread extends Thread {
    /**
     * Broj poteza u igri.
     */
    private final int mMaxIterations;

    /**
     * String koji se stampa pri svakom potezu.
     */
    private final String stampaj;

    /**
     * Constructor initializes the various fields.
     */
    public PingPongThread(String stringStampaj,
                          int maxIterations) {
        stampaj = stringStampaj;
        mMaxIterations = maxIterations;
    }

    /**
     * Apstraktne metode koje se implementiraju u subklasama i koje odredjuju
     *  ping/pong redosled.
     */
    protected abstract void acquire();
    protected abstract void release();

    /**
     * Run metoda implementira algoritam aplikacije. PRedstavlja
     * "template method" u Template MEthod paternu
     */
    public void run() {
        for (int loopsDone = 1; loopsDone <= mMaxIterations; ++loopsDone) {
            // Stampa ping i pong u iteracijama, acquire() i
            // release() su implementirane u subklasama.
            acquire();
            PlatformStrategy.instance().print(stampaj 
                                    + "(" 
                                    + loopsDone 
                                    + ")");
            release();
        }

        // Nit je zavrsila s igranjem meca.
        PlatformStrategy.instance().done();

        
    }
}

