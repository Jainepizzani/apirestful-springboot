package com.example.carros.api;

import com.example.carros.domain.Carro;
import com.example.carros.domain.CarroService;
import com.example.carros.domain.dto.CarroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carros")
public class CarrosController {

	@Autowired
	private CarroService service;
	
	@GetMapping
	@Secured({"ROLE_USER"})
	public ResponseEntity get() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity getCarrosById(@PathVariable("id")Long id){
		//Lambda (os :: puxa o objeto automaticamente)
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity getCarrosByTipo(@PathVariable("tipo")String tipo){
		List<CarroDTO>  carros = service.findByTipo(tipo);
		return carros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carros);
	}

	@PostMapping
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity post(@RequestBody Carro carro){
		CarroDTO c = service.save(carro);

		URI location = getUri(c.getId());
		return ResponseEntity.created(location).build();
	}

	private URI getUri(Long id){
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();
	}

	@PutMapping("/{id}")
	public ResponseEntity put(@PathVariable("id")Long id, @RequestBody Carro carro){
		CarroDTO c = service.update(carro, id);

		return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id")Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
}
