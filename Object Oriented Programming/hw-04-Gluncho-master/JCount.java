// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private static final int PART = 10000;

	public class Worker extends Thread{
		int count;
		int limit;
		public Worker(int limit) {
			count = 0;
			this.limit = limit;
		}

		@Override
		public void run() {
			while(count < limit){
				if(isInterrupted()) break;
				if(count % PART == 0){
					SwingUtilities.invokeLater(() -> label.setText(String.valueOf(count)));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						break;
					}
				}
				count++;
			}
		}
	}
	private JTextField field;
	private JLabel label;
	private JButton startButton, stopButton;
	private Worker worker;
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		field = new JTextField(15);
		label = new JLabel("0");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");

		addStartListener();
		addStopListener();

		add(field);
		add(label);
		add(startButton);
		add(stopButton);

		add(Box.createRigidArea(new Dimension(0,40)));
	}

	private void addStartListener() {
		startButton.addActionListener(e -> {
			try{
				int limit = Integer.parseInt(field.getText());
				if(worker != null && worker.isAlive()) worker.interrupt();
				worker = new Worker(limit);
				worker.start();
			}catch(NumberFormatException ex) {
				ex.printStackTrace();
			}
		});
	}

	private void addStopListener() {
		stopButton.addActionListener(e -> {
			if(worker != null && worker.isAlive()){
				worker.interrupt();
			}
		});
	}

	private static void createAndShowGUI() {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(Box.createRigidArea(new Dimension(0, 20)));
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.setSize(new Dimension(200,600));
		//frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	static public void main(String[] args)  {
		SwingUtilities.invokeLater(JCount::createAndShowGUI);
	}


}

