package com.example.springboot.dtos;

import com.example.springboot.models.CategoriaModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record VeiculoRecordDto(@NotBlank String modelo, @NotBlank String placa, CategoriaModel categoria, String marca, String cor, String imagem, double preco, int ano) {

}