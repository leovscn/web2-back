package com.example.springboot.controllers;


import com.example.springboot.dtos.VeiculoRecordDto;
import com.example.springboot.models.CategoriaModel;
import com.example.springboot.models.VeiculoModel;
import com.example.springboot.repositorys.CategoriaRepository;
import com.example.springboot.repositorys.VeiculosRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class VeiculosController {

	private static String imgPath = "./src/main/resources/img/";
	@Autowired
	VeiculosRepository alunosRepository;
	@Autowired
	CategoriaRepository cursosRepository;
	@PostMapping(path = "/veiculo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VeiculoModel> salvarVeiculo(@RequestParam("file") MultipartFile imagem, @ModelAttribute @Valid VeiculoRecordDto alunoDto ) {
		var alunoModel = new VeiculoModel();
		BeanUtils.copyProperties(alunoDto, alunoModel);

		try {
			if (!imagem.isEmpty()){
				byte[] bytes = imagem.getBytes();
				Path path = Paths.get(imgPath+imagem.getOriginalFilename());
				Files.write(path, bytes);
				alunoModel.setImagem(imagem.getOriginalFilename());
			}
		} catch (IOException e){
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(alunosRepository.save(alunoModel));
	}

	@GetMapping("/veiculo")
	public ResponseEntity<List<VeiculoModel>> getAllVeiculos(){
		return ResponseEntity.status(HttpStatus.OK).body(alunosRepository.findAll());
	}
	@GetMapping("/veiculo/pesquisar")
	public ResponseEntity<List<VeiculoModel>> pesquisarProdutos(@RequestParam String modelo) {
		try {
			var ano = Integer.parseInt(modelo);
			if (ano > 1950) {
				// If it's a valid integer, search by year
				return ResponseEntity.status(HttpStatus.OK).body(alunosRepository.findVeiculosByAno(ano));
			} else if(!modelo.isEmpty()) {
				// If it's not a valid year, search by model
				return ResponseEntity.status(HttpStatus.OK).body(alunosRepository.findVeiculosByModeloLike('%'+modelo+'%'));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
			return ResponseEntity.status(HttpStatus.OK).body(alunosRepository.findAll());
	}

	@GetMapping("/veiculo/categoria={id}")
	public ResponseEntity<List<VeiculoModel>> getVeiculoByCategoriaId(@PathVariable(value = "id") int id){
		return ResponseEntity.status(HttpStatus.OK).body(alunosRepository.findByCategoriaId(id));
	}

	@GetMapping("/veiculo/{id}")
	public ResponseEntity<Object> getOneVeiculo(@PathVariable(value="id") int id){
		Optional<VeiculoModel> aluno = alunosRepository.findById(id);
        return aluno.<ResponseEntity<Object>>map(alunoModel -> ResponseEntity.status(HttpStatus.OK).body(alunoModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo Não encontrado"));

    }
	@PutMapping(path = "/veiculo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> updateVeiculo(@PathVariable(value="id") int id,
											  @ModelAttribute @Valid VeiculoRecordDto alunoRecordDto, @RequestParam MultipartFile file) {
		Optional<VeiculoModel> aluno = alunosRepository.findById(id);
		if(aluno.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo não encontrado");
		}
		var alunoModel = aluno.get();
		BeanUtils.copyProperties(alunoRecordDto, alunoModel);
		try {
			if (!file.isEmpty()){
				byte[] bytes = file.getBytes();
				Path path = Paths.get(imgPath+file.getOriginalFilename());
				Files.write(path, bytes);
				alunoModel.setImagem(file.getOriginalFilename());
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(alunosRepository.save(alunoModel));
	}


	@DeleteMapping("/veiculo/{id}")
	public ResponseEntity<Object> deleteVeiculo(@PathVariable(value="id") int id) {
		Optional<VeiculoModel> aluno = alunosRepository.findById(id);
		if(aluno.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo não encontrado");
		}
		alunosRepository.delete(aluno.get());
		return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
	}

	@GetMapping("/imagem/{imagem}")
	@ResponseBody
	public byte[] mostraImagem(@PathVariable("imagem") String imagem) throws IOException {
		File nomeArquivo =
				new File(imgPath+imagem);
		if(imagem != null || !imagem.trim().isEmpty()) {
			return Files.readAllBytes(nomeArquivo.toPath());
		}
		return null;
	}

}
