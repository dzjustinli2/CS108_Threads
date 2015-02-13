package assign4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import assign4.TheCount.WorkerThread;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class WebFrame extends JFrame{
	
	private DefaultTableModel dbTable;
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
	ArrayList<String> links;
	ArrayList<WebWorker> workers;

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
	}
	
	private void fetch(int numThreads){
		setButtonsEnabled(false);
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
		runningLabel = new JLabel("Running: ");
		completedLabel = new JLabel("Completed: ");
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
