package com.example.carros;

import com.example.carros.api.exeception.ObjectNotFoundException;
import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarrosServiceTest {

	@Autowired
	private CarroService service;

	@Test
	void contextLoads() {
	}

	@Test
	public void testeSave(){
		Carro carro = new Carro();
		carro.setNome("Ferrari");
		carro.setTipo("esportivos");

		//verifico se o carro n ta null
		CarroDTO c = service.save(carro);
		assertNotNull(c);

		//verifico se o id nao ta null
		Long id = c.getId();
		assertNotNull(id);

		//buscar o objeto
		c = service.findById(id);
		assertNotNull(c);

		//verifica se o objeto é igual ao que criamos acima
		assertEquals("Ferrari", c.getNome());
		assertEquals("esportivos", c.getTipo());

		//deleta o objeto
		service.delete(id);

		//Verifica se deletou
		try{
			assertNotNull(service.findById(id));
			fail("O carro não foi excluído");
		} catch(ObjectNotFoundException e){
			// OK
		}
	}

	@Test
	public void testeLista(){
		List<CarroDTO> carros = service.findAll();
		assertEquals(30, carros.size());
	}

	@Test
	public void testeGet(){
		CarroDTO c = service.findById(11L);
		assertNotNull(c);
		assertEquals("Ferrari FF", c.getNome());
	}

	@Test
	public void testeListarPorTipo(){
		assertEquals(10, service.findByTipo("classicos").size());
		assertEquals(10, service.findByTipo("esportivos").size());
		assertEquals(10, service.findByTipo("luxo").size());

		assertEquals(0, service.findByTipo("x").size());
	}
}
