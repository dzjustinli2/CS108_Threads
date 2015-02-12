package assign4;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

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
	private int defaultGoal = 100000000;
	String name = "";

	public TheCount(int name) {
		goal = defaultGoal;
		current = 0;
		this.name = "Thread:" + name;
		wt = new WorkerThread(this.name);
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
		public WorkerThread(String name){
			super(name);
		}
		
		public void run(){
			try{
				while(current <= goal){
					if(current%10000 == 0){
						//Need to do this so it shows only the increments
						int value = current;
						//Need to sleep and update the jlabel
						sleep(100);
						SwingUtilities.invokeLater(new Runnable(){
							public void run(){
								currentStanding.setText(value + "");
							}
						});
					}
					current = current + 1;
				}
			}catch(Exception e){
				System.out.println(this.getName() + " interrupted");
			}
		}
	}
	
	private void addListeners(){
		goalTextField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGoal();
			}
		});
		goalTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateGoal();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateGoal();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateGoal();
			}
		});
		start.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Thread started");
				if(wt != null && wt.isAlive()){
					wt.interrupt();
					wt = null;
				}
				current = 0;
				currentStanding.setText(current + "");
				wt = new WorkerThread(name);
				wt.start();
			}
		});
		stop.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Thread stopped");
				if(wt != null && wt.isAlive()){
					wt.interrupt();
					wt = null;
				}
			}
		});
	}
	
	private void updateGoal(){
		String text = goalTextField.getText();
		try{
			goal = Integer.parseInt(text);
		}catch(Exception e){
			goal = defaultGoal;
		}
	}

	public static void main(String[] args) {
		jf = new JFrame();
		jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
		jf.setPreferredSize(new Dimension(200,500));
		jf.setLocationByPlatform(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		for(int i = 0; i < 4; i++){
			TheCount tc = new TheCount(i);
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
