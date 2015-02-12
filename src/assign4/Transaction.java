package assign4;

public class Transaction {
	
	private Integer fromID;
	private Integer toID;
	private int ammount;
	private boolean sentinal;
	
	public Transaction(){
		fromID = new Integer(-1);
		toID = new Integer(-1);
		ammount = new Integer(-1);
		sentinal = true;
	}

	public Transaction(String from, String to, String amm) {
		fromID = new Integer(Integer.parseInt(from));
		toID = new Integer(Integer.parseInt(to));
		ammount = Integer.parseInt(amm);
		sentinal = false;
	}

	public Integer getFrom(){
		return fromID;
	}
	
	public Integer getTo(){
		return toID;
	}
	
	public int getAmmount(){
		return ammount;
	}
	
	public boolean isSentinal(){
		return sentinal;
	}
	
	public String toString(){
		return fromID + " " + toID + " " + ammount;
	}
}
