package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreContainingIgnoreCase(String nombre);

    List<Autor> findAllByNombreContainingIgnoreCase(String nombre);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(
            Integer anioNacimiento, Integer anioFallecimiento);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioFallecimientoIsNull(
            Integer anioNacimiento);

    List<Autor> findByAnioNacimientoGreaterThanEqual(Integer anioNacimiento);

    List<Autor> findByAnioFallecimientoLessThan(Integer anioFallecimiento);
}
