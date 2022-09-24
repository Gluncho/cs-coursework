import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {
    private final String urlAddress;
    private final WebFrame.Launcher launcher;
    private final int index;
    private long elapsed;

    /**
     * Allocates a new {@code WebWorker} object.
     */
    public WebWorker(WebFrame.Launcher launcher, String urlAddress, int index) {
        super();
        this.launcher = launcher;
        this.urlAddress = urlAddress;
        this.index = index;
        this.elapsed = System.currentTimeMillis();
    }

    @Override
    public void run() {
        launcher.incrementRunningThreadCount(1);
        downloadURL();
        launcher.decrementThreadCount(false);
        launcher.sema.release();
    }

    //  This is the core web/download i/o code...
 	private void downloadURL() {
        InputStream input = null;
        StringBuilder contents;
        try {
            URL url = new URL(urlAddress);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            // Successful download if we get here
            elapsed = (System.currentTimeMillis() - elapsed);
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
            String status = String.format("%s %sms %d bytes", time, elapsed, contents.length());
            launcher.setStatusAt(index, status);
        }
        // Otherwise, control jumps to a catch...
        catch (IOException ignored) {
            launcher.setStatusAt(index, "err");
        } catch (InterruptedException exception) {
            launcher.setStatusAt(index, "interrupted");
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }
    }

}
