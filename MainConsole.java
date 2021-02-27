package pingpong;

/**
 * Klasa koja sadrzi main metodu i odakle se pokrece aplikacija
 * 
 */
public class MainConsole {
    
    public static void main(String[] args) {
        /** 
         * Inicijalizacija platforme kao singlton
         * Strategija ce uvek biti ConsolePlatform.
         */
        PlatformStrategy.instance
            (new PlatformStrategyFactory
             (System.out).makePlatformStrategy());

        // Kroz argumente komandne linije biramo sinhronizacionu opciju
        Options.instance().parseArgs(args);

        /**
         * Pravljenje pingPong objekta
         */
        PlayPingPong pingPong =
            new PlayPingPong(Options.instance().maxIterations(),
                             Options.instance().syncMechanism());

        /**
         * Startovanje niti
         */
        new Thread (pingPong).start();
    }
}
