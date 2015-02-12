package assign4;

public class Account {
	
	private Integer id;
	private int balance;

	public Account(String id) {
		this(Integer.parseInt(id));
	}
	
	public Account(int id){
		this(new Integer(id));
	}
	
	public Account(Integer id){
		this.id = new Integer(id);
		balance = 1000;
	}
	
	public String toString(){
		return null;
	}

}
