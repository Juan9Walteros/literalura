package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByIdioma(String idioma);

    long countByIdioma(String idioma);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();
}
