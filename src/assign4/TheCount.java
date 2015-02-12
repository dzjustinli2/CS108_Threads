package assign4;

import java.util.concurrent.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TheCount extends JPanel{
	
	private static JFrame jf;
	private JTextField goalTextField;
	private JLabel currentStanding;
	private JButton start;
	private JButton stop;

	public TheCount() {

	}
	
	public void createAndShowGui(){
		goalTextField = new JTextField("50000");
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
	

	public static void main(String[] args) {
		jf = new JFrame();
		BoxLayout boxLayout = new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS);
		jf.setLayout(boxLayout);
		jf.setPreferredSize(new Dimension(200,500));
		for(int i = 0; i < 4; i++){
			TheCount tc = new TheCount();
			tc.setLayout(new BoxLayout(tc, BoxLayout.Y_AXIS));
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					startGui(tc);
				}
			});
		}
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				jf.setLocationByPlatform(true);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.pack();
				jf.setVisible(true);
			}
		});
	}
	
	public static void startGui(TheCount tc){
		tc.createAndShowGui();
	}

}
