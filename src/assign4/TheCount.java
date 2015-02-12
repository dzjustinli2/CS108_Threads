package assign4;

import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("serial")
public class TheCount extends JPanel{
	
	private static JFrame jf;
	private JTextField goalTextField;
	private JLabel currentStanding;
	private JButton start;
	private JButton stop;
	private int goal;
	private int current;
	private WorkerThread wt;

	public TheCount() {
		goal = 100000000;
		current = 0;
		wt = new WorkerThread();
	}
	
	public void createAndShowGui(){
		goalTextField = new JTextField(goal + "");
		currentStanding = new JLabel(current + "");
		start = new JButton("Start");
		stop = new JButton("Stop");
		add(goalTextField);
		add(currentStanding);
		add(start);
		add(stop);
		add(Box.createRigidArea(new Dimension(0,40)));
		jf.add(this);
	}
	
	public void start(){
		wt.start();
	}
	
	public class WorkerThread extends Thread{
		public void run(){
			
		}
	}
	
	private void addListeners(){
		goalTextField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = goalTextField.getText();
				goal = Integer.parseInt(text);
			}
		});
		goalTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = goalTextField.getText();
				goal = Integer.parseInt(text);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = goalTextField.getText();
				goal = Integer.parseInt(text);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				String text = goalTextField.getText();
				goal = Integer.parseInt(text);
			}
		});
		start.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Thread started");
				if(wt.isAlive()){
					wt.interrupt();
					wt = null;
				}
				current = 0;
				currentStanding.setText(current + "");
				wt = new WorkerThread();
				wt.start();
			}
		});
		stop.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Thread stopped");
				if(wt.isAlive()){
					wt.interrupt();
					wt = null;
				}
			}
		});
	}

	public static void main(String[] args) {
		jf = new JFrame();
		jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
		jf.setPreferredSize(new Dimension(200,500));
		jf.setLocationByPlatform(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		for(int i = 0; i < 4; i++){
			TheCount tc = new TheCount();
			tc.setLayout(new BoxLayout(tc, BoxLayout.Y_AXIS));
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					tc.createAndShowGui();
					tc.addListeners();
					tc.start();
				}
			});
		}
		jf.setVisible(true);
	}

}
