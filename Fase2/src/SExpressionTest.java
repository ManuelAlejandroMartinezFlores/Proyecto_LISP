import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class SExpressionTest {

	@Test
	void test() {
		// (+ 1 2 3)
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("+"));
		data.add(new Atom(1));
		data.add(new Atom(2));
		data.add(new Atom(3));
		SExpression se = new SExpression(data);
		assertEquals(se.evaluate(null).getData(), "6");
		
		// (- 3 1)
		data = new ArrayList<Node>();
		data.add(new Atom("-"));
		data.add(new Atom(3));
		data.add(new Atom(1));
		se = new SExpression(data);
		assertEquals(se.evaluate(null).getData(), "2");
		
		// (+ a 1) donde a = 3
		HashMap<String, Node> vars = new HashMap<String, Node>();
		vars.put("a", new Atom(3));
		data = new ArrayList<Node>();
		data.add(new Atom("+"));
		data.add(new Atom("a"));
		data.add(new Atom(1));
		se = new SExpression(data);
		assertEquals(se.evaluate(vars).getData(), "4");
		
		// (+ a 1 (+ 1 2)) donde a = 3
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom(1));
		con.add(new Atom(2));
		SExpression contenido = new SExpression(con);
		data = new ArrayList<Node>();
		data.add(new Atom("+"));
		data.add(new Atom("a"));
		data.add(new Atom(1));
		data.add(contenido);
		se = new SExpression(data);
		assertEquals(se.evaluate(vars).getData(), "7");
		
	}
	
	@Test
	void testSetQ() {
		// (setq a 1 b 3) = a -> 1; b -> 3
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("setq"));
		data.add(new Atom("a"));
		data.add(new Atom(1));
		data.add(new Atom("b"));
		data.add(new Atom(3));
		SExpression se = new SExpression(data);
		se.evaluate();
		
		assertEquals(Ambiente.getVar("a").getData(), "1");
		assertEquals(Ambiente.getVar("b").getData(), "3");
		
		// (setq a 1 b (+ a 1)) = a -> 1; b -> (+ a 1) = 2
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom("a"));
		con.add(new Atom(1));
		SExpression contenido = new SExpression(con);
		data = new ArrayList<Node>();
		data.add(new Atom("setq"));
		data.add(new Atom("a"));
		data.add(new Atom(1));
		data.add(new Atom("b"));
		data.add(contenido);
		se = new SExpression(data);
		se.evaluate();
		assertEquals(Ambiente.getVar("a").getData(), "1");
		assertEquals(Ambiente.getVar("b").getData(), "2");
		
		
	}
	
	
	@Test
	void testDefun() {
		
		// (defun func (a) (+ a 1))
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("defun")); // defun
		data.add(new Atom("func")); // nombre -> func
		data.add(new Atom("a")); // parametros -> (a)
		
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom("a"));
		con.add(new Atom(1));
		SExpression contenido = new SExpression(con);
		data.add(contenido); // SExpression -> (+ a 1)
		
		
		SExpression se = new SExpression(data);
		
		assertEquals(se.evaluate().getData(), "T");
		assert(Ambiente.isFunc("func"));
		
		// (func 2) = (+ 2 1) = 3
		ArrayList<Node> tokens = new ArrayList<Node>();
		tokens.add(new Atom("func"));
		tokens.add(new Atom(2));
		SExpression ev = new SExpression(tokens);
		assertEquals(ev.evaluate().getData(), "3");
		
		// (defun fnc (a b) (+ a b (- a b)))
		ArrayList<Node> tokens_se = new ArrayList<Node>();
		con = new ArrayList<Node>();
		con.add(new Atom("-"));
		con.add(new Atom("a"));
		con.add(new Atom("b"));
		contenido = new SExpression(con);
		tokens_se = new ArrayList<Node>();
		tokens_se.add(new Atom("+"));
		tokens_se.add(new Atom("a"));
		tokens_se.add(new Atom("b"));
		tokens_se.add(contenido);
		se = new SExpression(tokens_se); // (+ a b (- a b))
		
		data = new ArrayList<Node>();
		data.add(new Atom("defun")); // (defun)
		data.add(new Atom("fnc")); // (defun fnc)
		ArrayList<Node> vars = new ArrayList<Node>();
		vars.add(new Atom("a"));// (a)
		vars.add(new Atom("b")); // (a b)
		SExpression vars_se = new SExpression(vars); // (a b)
		data.add(vars_se); // (defun fnc (a b))
		data.add(se); // (defun fnc (a b) (+ a b (- a b)))
		
		SExpression se_final = new SExpression(data);
		
		
		assertEquals(se_final.evaluate().getData(), "T");
		assert(Ambiente.isFunc("fnc"));
		
		// (fnc 2 3) = (+ 2 3 (- 2 3)) = 4
		ArrayList<Node> ev_tokens = new ArrayList<Node>();
		ev_tokens.add(new Atom("fnc"));
		ev_tokens.add(new Atom(2));
		ev_tokens.add(new Atom(3));
		SExpression ev_se = new SExpression(ev_tokens);
		assertEquals(ev_se.evaluate().getData(), "4");
	}
	
	
	@Test
	void testQuote() {
		// '(+ 1 2 3) -> (quote + 1 2 3) -> (+ 1 2 3)
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("quote"));
		data.add(new Atom("+"));
		data.add(new Atom(1));
		data.add(new Atom(2));
		data.add(new Atom(3));
		SExpression se = new SExpression(data);
		
		assertEquals(se.evaluate().getData(), "(+ 1 2 3)");
	}
	
	@Test
	void testEqual() {
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("equal"));

		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom(1));
		con.add(new Atom(3));
		SExpression cont = new SExpression(con);
		
		data.add(cont);
		data.add(new Atom(4));
		
		// (equal (+ 1 3) 4) -> T
		SExpression se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "T");
		
		data.remove(data.size() - 1);
		data.add(new Atom(5));
		// (equal (+ 1 3) 5) -> NIL
		se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "NIL");
		
		
	}
	
	@Test
	void testInequal() {
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("<"));

		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom(1));
		con.add(new Atom(3));
		SExpression cont = new SExpression(con);
		
		data.add(cont);
		data.add(new Atom(15));
		
		// (< (+ 1 3) 15) -> T
		SExpression se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "T");
		
		// (< (+ 1 3) 1) -> NIL
		data.set(data.size() - 1, new Atom(1));
		se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "NIL");
		
		// (> (+ 1 3) 1) -> T
		data.set(0, new Atom(">"));
		se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "T");
		
		// (> (+ 1 3) 15) -> NIL
		data.set(data.size() - 1, new Atom(15));
		se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "NIL");
	}
	
	@Test
	void testAtom() {
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("atom"));

		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom(1));
		con.add(new Atom(3));
		SExpression cont = new SExpression(con);
		
		data.add(cont);
		// (atom (+ 1 3)) -> NIL
		SExpression se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "T");
		
		data.set(1, new Atom("fff"));
		
		// (atom "fff") -> NIL
		se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "T");
	}
	
	@Test
	void testList() {
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("list"));
		
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("+"));
		con.add(new Atom(1));
		con.add(new Atom(3));
		SExpression cont = new SExpression(con);
		
		data.add(cont);
		
		data.add(new Atom(1));
		
		// (list (+ 1 3) 1) -> (4 1)
		SExpression se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "(4 1)");
		
		
	}
	
	@Test
	void testCond() {
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("cond"));
		
		ArrayList<Node> tok1 = new ArrayList<Node>();
		tok1.add(new Atom(false));
		tok1.add(new Atom(1));
		SExpression claus1 = new SExpression(tok1);
		data.add(claus1); // (cond (NIL 1))
		
		ArrayList<Node> tok2 = new ArrayList<Node>();
		tok2.add(new Atom(true));
		tok2.add(new Atom(2));
		SExpression claus2 = new SExpression(tok2);
		data.add(claus2);
		
		// (cond (NIL 1) (T 2)) -> 2
		SExpression se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "2");
		
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("equal"));
		con.add(new Atom(1));
		con.add(new Atom(1));
		SExpression contenido = new SExpression(con);
		
		tok2 = new ArrayList<Node>();
		tok2.add(contenido);
		tok2.add(new Atom(3));
		claus2 = new SExpression(tok2);
		
		data.set(2, claus2);
		
		// (cond (NIL 1) ((equal 1 1) 3)) -> 3
		se = new SExpression(data);
		assertEquals(se.evaluate().getData(), "3");
	}

}
