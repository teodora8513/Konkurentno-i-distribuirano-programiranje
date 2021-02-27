package pingpong;

import java.util.concurrent.CountDownLatch;
import java.io.PrintStream;

/**
 * 
 * Ima ulogu konkretne strategiju u Strategy paternu. Klasa implementira platformski nezavisni API
 * koji salje podatke konzoli, kao i vrsi sinhroniziju niti.
 * Prosiruje klasu PlatformStrategy 
 */
public class PlatformStrategyConsole extends PlatformStrategy {
    /**
     * Vrsi dekrementaciju svaki put kada nit napusti program
     */
    private CountDownLatch mExitBarrier = null;

    /**
     * Prikazuje sta ce se ispisati na konzoli
     */
    private final PrintStream mOutput;

    /** 
     * Konstruktor
     */
    public PlatformStrategyConsole(Object output) {
        mOutput = (PrintStream) output;
    }
	
    /** 
     * Postavlja broj niti
     */
    public void begin() {
        mExitBarrier = new CountDownLatch(NUMBER_OF_THREADS);
    }

    /** 
     * Stampa outputString na konzoli 
     */
    public void print(String outputString) {
       
        mOutput.println(outputString);
    }

    /** 
     * Nit je zavrsila s izvrsavanjem
     */
    public void done() {
        // Vrsi dekrementaciju za jedan
        mExitBarrier.countDown();
    }
    
    /** 
     * Barrier that waits for all the Threads to finish.
     */
    public void awaitDone() {
        try {
            // Sacekaj da CountDownLatch spadne na nulu
            mExitBarrier.await();
        } catch(java.lang.InterruptedException e) {
            errorLog("PlatformStrategyConsole",
                     e.getMessage());
        }
    }

    /**
     * Error poruka
     */
    public void errorLog(String javaFile, String errorMessage) {
        mOutput.println(javaFile 
                        + " " 
                        + errorMessage);
    }
}

