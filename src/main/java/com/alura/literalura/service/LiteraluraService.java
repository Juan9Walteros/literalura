package com.alura.literalura.service;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class LiteraluraService {

    private static final String URL_BASE = "https://gutendex.com/books/?search=";

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos convierteDatos;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public Optional<Libro> buscarYGuardarLibro(String titulo) {
        String url = URL_BASE + titulo.replace(" ", "+");
        String json = consumoAPI.obtenerDatos(url);
        ResultadosBusqueda resultados = convierteDatos.obtenerDatos(json, ResultadosBusqueda.class);

        if (resultados.resultados() == null || resultados.resultados().isEmpty()) {
            return Optional.empty();
        }

        DatosLibro datosLibro = resultados.resultados().get(0);

        if (libroRepository.existsByTituloContainingIgnoreCase(datosLibro.titulo())) {
            return Optional.empty(); // duplicado
        }

        Libro libro = new Libro(datosLibro);

        List<Autor> autores = datosLibro.autores().stream()
                .map(da -> autorRepository.findByNombreContainingIgnoreCase(da.nombre())
                        .orElseGet(() -> new Autor(da)))
                .toList();

        libro.setAutores(autores);
        return Optional.of(libroRepository.save(libro));
    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEnAnio(Integer anio) {
        List<Autor> conFallecimiento = autorRepository
                .findByAnioNacimientoLessThanEqualAndAnioFallecimientoGreaterThanEqual(anio, anio);
        List<Autor> sinFallecimiento = autorRepository
                .findByAnioNacimientoLessThanEqualAndAnioFallecimientoIsNull(anio);
        return Stream.concat(conFallecimiento.stream(), sinFallecimiento.stream()).toList();
    }

    public Map<String, Long> cantidadLibrosPorIdioma(List<String> idiomas) {
        return idiomas.stream()
                .collect(java.util.stream.Collectors.toMap(
                        idioma -> idioma,
                        libroRepository::countByIdioma));
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }

    public DoubleSummaryStatistics estadisticasDescargas() {
        return libroRepository.findAll().stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();
    }

    public List<Libro> top10LibrosMasDescargados() {
        return libroRepository.findTop10ByOrderByNumeroDescargasDesc();
    }

    public List<Autor> buscarAutoresPorNombre(String nombre) {
        return autorRepository.findAllByNombreContainingIgnoreCase(nombre);
    }

    public List<Autor> autoresNacidosDesde(Integer anio) {
        return autorRepository.findByAnioNacimientoGreaterThanEqual(anio);
    }

    public List<Autor> autoresFallecidosAntes(Integer anio) {
        return autorRepository.findByAnioFallecimientoLessThan(anio);
    }
}
