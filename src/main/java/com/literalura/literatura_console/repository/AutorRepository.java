package com.literalura.literatura_console.repository;

import com.literalura.literatura_console.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    // Consulta personalizada: autores vivos en un año dado
    @Query("SELECT a FROM Autor a WHERE a.birthYear <= :anio AND (a.deathYear IS NULL OR a.deathYear >= :anio)")
    List<Autor> autoresVivosEnAnio(@Param("anio") Integer anio);
}