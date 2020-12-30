public class SynServices {
    private static boolean processing=false;
    public static synchronized boolean getstatus()
    {
        return processing;
    }
    public static synchronized void flag()
    {
        processing=true;
    }
    public static synchronized void release()
    {
        processing=false;
    }
}
