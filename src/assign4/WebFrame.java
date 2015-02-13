package assign4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class WebFrame extends JFrame{
	
	private BasicTableModel bTable;
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

	public WebFrame(String file) {
		super();
		links = new ArrayList<String>();
		readUrls(file);
		graphics();
		populateTable();
	}
	
	private void populateTable(){
		int size = links.size();
		for(int i = 0; i < size; i++){
			ArrayList<String> row = new ArrayList<String>(2);
			row.add(links.get(i));
			row.add("");
			bTable.addRow(row);
		}
	}
	
	private void clearRows(){
		while(bTable.getRowCount() > 0){
			bTable.deleteRow(0);
		}
	}
	
	private void createTable(){
		bTable = new BasicTableModel();
		bTable.addColumn("url");
		bTable.addColumn("status");
		jTable = new JTable(bTable);
		scrollPane = new JScrollPane(jTable);
		scrollPane.setPreferredSize(new Dimension(500,300));
	}
	
	private void createButtons(){
		singleThreadButton = new JButton("Single Thread Fetch");
		concurrentThreadButton = new JButton("Concurrent Fetch");
		numThreadsTextField = new JTextField();
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

	public static void main(String[] args) {
		String file = "links.txt";
		if(args.length > 0){
			file = args[0];
		}
		WebFrame wb = new WebFrame(file);
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

}
