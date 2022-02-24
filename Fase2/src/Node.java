import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node {

	protected String data;
	
	protected ArrayList<Node> tokens;
	
	public Node() {
		tokens = new ArrayList<Node>();
	}
	
	public abstract boolean isList();
	
	public abstract Node evaluate();
	
	public abstract Node evaluate(HashMap<String, Node> vars_extra);
	
	public String getData() {
		return data;
	}
	
	public ArrayList<Node> getTokens(){
		return tokens;
	}
	
	public abstract Node copy();
	
	public String getSE() {
		return data;
	}

	protected abstract void susVars(HashMap<String, Node> vars_extra);
	
	 
}
