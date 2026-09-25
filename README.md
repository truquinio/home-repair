# 🏠 Home Repair — Plataforma de Reparaciones Hogareñas

Aplicación web full stack que conecta usuarios con proveedores de servicios del hogar (gasistas, plomeros, electricistas, albañiles, etc.) en zonas residenciales. Los usuarios pueden buscar proveedores verificados, contactarlos, y dejar comentarios y puntajes que construyen una red de confianza.

> Proyecto desarrollado originalmente en equipo como trabajo final de la formación Full Stack (Egg / Universidad de Alicante). Este repositorio es una copia independiente mantenida y evolucionada por [Federico Trucco](https://github.com/truquinio).

<br>

## 📸 Capturas

<!-- Arrastrar aquí las imágenes de /capturas-home-repair (home.jpg, registro.jpg, admin.jpg, lista-perfiles.jpg) -->

<br>

## 🎯 Objetivo

Brindar una lista de proveedores calificados por la experiencia de otros usuarios, facilitando el proceso de encontrar profesionales confiables para las tareas del hogar.

## 🌟 Audiencia

Proveedores de servicios del hogar, y propietarios o residentes que necesiten contratar alguno.

<br>

## ✨ Funcionalidades

- **Roles diferenciados**: Guest, User, Proveedor y Admin, cada uno con permisos distintos.
- **Registro y login** con Spring Security.
- **Perfil de proveedor**: foto, contacto, servicio ofrecido, carga y actualización de imagen.
- **Sistema de contacto y calificación**: un usuario solo puede calificar a un proveedor que ya contrató; el proveedor acumula reseñas y puntaje.
- **Búsqueda de proveedores** por rubro, con motor de búsqueda personalizado.
- **Panel de administración**: gestión de usuarios, cambio de roles, moderación de comentarios y alta de nuevos servicios.
- **CRUD completo** sobre proveedores, servicios y comentarios.

<br>

## 🛠️ Stack técnico

<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="40" alt="spring" />
  <img width="12" />
  <img src="https://www.thymeleaf.org/images/thymeleaf.png" height="40" alt="thymeleaf" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" height="40" alt="mysql" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg" height="40" alt="javascript" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/html5/html5-original.svg" height="40" alt="html5" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/css3/css3-original.svg" height="40" alt="css3" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/bootstrap/bootstrap-original.svg" height="40" alt="bootstrap" />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" height="40" alt="git" />
</div>

- **Backend**: Java 17, Spring Boot 2.7.10, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap
- **Base de datos**: MySQL
- **Herramientas**: Maven, Git, Postman

<br>

## 🗂️ Estructura del proyecto

```
src/main/java/com/egg/MiMaridoTeLoHace/
├── controllers/    # Controladores Spring: rutas HTTP
├── entities/        # Entidades JPA (Proveedor, Usuario, etc.)
├── enums/           # Roles y constantes
├── exceptions/       # Excepciones personalizadas
├── repositories/     # Repositorios JPA
├── security/         # Configuración de Spring Security
├── services/         # Lógica de negocio
└── converters/        # Mapeo entre entidades y DTOs

src/main/resources/
├── static/           # CSS, JS y recursos estáticos
└── templates/        # Vistas Thymeleaf
```

<br>

## 🚀 Instalación local

```bash
git clone https://github.com/truquinio/home-repair.git
cd home-repair
./mvnw spring-boot:run
```

Requiere Java 17 y una instancia de MySQL configurada en `application.properties`.

<br>

## 🧑‍💻 Autor

[Federico Trucco](https://github.com/truquinio) — [LinkedIn](https://www.linkedin.com/in/federico-trucco/)
