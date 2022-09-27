package com.example.carros;

import com.example.carros.domain.Carro;
import com.example.carros.domain.dto.CarroDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrosApiTest {

    private static String api = "/api/v1/carros";

    @Autowired
    protected TestRestTemplate rest;

    private ResponseEntity<CarroDTO> getCarro(String url){
        return rest.withBasicAuth("user", "123").getForEntity(url, CarroDTO.class);
    }

    private ResponseEntity<List<CarroDTO>> getCarros(String url){
        return rest.withBasicAuth("user", "123").exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CarroDTO>>() {}
                );
    }

    @Test
    public void testSave(){
        Carro carro = new Carro();
        carro.setNome("Porshe");
        carro.setTipo("esportivos");

        //Insert
        ResponseEntity responseEntity = rest.withBasicAuth("admin", "123").postForEntity(api, carro, null);
        System.out.println(responseEntity);

        //Verifica se criou
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        //Busca o objeto
        String location = responseEntity.getHeaders().get("location").get(0);
        CarroDTO c = getCarro(location).getBody();

        assertNotNull(c);
        assertEquals("Porshe", c.getNome());
        assertEquals("esportivos", c.getTipo());

        //Deletar o objeto
        rest.withBasicAuth("user", "123").delete(location);

        //Verifica se deletou
        assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
    }

    @Test
    public void testLista(){
        List<CarroDTO> carros = getCarros(api).getBody();
        assertNotNull(carros);
        assertEquals(30, carros.size());
    }

    @Test
    public void testListaPorTipo(){
        assertEquals(10, getCarros(api+"/tipo/classicos").getBody().size());
        assertEquals(10, getCarros(api+"/tipo/esportivos").getBody().size());
        assertEquals(10, getCarros(api+"/tipo/luxo").getBody().size());

        assertEquals(HttpStatus.NO_CONTENT, getCarros(api+"/tipo/xxx").getStatusCode());
    }

    @Test
    public void testGetOk(){
        ResponseEntity<CarroDTO> response = getCarro(api+"/11");
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        CarroDTO c = response.getBody();
        assertEquals("Ferrari FF", c.getNome());
    }

    @Test
    public void testGetNotFound(){
        ResponseEntity<CarroDTO> response = getCarro(api+"/1000");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
