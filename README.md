# Home Repair

Plataforma web para conectar personas que necesitan reparaciones del hogar con profesionales de distintos rubros.

**Autor y desarrollador:** [Federico Trucco](https://github.com/truquinio)

## Live Demo

La demo funcional está publicada en:

**https://truquinio.github.io/home-repair/**

Permite probar los principales flujos del producto directamente en el navegador: registro, login demo, roles Customer/Provider/Admin, búsqueda de profesionales, perfiles, solicitudes de trabajo, cambios de estado, reviews, ratings y administración simulada.

> La demo utiliza almacenamiento local del navegador. La autenticación, autorización y persistencia de esta versión son simuladas y no sustituyen la seguridad ni la base de datos del backend Java original.

## Capturas

<p align="center">
  <img width="620" alt="Home Repair - inicio" src="https://github.com/user-attachments/assets/3f6f8d3c-a556-4f53-bc34-3113d18d333f" />
  <img width="620" alt="Home Repair - registro" src="https://github.com/user-attachments/assets/256be1c6-26c1-4451-9518-c74f16805e87" />
  <img width="620" alt="Home Repair - administración" src="https://github.com/user-attachments/assets/8383a2de-c682-499d-a053-579ac071276e" />
  <img width="620" alt="Home Repair - perfiles" src="https://github.com/user-attachments/assets/1f6043df-f8a4-425e-94ac-23a498f3efd1" />
</p>

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

- Roles Customer, Provider y Admin
- Registro e inicio de sesión
- Perfiles de clientes y proveedores
- Búsqueda y filtrado de profesionales por rubro
- Solicitudes de trabajo
- Aceptación, cancelación y finalización de trabajos
- Reviews y ratings
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
| Customer | `cliente@homerepair.demo` | `demo123` |
| Provider | `plomero@homerepair.demo` | `demo123` |
| Admin | `admin@homerepair.demo` | `demo123` |

Los datos se guardan en `localStorage` y pueden restaurarse desde el panel Admin.

## CI/CD

GitHub Actions ejecuta los tests Maven y valida el artefacto estático. En `main`, el mismo workflow prepara y despliega la demo mediante GitHub Pages.

## Seguridad

Las credenciales de base de datos no deben versionarse. El backend utiliza configuración externa mediante variables de entorno.

Las credenciales incluidas en la sección de cuentas demo son deliberadamente públicas porque pertenecen únicamente a la simulación frontend.

## Autor

**Federico Trucco**

- GitHub: https://github.com/truquinio
- LinkedIn: https://www.linkedin.com/in/federico-trucco/
