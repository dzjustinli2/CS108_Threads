package assign4;

public class Transaction {
	
	private Integer fromID;
	private Integer toID;
	private int ammount;
	private boolean terminator;
	
	public Transaction(){
		fromID = new Integer(-1);
		toID = new Integer(-1);
		ammount = new Integer(-1);
		terminator = true;
	}

	public Transaction(String from, String to, String amm) {
		fromID = new Integer(Integer.parseInt(from));
		toID = new Integer(Integer.parseInt(to));
		ammount = Integer.parseInt(amm);
		terminator = false;
	}

	public Integer from(){
		return fromID;
	}
	
	public Integer to(){
		return toID;
	}
	
	public int ammount(){
		return ammount;
	}
	
	public boolean isTerminator(){
		return terminator;
	}
}
