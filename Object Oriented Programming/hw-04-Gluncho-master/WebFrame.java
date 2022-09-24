import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebFrame extends JFrame {
    public static final String FILENAME = "links2.txt";
    private static final String[] COLUMNS = new String[]{"URL", "Status"};
    private List<String> urls;
    private DefaultTableModel model;
    private JProgressBar progressBar;
    private JPanel panel;
    private JButton singleButton, concurrentButton, stopButton;
    private JTextField field;
    private JLabel runningLabel, completedLabel, elapsedLabel;
    private final Lock lock = new ReentrantLock();
    private Launcher launcher;

    /**
     * Called by the constructors to init the <code>JFrame</code> properly.
     */
    @Override
    protected void frameInit() {
        super.frameInit();
        panel = new JPanel();
        urls = new ArrayList<>();
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        model = new DefaultTableModel(COLUMNS, 0){
          @Override
          public boolean isCellEditable(int row, int column) {
              return false;
          } // make all cells uneditable
        };

        singleButton = new JButton("Single Thread Fetch");
        concurrentButton = new JButton("Concurrent Fetch");
        stopButton = new JButton("Stop");

        field = new JTextField(15);
        field.setMaximumSize(new Dimension(50, field.getHeight()));

        runningLabel = new JLabel("Running: 0");
        completedLabel = new JLabel("Completed: 0");
        elapsedLabel = new JLabel("Elapsed:");
    }

    public WebFrame(String filename) {
        super("WebLoader");
        readFile(filename);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        initTable();
        addSouthSide();
        addListeners();
        add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void readFile(String filename) {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(filename));
            while (true) {
                String url = rd.readLine();
                if (url == null) break;
                urls.add(url);
            }
            rd.close();
            progressBar.setMaximum(urls.size() - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTable() {
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600, 300));
        for (String url : urls) {
            model.addRow(new String[]{url, ""});
        }
        panel.add(scrollpane);
    }

    private void addSouthSide() {
        panel.add(singleButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(concurrentButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(runningLabel);
        panel.add(completedLabel);
        panel.add(elapsedLabel);
        panel.add(progressBar);
        panel.add(stopButton);
        stopButton.setEnabled(false);
    }

    private void addListeners() {
        singleButton.addActionListener(e -> startFetching(true));
        concurrentButton.addActionListener(e -> startFetching(false));
        stopButton.addActionListener(e -> stopFetching());
    }

    private void startFetching(boolean isSingleThread) {
        if(!isSingleThread && field.getText().equals((""))) return;
        try {
            int numThreads = 1;
            if(!isSingleThread) numThreads = Integer.parseInt(field.getText());
            SwingUtilities.invokeLater(this::resetGUI);
            launcher = new Launcher(numThreads);
            launcher.start();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void stopFetching() {
        lock.lock();
        launcher.interrupt();
        lock.unlock();
        runningLabel.setText("Running: 0");
        singleButton.setEnabled(true);
        concurrentButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    // Cleans the table, and sets the frame in a ready state
    private void resetGUI() {
        singleButton.setEnabled(false);
        concurrentButton.setEnabled(false);
        stopButton.setEnabled(true);

        for(int i = 0; i < urls.size(); i++){
            model.setValueAt("", i, 1);
        }

        runningLabel.setText("Running: 0");
        completedLabel.setText("Completed: 0");
        elapsedLabel.setText("Elapsed: 0.0");
        progressBar.setMaximum(urls.size());
        progressBar.setValue(0);
    }


    public class Launcher extends Thread {
        public Semaphore sema;
        private int runningCount, completedCount;
        private double elapsed;
        private final List<Thread> workers;

        /**
         * Allocates a new {@code Launcher} object.
         */
        public Launcher(int limit) {
            elapsed = (double) System.currentTimeMillis();
            workers = new ArrayList<>();
            sema = new Semaphore(limit);
            incrementRunningThreadCount(1);
        }

        @Override
        public void run() {
            for(int i = 0; i < urls.size(); i++){
                String url = urls.get(i);
                try {
                    sema.acquire();
                    lock.lock();
                    Thread worker = new WebWorker(this, url, i);
                    workers.add(worker);
                    worker.start();
                    lock.unlock();
                } catch (InterruptedException e) {
                    interruptWorkers();
                    decrementThreadCount(true);
                    return;
                }
            }
            joinThreads();
            decrementThreadCount(true);
        }

        private void joinThreads() {
            for(Thread thread : workers){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    interruptWorkers();
                }
            }
        }

        public void incrementRunningThreadCount(int num){
            synchronized(runningLabel){
                runningCount += num;
                SwingUtilities.invokeLater(() -> runningLabel.setText("Running: " + (runningCount)));
            }
        }

        public void decrementThreadCount(boolean isLauncher){
            incrementRunningThreadCount(-1);
            synchronized(completedLabel){
                completedLabel.setText("Completed: " + (++completedCount));
                progressBar.setValue(progressBar.getValue() + 1);
                if(isLauncher){
                    elapsed = (System.currentTimeMillis() - elapsed) / 1000;
                    elapsedLabel.setText("Elapsed: "+elapsed);
                    stopFetching();
                }
            }
        }

        public void setStatusAt(int index, String status) {
            model.setValueAt(status, index, 1);
        }

        private void interruptWorkers() {
            for(Thread worker : workers) worker.interrupt();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebFrame(FILENAME));
    }
}
