import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProcesadorTest {

	@Test
	void testRevisarParen() {
		assert(Procesador.revisarParen("(a (c))"));
		assert(!Procesador.revisarParen("(a (c)"));
	}
	
	@Test
	void testProcesarEX() {
		String s = "(a (c))";
		Node se = Procesador.procesarEX(s);
		assertEquals(se.getSE(), s);
		
		s = "(a (b) (c d))";
		se = Procesador.procesarEX(s);
		assertEquals(se.getSE(), s);
		
		s = "(defun sum (a) (+ a 1))";
		se = Procesador.procesarEX(s);
		assertEquals(se.getSE(), s);
		assertEquals(se.evaluate().getData(), "T");
		
		s = "(sum 3)";
		se = Procesador.procesarEX(s);
		assertEquals(se.getSE(), s);
		assertEquals(se.evaluate().getData(), "4");
	}
	
	
	@Test
	void testFactorial() {
		String s = "(defun factorial (n) (cond ((= n 0) 1) (T (* n (factorial (- n 1))))))";
		Node se = Procesador.procesarEX(s);
		s = "(defun factorial (n) (cond ((equal n 0) 1) (T (* n (factorial (- n 1))))))";
		assertEquals(se.getSE(), s);
		assertEquals(se.evaluate().getData(), "T");
		
		s = "(factorial 0)";
		se = Procesador.procesarEX(s);
		assertEquals(se.getSE(), s);
		assertEquals(se.evaluate().getData(), "1");
		
		s = "(factorial 5)";
		se = Procesador.procesarEX(s);
		assertEquals(se.getSE(), s);
		assertEquals(se.evaluate().getData(), "120");
		
		assertEquals(Procesador.evaluate(s), "120");
	}
	
	@Test
	void testFibonacci() {
		String s = "(defun fibonacci (n) (cond ((= n 1) 1) ((= n 2) 1) (T (+ (fibonacci (- n 1)) (fibonacci (- n 2))))))";
		assertEquals(Procesador.evaluate(s), "T");
		
		s = "(fibonacci 1)";
		assertEquals(Procesador.evaluate(s), "1");
		
		s = "(fibonacci 2)";
		assertEquals(Procesador.evaluate(s), "1");
		
		s = "(fibonacci 5)";
		assertEquals(Procesador.evaluate(s), "5");
		
		s = "(fibonacci 7)";
		assertEquals(Procesador.evaluate(s), "13");
	}
	
	@Test
	void testQuote() {
		String s = "\'(+ 1 2)";
		assertEquals(Procesador.evaluate(s), "(+ 1 2)");
	}
	
	@Test
	void testCond() {
		String s = "(cond ((= 0 1) 1) (T 2))";
		assertEquals(Procesador.evaluate(s), "2");
	}
	
	@Test
	void testAtomList() {
		String s = "(atom 1)";
		assertEquals(Procesador.evaluate(s), "T");
		
		s = "(atom (+ 1 2))";
		assertEquals(Procesador.evaluate(s), "T");
		
		s = "(atom (list 1 2))";
		assertEquals(Procesador.evaluate(s), "NIL");
	}
	
	
	
	

}
