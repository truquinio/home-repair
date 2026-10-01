# Home Repair

Plataforma web para conectar personas que necesitan reparaciones del hogar con profesionales de distintas especialidades.

**Autor y desarrollador:** [Federico Trucco](https://github.com/truquinio)

## Live Demo

La demo funcional está publicada en:

**https://truquinio.github.io/home-repair/**

Permite probar los principales flujos del producto directamente en el navegador: registro, login demo, roles Cliente/Profesional/Administrador, búsqueda de profesionales, perfiles, solicitudes de trabajo, cambios de estado, reseñas, valoraciones y administración simulada.

> La demo utiliza almacenamiento local del navegador. La autenticación, autorización y persistencia de esta versión son simuladas y no sustituyen la seguridad ni la base de datos del backend Java original.

## Capturas actuales

Las siguientes capturas se generan a partir de la demo funcional actual.

### Inicio y zona de trabajo

![Home Repair - Inicio](docs/screenshots/home.png)

### Inicio de sesión

![Home Repair - Inicio de sesión](docs/screenshots/login.png)

### Profesionales

![Home Repair - Profesionales](docs/screenshots/professionals.png)

### Perfil profesional

![Home Repair - Perfil profesional](docs/screenshots/professional-profile.png)

### Administración

![Home Repair - Administración](docs/screenshots/admin.png)

## Proyecto original

La aplicación original es un proyecto full stack desarrollado con:

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

- Roles Cliente, Profesional y Administrador
- Registro e inicio de sesión
- Perfiles de clientes y profesionales
- Búsqueda y filtrado de profesionales por especialidad
- Solicitudes de trabajo
- Aceptación, cancelación y finalización de trabajos
- Reseñas y valoraciones
- Gestión administrativa de usuarios
- Carga y visualización de imágenes

## Estructura

```text
.
├── src/                         # Aplicación Java/Spring Boot original
│   ├── main/java/com/egg/homerepair/ # Backend
│   ├── main/resources/
│   │   ├── static/              # CSS, JS e imágenes
│   │   └── templates/           # Vistas Thymeleaf
│   └── test/                    # Tests
├── demo/                        # Demo estática para GitHub Pages
│   ├── index.html
│   ├── styles.css
│   └── app.js
├── .github/workflows/           # CI y despliegue
└── pom.xml
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

Después:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Ejecutar la demo localmente

La demo no requiere Node, Java ni MySQL, pero el artefacto publicado reutiliza las imágenes de la aplicación Java. Para reproducir localmente el mismo artefacto que GitHub Pages:

```bash
rm -rf public
mkdir -p public/img
cp demo/index.html demo/styles.css demo/app.js public/
cp -R src/main/resources/static/img/. public/img/
python -m http.server 8080 --directory public
```

En PowerShell:

```powershell
Remove-Item public -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force public/img | Out-Null
Copy-Item demo/index.html,demo/styles.css,demo/app.js public/
Copy-Item src/main/resources/static/img/* public/img/ -Recurse
python -m http.server 8080 --directory public
```

Después abre `http://localhost:8080`.

### Cuentas demo

| Rol | Email | Contraseña |
| --- | --- | --- |
| Cliente | `cliente@homerepair.demo` | `demo123` |
| Profesional | `fontanero@homerepair.demo` | `demo123` |
| Administrador | `admin@homerepair.demo` | `demo123` |

Los datos se guardan en `localStorage` y pueden restaurarse desde el panel de Administración.

## CI/CD

GitHub Actions ejecuta los tests Maven y valida el artefacto estático. En `main`, el mismo workflow prepara y despliega la demo mediante GitHub Pages.

## Seguridad

Las credenciales de base de datos no deben versionarse. El backend utiliza configuración externa mediante variables de entorno.

Las credenciales incluidas en la sección de cuentas demo son deliberadamente públicas porque pertenecen únicamente a la simulación frontend.

## Autor

**Federico Trucco**

- GitHub: https://github.com/truquinio
- LinkedIn: https://www.linkedin.com/in/federico-trucco/
