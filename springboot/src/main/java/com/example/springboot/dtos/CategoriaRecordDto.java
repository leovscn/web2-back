package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRecordDto(@NotBlank String nome) {

}
