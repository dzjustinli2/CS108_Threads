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

	public TheCount() {
		goal = 100000000;
	}
	
	public void createAndShowGui(){
		goalTextField = new JTextField(goal + "");
		currentStanding = new JLabel("0");
		start = new JButton("Start");
		stop = new JButton("Stop");
		add(goalTextField);
		add(currentStanding);
		add(start);
		add(stop);
		add(Box.createRigidArea(new Dimension(0,40)));
		jf.add(this);
	}
	
	private void addListeners(){
		goalTextField.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = goalTextField.getText();
				goal = Integer.parseInt(text);
			}
		});
		start.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("start" + goal);
			}
		});
		stop.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("stop " + goal);
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
		jf.setVisible(true);
		//It's visible now, need to use the swing thread
		for(int i = 0; i < 4; i++){
			TheCount tc = new TheCount();
			tc.setLayout(new BoxLayout(tc, BoxLayout.Y_AXIS));
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					tc.createAndShowGui();
					tc.addListeners();
				}
			});
		}
	}

}
