import java.util.HashMap;

public class Atom extends Node {

	

	private int tipo;
	
	@Override
	public boolean isList() {
		return false;
	}
	
	public Atom(boolean b) {
		if (b) {
			data = "T";
		} else {
			data = "NIL";
		}
		tokens.add(this);
		tipo = 0;
	}
	
	public Atom(int i) {
		data = String.valueOf(i);
		tokens.add(this);
		tipo = 1;
	}
	
	public Atom(String s) {
		data = s;
		tokens.add(this);
		tipo = 2;
	}
	
	public Atom(String data_, int tipo_) {
		data = data_;
		tipo = tipo_;
	}
	
	public Node evaluate() {
		if (Ambiente.vars.containsKey(data)) {
			return Ambiente.vars.get(data);
		}
		return this;
	}

	@Override
	public Node evaluate(HashMap<String, Node> vars_extra) {
		if (vars_extra != null && vars_extra.containsKey(data)) {
			return vars_extra.get(data);
		}
		return evaluate();
	}
	
	public Node copy() {
		return new Atom(data, tipo);
	}
	public void susVars(HashMap<String, Node> vars_extra) {
		data = evaluate(vars_extra).getData();
	}

}
