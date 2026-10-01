# Home Repair

Plataforma web para conectar personas que necesitan reparaciones del hogar con profesionales de distintos rubros.

**Autor y desarrollador:** [Federico Trucco](https://github.com/truquinio)

## Live Demo

La demo funcional estÃ¡ publicada en:

**https://truquinio.github.io/home-repair/**

Permite probar los principales flujos del producto directamente en el navegador: registro, login demo, roles Customer/Provider/Admin, bÃºsqueda de profesionales, perfiles, solicitudes de trabajo, cambios de estado, reviews, ratings y administraciÃ³n simulada.

> La demo utiliza almacenamiento local del navegador. La autenticaciÃ³n, autorizaciÃ³n y persistencia de esta versiÃ³n son simuladas y no sustituyen la seguridad ni la base de datos del backend Java original.

## Proyecto original

La aplicaciÃ³n original es un proyecto full stack desarrollado con:

- Java 17
- Spring Boot 2.7
- Spring Security
- Spring Data JPA / Hibernate
- Thymeleaf
- MySQL
- HTML5, CSS3 y JavaScript
- Bootstrap
- Maven

### Funcionalidades

- Roles Customer, Provider y Admin
- Registro e inicio de sesiÃ³n
- Perfiles de clientes y proveedores
- BÃºsqueda y filtrado de profesionales por rubro
- Solicitudes de trabajo
- AceptaciÃ³n, cancelaciÃ³n y finalizaciÃ³n de trabajos
- Reviews y ratings
- GestiÃ³n administrativa de usuarios
- Carga y visualizaciÃ³n de imÃ¡genes

## Estructura

```text
.
â”œâ”€â”€ src/                         # AplicaciÃ³n Java/Spring Boot original
â”‚   â”œâ”€â”€ main/java/               # Backend
â”‚   â”œâ”€â”€ main/resources/
â”‚   â”‚   â”œâ”€â”€ static/              # CSS, JS e imÃ¡genes
â”‚   â”‚   â””â”€â”€ templates/           # Vistas Thymeleaf
â”‚   â””â”€â”€ test/                    # Tests
â”œâ”€â”€ demo/                        # Demo estÃ¡tica para GitHub Pages
â”‚   â”œâ”€â”€ index.html
â”‚   â”œâ”€â”€ styles.css
â”‚   â””â”€â”€ app.js
â”œâ”€â”€ .github/workflows/           # CI y despliegue
â””â”€â”€ pom.xml
```

## Ejecutar el backend Java

### Requisitos

- Java 17
- MySQL
- Maven, o utilizar el Maven Wrapper incluido

Configura las variables de entorno:

```bash
DB_URL=jdbc:mysql://localhost:3306/home_repair
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
```

DespuÃ©s:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Ejecutar la demo localmente

La demo no requiere Node, Java ni MySQL. Puede servirse con cualquier servidor HTTP estÃ¡tico.

Por ejemplo:

```bash
python -m http.server 8080 --directory demo
```

y abrir `http://localhost:8080`.

### Cuentas demo

| Rol | Email | ContraseÃ±a |
| --- | --- | --- |
| Customer | `cliente@homerepair.demo` | `demo123` |
| Provider | `plomero@homerepair.demo` | `demo123` |
| Admin | `admin@homerepair.demo` | `demo123` |

Los datos se guardan en `localStorage` y pueden restaurarse desde el panel Admin.

## CI/CD

GitHub Actions ejecuta los tests Maven y valida el artefacto estÃ¡tico. En `main`, el mismo workflow prepara y despliega la demo mediante GitHub Pages.

## Seguridad

Las credenciales de base de datos no deben versionarse. El backend utiliza configuraciÃ³n externa mediante variables de entorno.

Las credenciales incluidas en la secciÃ³n de cuentas demo son deliberadamente pÃºblicas porque pertenecen Ãºnicamente a la simulaciÃ³n frontend.

## Autor

**Federico Trucco**

- GitHub: https://github.com/truquinio
- LinkedIn: https://www.linkedin.com/in/federico-trucco/

