package com.literalura.literatura_console;

import com.literalura.literatura_console.model.Libro;
import com.literalura.literatura_console.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraturaConsoleApplication implements CommandLineRunner {

	@Autowired
	private LibroService servicio;

	private final Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaConsoleApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException, InterruptedException {
		System.out.println("📚 Bienvenido a Literalura - Tu biblioteca de libros de dominio público");
		System.out.println("=".repeat(70));

		boolean salir = false;
		while (!salir) {
			mostrarMenu();
			String opcion = sc.nextLine().trim();

			try {
				switch (opcion) {
					case "1" -> buscarLibroPorTitulo();
					case "2" -> listarLibrosGuardados();
					case "3" -> listarAutoresGuardados();
					case "4" -> listarAutoresVivosEnAnio();
					case "5" -> listarLibrosPorIdioma();
					case "0" -> {
						salir = true;
						System.out.println("👋 ¡Hasta pronto!");
					}
					default -> System.out.println("❌ Opción no válida. Por favor, elige una opción del menú.");
				}
			} catch (Exception e) {
				System.out.println("⚠️  Ocurrió un error inesperado: " + e.getMessage());
			}
			System.out.println(); // Espacio entre operaciones
		}
		sc.close();
	}

	private void mostrarMenu() {
		System.out.println("¿Qué deseas hacer?");
		System.out.println("1 - 🔍 Buscar libro por título");
		System.out.println("2 - 📚 Listar libros guardados");
		System.out.println("3 - 👤 Listar autores guardados");
		System.out.println("4 - 🕰️  Autores vivos en un año");
		System.out.println("5 - 🌍 Listar libros por idioma");
		System.out.println("0 - 🚪 Salir");
		System.out.print("👉 Elige una opción: ");
	}

	private void buscarLibroPorTitulo() throws IOException, InterruptedException {
		System.out.print("Ingrese el título del libro: ");
		String titulo = sc.nextLine().trim();
		if (titulo.isEmpty()) {
			System.out.println("⚠️  El título no puede estar vacío.");
			return;
		}

		servicio.buscarYGuardarPorTitulo(titulo)
				.ifPresentOrElse(
						libro -> System.out.println("✅ Guardado: \"" + libro.getTitulo() +
								"\" — " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "-")),
						() -> System.out.println("❌ No se encontró ningún libro con ese título en Gutendex.")
				);
	}

	private void listarLibrosGuardados() {
		List<Libro> libros = servicio.listarLibros();
		if (libros.isEmpty()) {
			System.out.println("📭 No hay libros guardados en la base de datos.");
			return;
		}
		System.out.println("📖 Libros guardados (" + libros.size() + "):");
		System.out.println("─".repeat(50));
		libros.forEach(libro -> System.out.printf(" • %-40s — %s%n",
				truncar(libro.getTitulo(), 40),
				libro.getAutor() != null ? libro.getAutor().getNombre() : "Sin autor"));
	}

	private void listarAutoresGuardados() {
		var autores = servicio.listarAutores();
		if (autores.isEmpty()) {
			System.out.println("📭 No hay autores guardados.");
			return;
		}
		System.out.println("👥 Autores guardados (" + autores.size() + "):");
		System.out.println("─".repeat(30));
		autores.forEach(autor -> System.out.println(" • " + autor.getNombre()));
	}

	private void listarAutoresVivosEnAnio() {
		System.out.print("Ingrese el año: ");
		String anioStr = sc.nextLine().trim();
		try {
			Integer anio = Integer.parseInt(anioStr);
			var autores = servicio.autoresVivosEnAnio(anio);
			if (autores.isEmpty()) {
				System.out.println("📭 No se encontraron autores vivos en el año " + anio + ".");
			} else {
				System.out.println("🕰️  Autores vivos en " + anio + " (" + autores.size() + "):");
				System.out.println("─".repeat(30));
				autores.forEach(autor -> System.out.println(" • " + autor.getNombre()));
			}
		} catch (NumberFormatException e) {
			System.out.println("❌ Por favor, ingrese un número válido.");
		}
	}

	private void listarLibrosPorIdioma() {
		System.out.println("🌍 Selecciona un idioma:");
		System.out.println("1 - 🇬🇧 Inglés (en)");
		System.out.println("2 - 🇪🇸 Español (es)");
		System.out.println("3 - 🇫🇷 Francés (fr)");
		System.out.println("4 - 🇮🇹 Italiano (it)");
		System.out.print("👉 Elige una opción: ");
		String idiomaOpt = sc.nextLine().trim();

		String codigo = switch (idiomaOpt) {
			case "1" -> "en";
			case "2" -> "es";
			case "3" -> "fr";
			case "4" -> "it";
			default -> null;
		};

		if (codigo == null) {
			System.out.println("❌ Opción no válida.");
			return;
		}

		String nombre = nombreIdioma(codigo);
		List<Libro> libros = servicio.librosPorIdioma(codigo);

		if (libros.isEmpty()) {
			System.out.println("📭 No se encontraron libros en " + nombre + ".");
		} else {
			System.out.println("📖 Libros en " + nombre + " (" + libros.size() + "):");
			System.out.println("─".repeat(50));
			libros.forEach(libro -> System.out.printf(" • %-40s — %s%n",
					truncar(libro.getTitulo(), 40),
					libro.getAutor() != null ? libro.getAutor().getNombre() : "Sin autor"));
		}
	}

	private String nombreIdioma(String codigo) {
		return switch (codigo) {
			case "en" -> "Inglés";
			case "es" -> "Español";
			case "fr" -> "Francés";
			case "it" -> "Italiano";
			default -> "Desconocido";
		};
	}

	// Utilidad para truncar títulos largos
	private String truncar(String texto, int longitud) {
		if (texto == null) return "Desconocido";
		return texto.length() > longitud ? texto.substring(0, longitud - 3) + "..." : texto;
	}
}