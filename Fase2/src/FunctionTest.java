import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class FunctionTest {

	@Test
	void testEvaluar() {
		
		// (+ a b (- a b)) = 2a
		ArrayList<Node> tokens_se = new ArrayList<Node>();
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("-"));
		con.add(new Atom("a"));
		con.add(new Atom("b"));
		SExpression contenido = new SExpression(con);
		tokens_se = new ArrayList<Node>();
		tokens_se.add(new Atom("+"));
		tokens_se.add(new Atom("a"));
		tokens_se.add(new Atom("b"));
		tokens_se.add(contenido);
		SExpression se = new SExpression(tokens_se);
		
		ArrayList<String> n_vars = new ArrayList<String>();
		n_vars.add("a");
		n_vars.add("b");
		
		ArrayList<Node> tokens = new ArrayList<Node>();
		tokens.add(new Atom(2));
		tokens.add(new Atom(3));
		
		Function func = new Function(se, n_vars);
		assertEquals(func.evaluar(tokens, null).getData(), "4");
		
		
		ArrayList<Node> tokens2 = new ArrayList<Node>();
		tokens2.add(new Atom(5));
		tokens2.add(new Atom(2));
		assertEquals(func.evaluar(tokens2, null).getData(), "10");
	}
	
	@Test 
	void testFunctionAmbiente() {
		// (+ a b (- a b)) = 2a
		ArrayList<Node> tokens_se = new ArrayList<Node>();
		ArrayList<Node> con = new ArrayList<Node>();
		con.add(new Atom("-"));
		con.add(new Atom("a"));
		con.add(new Atom("b"));
		SExpression contenido = new SExpression(con);
		tokens_se = new ArrayList<Node>();
		tokens_se.add(new Atom("+"));
		tokens_se.add(new Atom("a"));
		tokens_se.add(new Atom("b"));
		tokens_se.add(contenido);
		SExpression se = new SExpression(tokens_se);
		
		ArrayList<String> n_vars = new ArrayList<String>();
		n_vars.add("a");
		n_vars.add("b");
		
		
		
		Function func = new Function(se, n_vars);
		Ambiente.setFunc(func, "func");
		
		// (func 2 3) = 4
		ArrayList<Node> tokens = new ArrayList<Node>();
		tokens.add(new Atom("func"));
		tokens.add(new Atom(2));
		tokens.add(new Atom(3));
		SExpression ev = new SExpression(tokens);
		assertEquals(ev.evaluate(null).getData(), "4");
		
		// (func 5 2) = 10
		tokens = new ArrayList<Node>();
		tokens.add(new Atom("func"));
		tokens.add(new Atom(5));
		tokens.add(new Atom(2));
		ev = new SExpression(tokens);
		assertEquals(ev.evaluate(null).getData(), "10");
		
		
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
	void testFunctionParametro() {
		
		// (defun sum (a b) (+ a b))
		ArrayList<Node> data = new ArrayList<Node>();
		data.add(new Atom("defun"));
		data.add(new Atom("sum")); // (defun sum)
		
		ArrayList<Node> vars = new ArrayList<Node>();
		vars.add(new Atom("a"));
		vars.add(new Atom("b"));
		SExpression vars_se = new SExpression(vars);
		
		data.add(vars_se); // (defun sum (a b))
		
		ArrayList<Node> cuerpo = new ArrayList<Node>();
		cuerpo.add(new Atom("+"));
		cuerpo.add(new Atom("a"));
		cuerpo.add(new Atom("b"));
		SExpression cuerpo_se = new SExpression(cuerpo);
		
		data.add(cuerpo_se); // (defun sum (a b) (+ a b))
		SExpression def_func = new SExpression(data);
		def_func.evaluate();
		assert(Ambiente.isFunc("sum"));
		
		
		// (defun ejec (m n f) (f m n))
		ArrayList<Node> data2 = new ArrayList<Node>();
		data2 = new ArrayList<Node>();
		data2.add(new Atom("defun"));
		data2.add(new Atom("ejec")); // (defun ejec)
		
		ArrayList<Node> vars2 = new ArrayList<Node>();
		vars2.add(new Atom("m"));
		vars2.add(new Atom("n"));
		vars2.add(new Atom("f"));
		SExpression vars_se2 = new SExpression(vars2);
		
		data2.add(vars_se2); // (defun ejec (m n f))
		
		ArrayList<Node> cuerpo2 = new ArrayList<Node>();
		cuerpo2.add(new Atom("f"));
		cuerpo2.add(new Atom("m"));
		cuerpo2.add(new Atom("n"));
		SExpression cuerpo_se2 = new SExpression(cuerpo2); // (defun ejec (m n f) (f m n))
		
		data2.add(cuerpo_se2); // (defun ejec (m n f) (f m n))
		SExpression def_func2 = new SExpression(data2);
		def_func2.evaluate();
		assert(Ambiente.isFunc("ejec"));
		
		ArrayList<Node> ev_tok = new ArrayList<Node>();
		ev_tok.add(new Atom("sum"));
		ev_tok.add(new Atom(2));
		ev_tok.add(new Atom(3));
		SExpression ev_s = new SExpression(ev_tok);
		assertEquals(ev_s.evaluate().getData(), "5");
		
		// (ejec 2 3 sum) -> (sum 2 3) -> 5
		ArrayList<Node> ev_tokens = new ArrayList<Node>();
		ev_tokens.add(new Atom("ejec"));
		ev_tokens.add(new Atom(2));
		ev_tokens.add(new Atom(3));
		ev_tokens.add(new Atom("sum"));
		SExpression ev_se = new SExpression(ev_tokens);
		assertEquals(ev_se.evaluate().getData(), "5");
		
	}
	
	@Test
	void testFact() {
		// (defun fact (n) (cond ((equal 0 n) 1) (T (* n (fact (- n 1)))))
		// exp1 : (- n 1)
		// exp2 : (fact exp1)
		// exp3 : (* n exp2)
		// exp4 : (T exp3)
		// exp5 : (equal 0 n)
		// exp6 : (exp5 1)
		// exp7 : (cond exp6 exp4)
		// exp8 : (defun fact (n) exp7)
		
		// exp1 : (- n 1)
		ArrayList<Node> tok1 = new ArrayList<Node>();
		tok1.add(new Atom("-"));
		tok1.add(new Atom("n"));
		tok1.add(new Atom(1));
		SExpression exp1 = new SExpression(tok1);
		
		// exp2 : (fact exp1)
		ArrayList<Node> tok2 = new ArrayList<Node>();
		tok2.add(new Atom("fact"));
		tok2.add(exp1);
		SExpression exp2 = new SExpression(tok2);
		
		// exp3 : (* n exp2)
		ArrayList<Node> tok3 = new ArrayList<Node>();
		tok3.add(new Atom("*"));
		tok3.add(new Atom("n"));
		tok3.add(exp2);
		SExpression exp3 = new SExpression(tok3);
		
		// exp4 : (T exp3)
		ArrayList<Node> tok4 = new ArrayList<Node>();
		tok4.add(new Atom(true));
		tok4.add(exp3);
		SExpression exp4 = new SExpression(tok4);
		
		// exp4 : (equal 0 n)
		ArrayList<Node> tok5 = new ArrayList<Node>();
		tok5.add(new Atom("equal"));
		tok5.add(new Atom("n"));
		tok5.add(new Atom(0));
		SExpression exp5 = new SExpression(tok5);
		
		// exp5 : (exp4 1)
		ArrayList<Node> tok6 = new ArrayList<Node>();
		tok6.add(exp5);
		tok6.add(new Atom(1));
		SExpression exp6 = new SExpression(tok6);
		
		// exp6 : (cond exp5 exp3)
		ArrayList<Node> tok7 = new ArrayList<Node>();
		tok7.add(new Atom("cond"));
		tok7.add(exp6);
		tok7.add(exp4);
		SExpression exp7 = new SExpression(tok7);
		
		// exp8 : (defun fact (n) exp6)
		ArrayList<Node> tok8 = new ArrayList<Node>();
		tok8.add(new Atom("defun"));
		tok8.add(new Atom("fact"));
		tok8.add(new Atom("n"));
		tok8.add(exp7);
		SExpression exp8 = new SExpression(tok8);
		
		assertEquals(exp8.evaluate().getData(), "T");
		assert(Ambiente.isFunc("fact"));
				
		// (fact 0) -> 1
		ArrayList<Node> ev = new ArrayList<Node>();
		ev.add(new Atom("fact"));
		ev.add(new Atom(0));
		SExpression ev_se = new SExpression(ev);
		assertEquals(ev_se.evaluate().getData(), "1");
		
		// (fact 1) -> 1
		ArrayList<Node> ev2 = new ArrayList<Node>();
		ev2.add(new Atom("fact"));
		ev2.add(new Atom(1));
		SExpression ev_se2 = new SExpression(ev2);
		assertEquals(ev_se2.evaluate().getData(), "1");
		
		// (fact 5) -> 120
		ArrayList<Node> ev3 = new ArrayList<Node>();
		ev3.add(new Atom("fact"));
		ev3.add(new Atom(9));
		SExpression ev_se3 = new SExpression(ev3);
		assertEquals(ev_se3.evaluate().getData(), "362880");
	}
	
	@Test
	void testFibonacci() {
		// (defun fib (n) (cond ((equal n 1) 1) ((equal n 2) 1) (T (+ (fib (- n 1)) (fib (- n 2))))))
		// exp1 : (- n 2)
		// exp2 : (fib exp1)
		// exp3 : (- n 1)
		// exp4 : (fib exp3)
		// exp5 : (+ exp2 exp4)
		// exp6 : (T exp5)
		// exp7 : (equal n 2)
		// exp8 : (exp7 1)
		// exp9 : (equal n 1)
		// exp10 : (exp9 1)
		// exp11 : (cond exp10 exp8 exp6)
		// exp12 : (defun fib (n) exp11)
		
		// exp1 : (- n 2)
		ArrayList<Node> tok1 = new ArrayList<Node>();
		tok1.add(new Atom("-"));
		tok1.add(new Atom("n"));
		tok1.add(new Atom(2));
		SExpression exp1 = new SExpression(tok1);
		
		// exp2 : (fib exp1)
		ArrayList<Node> tok2 = new ArrayList<Node>();
		tok2.add(new Atom("fib"));
		tok2.add(exp1);
		SExpression exp2 = new SExpression(tok2);
		

		// exp3 : (- n 1)
		ArrayList<Node> tok3 = new ArrayList<Node>();
		tok3.add(new Atom("-"));
		tok3.add(new Atom("n"));
		tok3.add(new Atom(1));
		SExpression exp3 = new SExpression(tok3);
		
		// exp4 : (fib exp3)
		ArrayList<Node> tok4 = new ArrayList<Node>();
		tok4.add(new Atom("fib"));
		tok4.add(exp3);
		SExpression exp4 = new SExpression(tok4);
		
		// exp5 : (+ exp2 exp4)
		ArrayList<Node> tok5 = new ArrayList<Node>();
		tok5.add(new Atom("+"));
		tok5.add(exp2);
		tok5.add(exp4);
		SExpression exp5 = new SExpression(tok5);
		
		// exp6 : (T exp5)
		ArrayList<Node> tok6 = new ArrayList<Node>();
		tok6.add(new Atom(true));
		tok6.add(exp5);
		SExpression exp6 = new SExpression(tok6);
		
		// exp7 : (equal n 2)
		ArrayList<Node> tok7 = new ArrayList<Node>();
		tok7.add(new Atom("equal"));
		tok7.add(new Atom("n"));
		tok7.add(new Atom(2));
		SExpression exp7 = new SExpression(tok7);
		
		// exp8 : (exp7 0)
		ArrayList<Node> tok8 = new ArrayList<Node>();
		tok8.add(exp7);
		tok8.add(new Atom(1));
		SExpression exp8 = new SExpression(tok8);
		
		// exp9 : (equal n 1)
		ArrayList<Node> tok9 = new ArrayList<Node>();
		tok9.add(new Atom("equal"));
		tok9.add(new Atom("n"));
		tok9.add(new Atom(1));
		SExpression exp9 = new SExpression(tok9);
		
		// exp10 : (exp9 1)
		ArrayList<Node> tok10 = new ArrayList<Node>();
		tok10.add(exp9);
		tok10.add(new Atom(1));
		SExpression exp10 = new SExpression(tok10);
		
		// exp11 : (cond exp10 exp8 exp6)
		ArrayList<Node> tok11 = new ArrayList<Node>();
		tok11.add(new Atom("cond"));
		tok11.add(exp10);
		tok11.add(exp8);
		tok11.add(exp6);
		SExpression exp11 = new SExpression(tok11);
		
		// exp12 : (defun fib (n) exp11)
		ArrayList<Node> tok12 = new ArrayList<Node>();
		tok12.add(new Atom("defun"));
		tok12.add(new Atom("fib"));
		tok12.add(new Atom("n"));
		tok12.add(exp11);
		SExpression exp12 = new SExpression(tok12);
		
		assertEquals(exp12.evaluate().getData(), "T");
		assert(Ambiente.isFunc("fib"));
		
		
		// (fib 1) -> 1
		ArrayList<Node> ev = new ArrayList<Node>();
		ev.add(new Atom("fib"));
		ev.add(new Atom(1));
		SExpression ev_se = new SExpression(ev);
		assertEquals(ev_se.evaluate().getData(), "1");
		
		// (fib 2) -> 1
		ArrayList<Node> ev2 = new ArrayList<Node>();
		ev2.add(new Atom("fib"));
		ev2.add(new Atom(2));
		SExpression ev_se2 = new SExpression(ev2);
		assertEquals(ev_se2.evaluate().getData(), "1");
		
		// (fib 7) -> 13
		ArrayList<Node> ev3 = new ArrayList<Node>();
		ev3.add(new Atom("fib"));
		ev3.add(new Atom(7));
		SExpression ev_se3 = new SExpression(ev3);
		assertEquals(ev_se3.evaluate().getData(), "13");
		
	}

}
