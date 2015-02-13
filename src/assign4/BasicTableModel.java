package assign4;

//BasicTableModel.java
/*
Demonstrate a basic table model implementation
using ArrayList of rows, where each row is itself an ArrayList
of the data in that row.
This code is free for any purpose -- Nick Parlante.
A row may be shorter than the number of columns
which complicates the data handling a bit.
*/
import javax.swing.table.*;

import java.util.*;

@SuppressWarnings("serial")
public class BasicTableModel extends AbstractTableModel {

	private ArrayList<String> colNames; // defines the number of cols
	private ArrayList<ArrayList<String>> data;
	// arraylist of arraylists
	
	/**
	 * Constructor, sets up the tableModel
	 */
	public BasicTableModel() {
		colNames = new ArrayList<String>();
		data = new ArrayList<ArrayList<String>>();
	}
	/*
	Basic getXXX methods required by an class implementing TableModel
	 */
	/**
	 *  Returns the name of each col, numbered 0..columns-1
	 *  @param col index of column
	 *  @return name of column
	 */
	public String getColumnName(int col) {
		return colNames.get(col);
	}
	/**
	 * Returns the number of columns
	 * @return int number of columns
	 */
	public int getColumnCount() {
		return(colNames.size());
	}
	/**
	 *  Returns the number of rows
	 */
	public int getRowCount() {
		return(data.size());
	}
	/**
	 * Returns the data for each cell, identified by its
	 * row col index
	 * @param row row of cell
	 * @param col col of cell
	 * @return object at the table index
	 */
	public Object getValueAt(int row, int col) {
		ArrayList<String> rowList = data.get(row);
		Object result = null;
		if (col<rowList.size()) {
			result = rowList.get(col);
		}
		// _apparently_ it's ok to return null for a "blank" cell
		return(result);
	}
	/**
	 *  Returns true if a cell should be editable in the table
	 *  @param row of cell
	 *  @param col of cell
	 *  @return true if editable
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	/**
	 * Changes the value of a cell
	 * @param value the string value to set
	 * @param row of cell
	 * @param col of cell
	 */
	public void setValueAt(Object value, int row, int col) {
		ArrayList<String> rowList = data.get(row);
		// make this row long enough
		if (col>=rowList.size()) {
			while (col>=rowList.size()) rowList.add(null);
		}
		// install the data
		rowList.set(col,(String)value);
		// notify model listeners of cell change
		fireTableCellUpdated(row, col);
	}
	/*
	Convenience methods of BasicTable
	 */
	/**
	 *  Adds the given column to the right hand side of the model
	 * @param name is the string value of the column at the very top
	 */
	public void addColumn(String name) {
		colNames.add(name);
		fireTableStructureChanged();
		/*	At present, TableModelListener does not have a more specific
			notification for changing the number of columns.
		 */
	}
	/**
	 *  Adds the given row, returns the new row index
	 * @param row each index of this arraylist corresponds to a column in the table
	 * @return index of the created row
	 */
	public int addRow(ArrayList<String> row) {
		data.add(row);
		fireTableRowsInserted(data.size()-1, data.size()-1);
		return(data.size() -1);
	}
	/**
	 *  Adds an empty row, returns the new row index
	 * @return int 
	 */
	public int addRow() {
		// Create a new row with nothing in it
		ArrayList<String> row = new ArrayList<String>();
		return(addRow(row));
	}
	/** Deletes the given row starting at 0
	 * 
	 * @param row index to delete
	 */
	public void deleteRow(int row) {
		if (row == -1) return;
		data.remove(row);
		fireTableRowsDeleted(row, row);
	}
	
}
