# 📚 Literalura: Tu biblioteca de libros de dominio público

**Literalura** es una aplicación de consola desarrollada con **Spring Boot** y **Java** que permite buscar y almacenar libros obtenidos de la API [Gutendex](https://gutendex.com/), la cual provee metadatos de libros de dominio público de [Project Gutenberg](https://www.gutenberg.org/).

Esta aplicación fue creada como parte del reto **"Practicando Spring Boot Challenge"** de Alura Latam.

---

## 🎯 Funcionalidades

- 🔍 Buscar un libro por título en Gutendex y guardarlo en una base de datos.
- 📚 Listar todos los libros guardados.
- 👤 Listar todos los autores registrados.
- 🕰️ Mostrar autores que estaban vivos en un año específico.
- 🌍 Filtrar libros por idioma (español, inglés, francés, italiano, etc.).
- 💾 Almacenamiento persistente usando **JPA** y **PostgreSQL** (o H2 en memoria).

---

## 🛠️ Tecnologías utilizadas

- **Java 23**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **PostgreSQL** (opcionalmente **H2 en memoria**)
- **Maven** (gestión de dependencias)
- **HttpClient** (para consumir la API)
- **Jackson** (mapeo JSON)

---

## 📦 Estructura del proyecto
![img.png](img.png)


---

## 🚀 Cómo ejecutar el proyecto

### 1. Prerrequisitos
- Java 17 o superior (recomendado Java 23)
- Maven
- (Opcional) PostgreSQL instalado y configurado

### 2. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/literalura.git
cd literalura

## ✍️ Autor

**Carlos Galdamez **  
💼 Desarrollador Backend en formación  
📧 [cmgg1984@gmail.com]  
🌐 [Tu perfil de LinkedIn](https://www.linkedin.com/in/avcelsalvador/) | [GitHub](https://github.com/CharlyAldo/)
