package pingpong;

/**
 * Klasa sadrzi iplementaciju singltona kao i pokretanje aplikacije iz komandne linije
 */
public class Options {
    //Singlton Options
    private static Options mUniqueInstance = null;

    /** 
     * Maximalni broj iteracija
     */
    private int mMaxIterations = 5;

    /**
     * Koju sinhronizacionu metodu koristiti, "SEMA", "COND", "MONOBJ",
     * ili "QUEUE".  Difoltna vrednost je "SEMA".
     */
    private String mSyncMechanism = "MONOBJ";

    /** 
     * Metoda vraca jednu instancu uniqueInstance. 
     */
    public static Options instance() {
        if (mUniqueInstance == null)
            mUniqueInstance = new Options();

        return mUniqueInstance;
    }

    /** 
     * Broj iteracija 
     */
    public int maxIterations() {
        return mMaxIterations;
    }

    
    public String syncMechanism() {
        return mSyncMechanism;
    }

    /**
     * Parsiranje argumenata
     */
    public boolean parseArgs(String argv[]) {
        if (argv != null) {
            for (int argc = 0; argc < argv.length; argc += 2)
                if (argv[argc].equals("-i"))
                    mMaxIterations = Integer.parseInt(argv[argc + 1]);
                else if (argv[argc].equals("-s"))
                    mSyncMechanism = argv[argc + 1];
                else {
                    printUsage();
                    return false;
                }
            return true;
        } else
            return false;
    }

    /** 
     * Stampaj opcije na cmd-u
     */
    public void printUsage() {
        PlatformStrategy platform = PlatformStrategy.instance();
        platform.errorLog("Options",
                          "\nHelp Invoked on ");
        platform.errorLog("Options",
                          "[-his] ");
        platform.errorLog("", "");
        platform.errorLog("", "");

        platform.errorLog("Options",
                          "Usage: ");
        platform.errorLog("Options",
                          "-h: invoke help");
        platform.errorLog("Options",
                          "-i max-number-of-iterations");
        platform.errorLog("Options",
                          "-s sync-mechanism (e.g., \"SEMA\", \"COND\", \"MONOBJ\", or \"QUEUE\"");
    }

    /**
     * Privatni konstruktor za singlton
     */
    private Options() {
    }
}
