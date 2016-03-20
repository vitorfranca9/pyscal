package br.unibh.pyscal.test;

import org.junit.Assert;
import org.junit.Test;

public class AnaliseLexicaTest {
	
	@Test
	public void test() {
		Double esperado = 2.0;
		Double atual = 3.0;
		
		Assert.assertEquals(String.format("esperado %s, veio %s", esperado, atual), esperado, atual);
	}

}
