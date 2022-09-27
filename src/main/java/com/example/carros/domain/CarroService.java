package com.example.carros.domain;

import com.example.carros.api.exeception.ObjectNotFoundException;
import com.example.carros.domain.dto.CarroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarroService {

    @Autowired
    private CarroRepository repository;

    public List<CarroDTO> findAll(){
        return repository.findAll().stream().map(CarroDTO::create).collect(Collectors.toList());
    }

    public CarroDTO findById(Long id) {
        return repository.findById(id).map(CarroDTO::create).orElseThrow(() -> new ObjectNotFoundException("Carro não encontrado"));
    }

    public List<CarroDTO> findByTipo(String tipo) {
        return repository.findByTipo(tipo).stream().map(CarroDTO::create).collect(Collectors.toList());
    }

    public CarroDTO save(Carro carro) {
        Assert.isNull(carro.getId(), "Não foi possível inserir o registro.");
        return CarroDTO.create(repository.save(carro));
    }

    public CarroDTO update(Carro carro, Long id) {
        Assert.notNull(id, "Informe um ID para realizar atualização.");

        Optional<Carro> optional = repository.findById(id);
        if(optional.isPresent()){
            Carro db = optional.get();

            db.setNome(carro.getNome());
            db.setTipo(carro.getTipo());
            System.out.println("Carro id " + db.getId());

            repository.save(db);
            return CarroDTO.create(db);
        } else {
            return null;
        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
