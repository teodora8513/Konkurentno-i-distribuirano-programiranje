package pingpong;

import java.util.HashMap;

/**
 * Klasa igra ulogu kreatora u Factory paternu.
 */
public class PlatformStrategyFactory {
    /**
     * Preko enumeracije program bira koju ce platformu da koristi
     */
    public enum PlatformType {
        ANDROID,
        PLAIN_JAVA
    };
    
    
    private final PlatformType mPlatformType =
        System.getProperty("java.specification.vendor").indexOf("Android") >= 0
            ? PlatformType.ANDROID
            : PlatformType.PLAIN_JAVA;

    /**
     * Interfejs koristi za kreiranje PlatformStrategy u runtime-u
     * 
     */
    private interface IPlatformStrategyFactoryCommand {
        public PlatformStrategy execute();
    }

    /**
     * HashMapa koja povezuje tip platforme s komandom execute() 
     * koja prati odgovarajuci tip PlatformStratefy-ja
     * 
     */
    private HashMap<PlatformType, IPlatformStrategyFactoryCommand> mPlatformStrategyMap =
        new HashMap<PlatformType, IPlatformStrategyFactoryCommand>();

    /**
     * Constructor stores the objects that perform output and
     * synchronization for a particular Java platform, such as
     * PlatformStrategyConsole or PlatformStrategyAndroid.
     */
    public PlatformStrategyFactory(final Object output) {
        if (mPlatformType == PlatformType.ANDROID)
            /**
             * Komanda za pravljenje ANDROID objekta
             */
            mPlatformStrategyMap.put(PlatformType.ANDROID,
                                     new IPlatformStrategyFactoryCommand() {
                                         // Creates the PlatformStrategyAndroid.
                                         public PlatformStrategy execute() {
                                             return null;
                                         }
                                     });
        else if (mPlatformType == PlatformType.PLAIN_JAVA)
                 /**
                  * Komanda za pravljenje StrategyConsole tj za JAVA platformu
                  */
                 mPlatformStrategyMap.put(PlatformType.PLAIN_JAVA,
                                          new IPlatformStrategyFactoryCommand() {
                                              // Creates the PlatformStrategyConsole.
                                              public PlatformStrategy execute() {
                                                  return new PlatformStrategyConsole(output);
                                              }
                                          });
    }

    /**
     * Metoda koja pravi i vraca novi PlatformStrategy objekat
     *
     */
    public PlatformStrategy makePlatformStrategy() {
        return mPlatformStrategyMap.get(mPlatformType).execute();
    }
}
