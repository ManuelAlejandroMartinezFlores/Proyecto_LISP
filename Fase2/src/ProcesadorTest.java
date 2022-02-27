/**
 * 
 * @author Manuel Alejandro Martínez Flores
 * @author Mario Puente
 * @author Pedro Marroquín
 * 
 * ProcesadorTest.
 * Evalua las funciones del Procesador.
 *
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProcesadorTest {

	@Test
	void testRevisarParen() {
		// Cantidad correcto de paréntesis
		assert(Procesador.revisarParen("(a (c))"));
		assert(!Procesador.revisarParen("(a (c)"));
	}
	
	@Test
	void testProcesarEX() {
		// Creación de árboles de nodos
		try {
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
		} catch (Exception e) {
			fail();
		}
	}
	
	
	@Test
	void testFactorial() {
		// Evalua declaración de función factorial
		try {
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
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testFibonacci() {
		// Evalua declaración de función fibonacci
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
		// Evalua declaración Quote
		String s = "\'(+ 1 2)";
		assertEquals(Procesador.evaluate(s), "(+ 1 2)");
	}
	
	@Test
	void testCond() {
		// Evalua declaración Cond
		String s = "(cond ((= 0 1) 1) (T 2))";
		assertEquals(Procesador.evaluate(s), "2");
	}
	
	@Test
	void testAtomList() {
		// Evalua declaración Atom
		String s = "(atom 1)";
		assertEquals(Procesador.evaluate(s), "T");
		
		s = "(atom (+ 1 2))";
		assertEquals(Procesador.evaluate(s), "T");
		
		// Evalua declaración List
		s = "(atom (list 1 2))";
		assertEquals(Procesador.evaluate(s), "NIL");
	}
	
	
	
	

}
