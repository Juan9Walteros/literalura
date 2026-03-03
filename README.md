# 📚 LiterAlura - Catálogo de Libros

Aplicación de consola desarrollada en Java con Spring Boot que permite buscar y gestionar libros a través de la API Gutendex, almacenando los datos en una base de datos PostgreSQL.

## 🚀 Funcionalidades

- 📖 **Buscar libro por título** — Consulta la API de Gutendex y registra el libro en la BD
- 📚 **Listar libros registrados** — Muestra todos los libros guardados en la base de datos
- 👤 **Listar autores registrados** — Muestra todos los autores con sus libros
- 🗓️ **Autores vivos en un año** — Filtra autores que estaban vivos en un año determinado
- 🌍 **Libros por idioma** — Filtra libros por idioma (ES, EN, FR, PT)

## 🛠️ Tecnologías

- Java 17
- Spring Boot 3.2.4
- Spring Data JPA
- PostgreSQL
- API Gutendex (gutendex.com)

## ⚙️ Configuración

Configura las siguientes variables de entorno antes de ejecutar:

```
DB_HOST=localhost/nombre_base_datos
DB_NAME=literalura
DB_USER=tu_usuario
DB_PASSWORD=tu_password
```

## ▶️ Cómo ejecutar

```bash
./mvnw spring-boot:run
```
