package pingpong;

/** 
 *Obezbedjuje platformsku nezavisnost. Klasa koristi singlton patern. 
 *Glumi ulogu strategije u Strategy paternu. Dve klase prosiruju ovu klasu, 
 *ali samo jedna ce biti implementirana.
 */
public abstract class PlatformStrategy
{
    /** 
     * Broj igraca - niti
     */
    public static final int NUMBER_OF_THREADS = 2;

    /** 
     * Singlton patern 
     */
    private static PlatformStrategy sUniqueInstance = null;

    /** 
     * Vracanje jedne instance
     */
    public static PlatformStrategy instance() {
        return sUniqueInstance;
    }

    
    public static PlatformStrategy instance(PlatformStrategy platform) {
        return sUniqueInstance = platform;
    }

    /** 
     * Inicijalizacija i pokretanje ping pong algoritma
     */
    public abstract void begin();

    /** 
     * String koji se stampa
     */
    public abstract void print(String outputString);

    /** 
     * Obavestava kada je nit zavrsila sa igrom
     */
    public abstract void done();

    /** 
     * Cekamo sve niti da zavrse igru tj prolazak kroz algoritam aplikacije
     */
    public abstract void awaitDone();

    /**
     * Klasican Log format poruke
     */
    public abstract void errorLog(String javaFile,
                                  String errorMessage);

    /**
     * Protected konstruktor zbog singlton paterna
     */
    protected PlatformStrategy() {}
}
