package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.service.LiteraluraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    // ── Colores ANSI ──────────────────────────────────────────────
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String WHITE = "\u001B[37m";

    @Autowired
    private LiteraluraService servicio;

    private final Scanner teclado = new Scanner(System.in);

    public void ejecutar() {
        mostrarBienvenida();
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
            procesarOpcion(opcion);
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> buscarLibro();
            case 2 -> listarLibros();
            case 3 -> listarAutores();
            case 4 -> listarAutoresVivosEnAnio();
            case 5 -> listarLibrosPorIdioma();
            case 6 -> mostrarEstadisticasPorIdioma();
            case 7 -> mostrarEstadisticasDescargas();
            case 8 -> mostrarTop10LibrosMasDescargados();
            case 9 -> buscarAutorPorNombre();
            case 10 -> consultasAdicionalesAutores();
            case 0 -> mostrarDespedida();
            default -> System.out.println(RED + "\n  ✖  Opción inválida. Intenta de nuevo." + RESET);
        }
    }

    // ── Vistas ────────────────────────────────────────────────────

    private void mostrarBienvenida() {
        System.out.println();
        System.out.println(CYAN + BOLD + "  ╔══════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "  ║                                              ║" + RESET);
        System.out.println(
                CYAN + BOLD + "  ║   " + YELLOW + "  📚  L I T E R A L U R A  📚  " + CYAN + "           ║" + RESET);
        System.out.println(
                CYAN + BOLD + "  ║   " + WHITE + "  Catálogo de libros - Gutendex API   " + CYAN + "   ║" + RESET);
        System.out.println(CYAN + BOLD + "  ║                                              ║" + RESET);
        System.out.println(CYAN + BOLD + "  ╚══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void mostrarMenu() {
        System.out.println(PURPLE + BOLD + "\n  ┌─────────────────────────────────────────┐" + RESET);
        System.out.println(
                PURPLE + BOLD + "  │            " + WHITE + "✦  MENÚ PRINCIPAL  ✦" + PURPLE + "           │" + RESET);
        System.out.println(PURPLE + BOLD + "  ├─────────────────────────────────────────┤" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "1" + RESET + "  →  Buscar libro por título           " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "2" + RESET + "  →  Listar libros registrados         " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "3" + RESET + "  →  Listar autores registrados        " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "4" + RESET + "  →  Autores vivos en un año           " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "5" + RESET + "  →  Listar libros por idioma          " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "6" + RESET + "  →  Estadísticas por idioma           " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "7" + RESET + "  →  Estadísticas de descargas         " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "8" + RESET + "  →  Top 10 libros más descargados     " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "9" + RESET + "  →  Buscar autor por nombre           " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "10" + RESET + " →  Consultas adicionales de autores  " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + "  │  " + RED + "0" + RESET + "  →  Salir                             " + PURPLE
                + "│" + RESET);
        System.out.println(PURPLE + BOLD + "  └─────────────────────────────────────────┘" + RESET);
        System.out.print(YELLOW + "\n  ▶  Tu elección: " + RESET);
    }

    private void buscarLibro() {
        System.out.println(CYAN + BOLD + "\n  ╔══ 🔍 BUSCAR LIBRO ══╗" + RESET);
        System.out.print(WHITE + "  Ingresa el título: " + RESET);
        String titulo = teclado.nextLine();

        System.out.println(YELLOW + "\n  ⏳ Buscando en Gutendex..." + RESET);

        Optional<Libro> resultado = servicio.buscarYGuardarLibro(titulo);

        if (resultado.isPresent()) {
            Libro libro = resultado.get();
            System.out.println(GREEN + BOLD + "\n  ✔  ¡Libro encontrado y registrado!" + RESET);
            imprimirLibro(libro);
        } else {
            if (servicio.listarLibros().stream()
                    .anyMatch(l -> l.getTitulo().toLowerCase().contains(titulo.toLowerCase()))) {
                System.out.println(YELLOW + "\n  ⚠  Este libro ya está registrado en tu catálogo." + RESET);
            } else {
                System.out.println(RED + "\n  ✖  No se encontró ningún libro con ese título." + RESET);
            }
        }
    }

    private void listarLibros() {
        List<Libro> libros = servicio.listarLibros();
        System.out.println(CYAN + BOLD + "\n  ╔══ 📚 LIBROS REGISTRADOS ══╗" + RESET);

        if (libros.isEmpty()) {
            System.out.println(YELLOW + "\n  ⚠  No hay libros registrados aún." + RESET);
            return;
        }

        System.out.println(BLUE + "  Total: " + WHITE + BOLD + libros.size() + " libro(s)" + RESET);
        libros.forEach(this::imprimirLibro);
    }

    private void listarAutores() {
        List<Autor> autores = servicio.listarAutores();
        System.out.println(CYAN + BOLD + "\n  ╔══ 👤 AUTORES REGISTRADOS ══╗" + RESET);

        if (autores.isEmpty()) {
            System.out.println(YELLOW + "\n  ⚠  No hay autores registrados aún." + RESET);
            return;
        }

        System.out.println(BLUE + "  Total: " + WHITE + BOLD + autores.size() + " autor(es)" + RESET);
        autores.forEach(this::imprimirAutor);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println(CYAN + BOLD + "\n  ╔══ 🗓  AUTORES VIVOS EN UN AÑO ══╗" + RESET);
        System.out.print(WHITE + "  Ingresa el año: " + RESET);
        try {
            int anio = Integer.parseInt(teclado.nextLine().trim());
            List<Autor> autores = servicio.listarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println(YELLOW + "\n  ⚠  No se encontraron autores vivos en el año " + anio + "." + RESET);
            } else {
                System.out.println(GREEN + BOLD + "\n  ✔  Autores vivos en " + anio + ":" + RESET);
                autores.forEach(this::imprimirAutor);
            }
        } catch (NumberFormatException e) {
            System.out.println(RED + "\n  ✖  Por favor ingresa un año válido." + RESET);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println(CYAN + BOLD + "\n  ╔══ 🌍 LIBROS POR IDIOMA ══╗" + RESET);
        System.out.println(WHITE + "  Idiomas disponibles: " + GREEN + "en" + WHITE + " (Inglés)  " +
                GREEN + "es" + WHITE + " (Español)  " + GREEN + "fr" + WHITE + " (Francés)  " + GREEN + "pt" + WHITE
                + " (Portugués)" + RESET);
        System.out.print(WHITE + "  Ingresa el código de idioma: " + RESET);
        String idioma = teclado.nextLine().trim().toLowerCase();

        List<Libro> libros = servicio.listarLibrosPorIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println(YELLOW + "\n  ⚠  No hay libros registrados en ese idioma." + RESET);
        } else {
            System.out.println(GREEN + BOLD + "\n  ✔  Libros en idioma '" + idioma + "':" + RESET);
            libros.forEach(this::imprimirLibro);
        }
    }

    private void mostrarEstadisticasPorIdioma() {
        System.out.println(CYAN + BOLD + "\n  ╔══ 📊 ESTADÍSTICAS POR IDIOMA ══╗" + RESET);
        List<String> idiomas = List.of("en", "es", "fr", "pt");
        Map<String, Long> stats = servicio.cantidadLibrosPorIdioma(idiomas);

        Map<String, String> nombres = Map.of(
                "en", "Inglés",
                "es", "Español",
                "fr", "Francés",
                "pt", "Portugués");

        idiomas.forEach(idioma -> System.out.println(
                PURPLE + "  │  " + BLUE + nombres.get(idioma) +
                " (" + idioma + "): " + WHITE + BOLD + stats.get(idioma) + " libro(s)" + RESET));
    }

    private void buscarAutorPorNombre() {
        System.out.println(CYAN + BOLD + "\n  ╔══ 🔎 BUSCAR AUTOR POR NOMBRE ══╗" + RESET);
        System.out.print(WHITE + "  Ingresa el nombre: " + RESET);
        String nombre = teclado.nextLine().trim();

        List<Autor> autores = servicio.buscarAutoresPorNombre(nombre);

        if (autores.isEmpty()) {
            System.out.println(YELLOW + "\n  ⚠  No se encontró ningún autor con ese nombre." + RESET);
        } else {
            System.out.println(GREEN + BOLD + "\n  ✔  Resultados para '" + nombre + "':" + RESET);
            autores.forEach(this::imprimirAutor);
        }
    }

    private void mostrarEstadisticasDescargas() {
        DoubleSummaryStatistics stats = servicio.estadisticasDescargas();
        System.out.println(CYAN + BOLD + "\n  ╔══ 📈 ESTADÍSTICAS DE DESCARGAS ══╗" + RESET);
        if (stats.getCount() == 0) {
            System.out.println(YELLOW + "\n  ⚠  No hay libros registrados aún." + RESET);
            return;
        }
        System.out.println(PURPLE + "  │  " + BLUE + "Total libros:  " + WHITE + BOLD + stats.getCount() + RESET);
        System.out.println(PURPLE + "  │  " + BLUE + "Máx descargas: " + WHITE + BOLD + String.format("%,.0f", stats.getMax()) + RESET);
        System.out.println(PURPLE + "  │  " + BLUE + "Mín descargas: " + WHITE + BOLD + String.format("%,.0f", stats.getMin()) + RESET);
        System.out.println(PURPLE + "  │  " + BLUE + "Promedio:      " + WHITE + BOLD + String.format("%,.0f", stats.getAverage()) + RESET);
        System.out.println(PURPLE + "  │  " + BLUE + "Total:         " + WHITE + BOLD + String.format("%,.0f", stats.getSum()) + RESET);
    }

    private void mostrarTop10LibrosMasDescargados() {
        List<Libro> libros = servicio.top10LibrosMasDescargados();
        System.out.println(CYAN + BOLD + "\n  ╔══ 🏆 TOP 10 LIBROS MÁS DESCARGADOS ══╗" + RESET);
        if (libros.isEmpty()) {
            System.out.println(YELLOW + "\n  ⚠  No hay libros registrados aún." + RESET);
            return;
        }
        int[] posicion = {1};
        libros.forEach(libro -> {
            System.out.println(YELLOW + BOLD + "\n  " + posicion[0]++ + "." + RESET);
            imprimirLibro(libro);
        });
    }

    private void consultasAdicionalesAutores() {
        System.out.println(CYAN + BOLD + "\n  ╔══ 🗂  CONSULTAS ADICIONALES DE AUTORES ══╗" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "1" + RESET + "  →  Nacidos a partir de un año" + RESET);
        System.out.println(PURPLE + "  │  " + GREEN + "2" + RESET + "  →  Fallecidos antes de un año" + RESET);
        System.out.print(YELLOW + "\n  ▶  Tu elección: " + RESET);

        int sub;
        try {
            sub = Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(RED + "\n  ✖  Opción inválida." + RESET);
            return;
        }

        System.out.print(WHITE + "  Ingresa el año: " + RESET);
        int anio;
        try {
            anio = Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(RED + "\n  ✖  Por favor ingresa un año válido." + RESET);
            return;
        }

        List<Autor> autores = switch (sub) {
            case 1 -> servicio.autoresNacidosDesde(anio);
            case 2 -> servicio.autoresFallecidosAntes(anio);
            default -> {
                System.out.println(RED + "\n  ✖  Opción inválida." + RESET);
                yield List.of();
            }
        };

        if (autores.isEmpty()) {
            System.out.println(YELLOW + "\n  ⚠  No se encontraron autores con esos criterios." + RESET);
        } else {
            autores.forEach(this::imprimirAutor);
        }
    }

    private void mostrarDespedida() {
        System.out.println();
        System.out.println(CYAN + BOLD + "  ╔══════════════════════════════════════╗" + RESET);
        System.out
                .println(CYAN + BOLD + "  ║  " + YELLOW + "¡Hasta pronto! Gracias por leer 📖  " + CYAN + "║" + RESET);
        System.out.println(CYAN + BOLD + "  ╚══════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    // ── Helpers de impresión ──────────────────────────────────────

    private void imprimirLibro(Libro libro) {
        System.out.println(PURPLE + "\n  ┌─────────────────────────────────────────" + RESET);
        System.out.println(PURPLE + "  │  " + WHITE + BOLD + "📖 " + libro.getTitulo() + RESET);
        System.out.println(PURPLE + "  │  " + BLUE + "Autores:    " + WHITE +
                libro.getAutores().stream().map(Autor::getNombre).reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b));
        System.out.println(PURPLE + "  │  " + BLUE + "Idioma:     " + WHITE + libro.getIdioma().toUpperCase());
        System.out.println(
                PURPLE + "  │  " + BLUE + "Descargas:  " + WHITE + String.format("%,d", libro.getNumeroDescargas()));
        System.out.println(PURPLE + "  └─────────────────────────────────────────" + RESET);
    }

    private void imprimirAutor(Autor autor) {
        String nacimiento = autor.getAnioNacimiento() != null ? String.valueOf(autor.getAnioNacimiento()) : "?";
        String fallecimiento = autor.getAnioFallecimiento() != null ? String.valueOf(autor.getAnioFallecimiento())
                : "?";
        List<String> titulosLibros = autor.getLibros().stream().map(Libro::getTitulo).toList();

        System.out.println(PURPLE + "\n  ┌─────────────────────────────────────────" + RESET);
        System.out.println(PURPLE + "  │  " + WHITE + BOLD + "✍  " + autor.getNombre() + RESET);
        System.out.println(PURPLE + "  │  " + BLUE + "Nacimiento: " + WHITE + nacimiento +
                "   " + BLUE + "Fallecimiento: " + WHITE + fallecimiento);
        if (!titulosLibros.isEmpty()) {
            System.out.println(PURPLE + "  │  " + BLUE + "Libros:     " + WHITE + String.join(", ", titulosLibros));
        }
        System.out.println(PURPLE + "  └─────────────────────────────────────────" + RESET);
    }
}
