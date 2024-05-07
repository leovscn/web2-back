package com.example.springboot.repositorys;


import com.example.springboot.models.CategoriaModel;
import com.example.springboot.models.VeiculoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculosRepository extends JpaRepository<VeiculoModel, Integer> {

    List<VeiculoModel> findByCategoriaId(int categoriaId);
    List<VeiculoModel> findVeiculosByModeloLike(String modelo);
    List<VeiculoModel> findVeiculosByAno(int modelo);
}