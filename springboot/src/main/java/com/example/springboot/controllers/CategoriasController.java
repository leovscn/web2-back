package com.example.springboot.controllers;

import com.example.springboot.dtos.CategoriaRecordDto;
import com.example.springboot.models.CategoriaModel;
import com.example.springboot.repositorys.CategoriaRepository;
import com.example.springboot.repositorys.VeiculosRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriasController {

    @Autowired
    CategoriaRepository cursosRepository;
    @Autowired
    VeiculosRepository alunosRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @PostMapping("/categoria")
    public ResponseEntity<CategoriaModel> salvarCategoria(@RequestBody @Valid CategoriaRecordDto cursoDto) {
        var cursoModel = new CategoriaModel();
        BeanUtils.copyProperties(cursoDto, cursoModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(cursosRepository.save(cursoModel));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<CategoriaModel>> getAllCategoria(){
        return ResponseEntity.status(HttpStatus.OK).body(cursosRepository.findAll());
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<Object> getOneCategoria(@PathVariable(value="id") int id){
        Optional<CategoriaModel> curso = cursosRepository.findById(id);
        return curso.<ResponseEntity<Object>>map(cursoModel -> ResponseEntity.status(HttpStatus.OK).body(cursoModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria Não encontrada"));
    }
    @PutMapping("/categoria/{id}")
    public ResponseEntity<Object> updateCurso(@PathVariable(value="id") int id,
                                              @RequestBody @Valid CategoriaRecordDto cursoRecordDto) {
        Optional<CategoriaModel> curso = cursosRepository.findById(id);
        if(curso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrado");
        }
        var cursoModel = curso.get();
        BeanUtils.copyProperties(cursoRecordDto, cursoModel);
        return ResponseEntity.status(HttpStatus.OK).body(cursosRepository.save(cursoModel));
    }

    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<Object> deleteCategoria(@PathVariable(value="id") int id) {
        Optional<CategoriaModel> curso = cursosRepository.findById(id);
        if(curso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrado");
        }
        cursosRepository.delete(curso.get());
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

}
