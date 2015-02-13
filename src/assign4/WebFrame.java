package assign4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class WebFrame extends JFrame{
	
	public DefaultTableModel dbTable;
	private JTable jTable;
	private JScrollPane scrollPane;
	private JButton singleThreadButton;
	private JButton concurrentThreadButton;
	private JTextField numThreadsTextField;
	private JLabel runningLabel;
	private JLabel completedLabel;
	private JLabel elapsedLabel;
	private JProgressBar progressBar;
	private JButton stopButton;
	public ArrayList<String> links;
	private Launcher lt;

	public WebFrame(String file) {
		super();
		links = new ArrayList<String>();
		readUrls(file);
		graphics();
		addListeners();
		populateTable();
	}
	
	private void setButtonsEnabled(boolean b){
		singleThreadButton.setEnabled(b);
		concurrentThreadButton.setEnabled(b);
		stopButton.setEnabled(!b);
	}
	
	private void interrupt(){
		setButtonsEnabled(true);
		lt.cancelThreads();
	}
	
	private void fetch(int numThreads){
		//We're on the swing thread here
		progressBar.setValue(0);
		elapsedLabel.setText("Elapsed: ");
		clearStatus();
		setButtonsEnabled(false);
		lt = new Launcher(numThreads,this);
		lt.start();
	}
	
	public class Launcher extends Thread{
		private int totalThreads;
		private ArrayList<WebWorker> ar;
		public Semaphore sm;
		public AtomicLong threadsRunning;
		public int tasksFinished;
		public WebFrame program;
		
		public Launcher(int numThreads, WebFrame wb){
			super();
			totalThreads = numThreads;
			threadsRunning = new AtomicLong(0);
			sm = new Semaphore(totalThreads);
			ar = new ArrayList<WebWorker>(totalThreads);
			tasksFinished = 0;
			program = wb;
		}
		
		public void run(){
			incrementLabel();
			int linkSize = links.size();
			int linkIndex = 0;
			try{
				double start = System.currentTimeMillis();
				while(linkIndex < linkSize){
					synchronized(this){
						if(isInterrupted()){
							throw new InterruptedException();
						}
						sm.acquire();
					}
					WebWorker wb = new WebWorker(program,linkIndex);
					ar.add(wb);
					wb.start();
					linkIndex = linkIndex + 1;
				}
				sm.acquire(totalThreads);
				double end = System.currentTimeMillis();
				final double elapsed = (end - start)/1000;
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						elapsedLabel.setText("Elapsed: " + elapsed);
					}
				});
			}catch(Exception e){
				//System.out.println("Interruption in launch thread");
			}
			setButtonsEnabled(true);
			decrementLabel();
		}
		
		public void cancelThreads(){
			synchronized(this){
				int size = ar.size();
				for(int i = 0; i < size; i++){
					WebWorker wb = ar.get(i);
					if(wb != null && wb.isAlive()){
						wb.interrupt();
						wb = null;
					}
				}
			}
			this.interrupt();
		}
	}

	public void decrementLabel(){
		lt.sm.release();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				runningLabel.setText("Running: " + (int)lt.threadsRunning.decrementAndGet());
			}
		});
	}
	
	public void incrementLabel(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				runningLabel.setText("Running: " + (int)lt.threadsRunning.incrementAndGet());
			}
		});
	}
	
	public synchronized void updateProgressBar(){
		lt.tasksFinished = lt.tasksFinished + 1;
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				progressBar.setValue(lt.tasksFinished);
				completedLabel.setText("Completed: " + lt.tasksFinished);
			}
		});
	}
	
	private void addListeners(){
		singleThreadButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Single Button");
				fetch(1);
			}
		});
		concurrentThreadButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Concurrent Button");
				String str = numThreadsTextField.getText();
				int numThreads = 1;
				try{
					numThreads = Integer.parseInt(str);
				}catch(Exception err){ numThreads = 1; }
				fetch(numThreads);
			}
		});
		stopButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Stop Button");
				interrupt();
			}
		});
	}
	
	private void populateTable(){
		int size = links.size();
		for(int i = 0; i < size; i++){
			ArrayList<String> row = new ArrayList<String>(2);
			row.add(links.get(i));
			row.add("");
			dbTable.addRow(row.toArray());
		}
	}
	
	private void clearStatus(){
		int numRows = dbTable.getRowCount();
		for(int i = 0; i < numRows; i++){
			dbTable.setValueAt("",i,1);
		}
	}
	
	public void setStatusValue(String value, int index){
		dbTable.setValueAt(value,index,1);
	}
	
	private void createTable(){
		dbTable = new DefaultTableModel(new String[] { "url", "status"}, 0);
		jTable = new JTable(dbTable);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane = new JScrollPane(jTable);
		scrollPane.setPreferredSize(new Dimension(600,300));
	}
	
	private void createButtons(){
		singleThreadButton = new JButton("Single Thread Fetch");
		concurrentThreadButton = new JButton("Concurrent Fetch");
		numThreadsTextField = new JTextField("4");
		numThreadsTextField.setMaximumSize(new Dimension(50,10));
	}
	
	private void createFields(){
		runningLabel = new JLabel("Running: " + 0);
		completedLabel = new JLabel("Completed: " + 0);
		elapsedLabel = new JLabel("Elapsed: ");
	}
	
	private void createProgressAndStop(){
		progressBar = new JProgressBar(0, links.size());
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
	}
	
	private void graphics(){
		createTable();
		createButtons();
		createFields();
		createProgressAndStop();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(scrollPane);
		this.add(singleThreadButton);
		this.add(concurrentThreadButton);
		this.add(numThreadsTextField);
		this.add(runningLabel);
		this.add(completedLabel);
		this.add(elapsedLabel);
		this.add(progressBar);
		this.add(stopButton);
		setTitle("WebLoader");
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void readUrls(String file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while(line != null){
				//System.out.println(line);
				links.add(line);
				line = br.readLine();
			}
			br.close();
		}catch(Exception e){
			System.out.println("Error reading in links");
		}
	}
	
	public static void main(String[] args) {
		String file = "links.txt";
		if(args.length > 0){
			file = args[0];
		}
		new WebFrame(file);
	}

}
