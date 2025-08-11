package com.literalura.literatura_console.service;

import com.literalura.literatura_console.client.GutendexClient;
import com.literalura.literatura_console.dto.GutendexAuthorDTO;
import com.literalura.literatura_console.dto.GutendexBookDTO;
import com.literalura.literatura_console.model.Autor;
import com.literalura.literatura_console.model.Libro;
import com.literalura.literatura_console.repository.LibroRepository;
import com.literalura.literatura_console.repository.AutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private GutendexClient client;

    @Autowired
    private LibroRepository libroRepo;

    @Autowired
    private AutorRepository autorRepo;

    /**
     * Busca un libro por título en Gutendex, lo guarda en la base de datos y lo devuelve.
     * Evita duplicados usando el externalId (ID de Gutendex).
     */
    public Optional<Libro> buscarYGuardarPorTitulo(String titulo) throws IOException, InterruptedException {
        Optional<GutendexBookDTO> dtoOpt = client.buscarPorTitulo(titulo);
        if (dtoOpt.isEmpty()) return Optional.empty();
        GutendexBookDTO dto = dtoOpt.get();

        // Evitar duplicado por externalId
        Optional<Libro> existente = libroRepo.findByExternalId(dto.getId());
        if (existente.isPresent()) return existente;

        // Mapear autor (usamos el primer autor)
        GutendexAuthorDTO aDto = dto.getAuthors() != null && !dto.getAuthors().isEmpty()
                ? dto.getAuthors().get(0) : null;
        Autor autor = null;
        if (aDto != null) {
            autor = autorRepo.findByNombreIgnoreCase(aDto.getName())
                    .orElseGet(() -> {
                        Autor nuevoAutor = new Autor();
                        nuevoAutor.setNombre(aDto.getName());
                        nuevoAutor.setBirthYear(aDto.getBirthYear());
                        nuevoAutor.setDeathYear(aDto.getDeathYear());
                        return autorRepo.save(nuevoAutor);
                    });
        }

        Libro libro = new Libro();
        libro.setExternalId(dto.getId());
        libro.setTitulo(dto.getTitle());
        libro.setIdioma(dto.getLanguages() != null && !dto.getLanguages().isEmpty() ? dto.getLanguages().get(0) : null);
        libro.setNumeroDescargas(dto.getDownloadCount() != null ? dto.getDownloadCount().longValue() : 0L);
        libro.setAutor(autor);

        return Optional.of(libroRepo.save(libro));
    }

    /**
     * Lista todos los libros almacenados en la base de datos.
     */
    public List<Libro> listarLibros() {
        return libroRepo.findAll();
    }

    /**
     * Lista todos los autores almacenados en la base de datos.
     */
    public List<Autor> listarAutores() {
        return autorRepo.findAll();
    }

    /**
     * Busca libros por idioma (ej: "es", "en", "fr").
     */
    public List<Libro> librosPorIdioma(String idioma) {
        return libroRepo.findByIdioma(idioma);
    }

    /**
     * Devuelve los autores que estaban vivos durante un año específico.
     * Incluye autores que nacieron antes o en ese año y murieron después o en ese año,
     * o que aún no han muerto (deathYear = null).
     */
    public List<Autor> autoresVivosEnAnio(Integer anio) {
        return autorRepo.autoresVivosEnAnio(anio);
    }
}