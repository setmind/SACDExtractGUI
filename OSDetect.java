public class OSDetect{
    private static String strOS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows(){
        return (strOS.indexOf("win") >= 0);
    }

    public static boolean isMac(){
        return (strOS.indexOf("mac") >= 0);
    }
}
