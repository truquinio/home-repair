<div align="center">

# 🛠️ Home Repair

**Marketplace web para conectar personas que necesitan reparaciones del hogar con profesionales de distintas especialidades.**

[![Live Demo](https://img.shields.io/badge/Live%20Demo-GitHub%20Pages-2ea44f?style=flat&logo=githubpages&logoColor=white)](https://truquinio.github.io/home-repair/)
[![CI and Pages](https://github.com/truquinio/home-repair/actions/workflows/ci-pages.yml/badge.svg)](https://github.com/truquinio/home-repair/actions/workflows/ci-pages.yml)
![Java 17](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)

[**Abrir demo**](https://truquinio.github.io/home-repair/) · [**Ver código**](https://github.com/truquinio/home-repair) · [**UX/UI y accesibilidad**](docs/UX_UI_ACCESSIBILITY.md)

</div>

---

## Qué demuestra el proyecto

Home Repair combina un backend Java/Spring Boot con una demo estática navegable para mostrar el flujo de producto sin exigir una instalación local.

La aplicación modela tres perfiles principales —**Cliente, Profesional y Administrador**— y cubre búsqueda de profesionales, solicitudes de trabajo, cambios de estado y valoraciones.

> [!NOTE]
> La demo de GitHub Pages usa almacenamiento local del navegador. Su autenticación, autorización y persistencia son simuladas; no sustituyen Spring Security ni MySQL del backend original.

## 📸 Vista previa

<p align="center">
  <img src="docs/screenshots/home.png" alt="Home Repair — pantalla principal" width="90%"/>
</p>

<details>
<summary><strong>Ver más capturas</strong></summary>

<br/>

| Acceso | Profesionales |
| --- | --- |
| ![Inicio de sesión](docs/screenshots/login.png) | ![Listado de profesionales](docs/screenshots/professionals.png) |

| Perfil profesional | Administración |
| --- | --- |
| ![Perfil profesional](docs/screenshots/professional-profile.png) | ![Panel de administración](docs/screenshots/admin.png) |

</details>

## ✨ Funcionalidades

- Registro e inicio de sesión.
- Roles Cliente, Profesional y Administrador.
- Perfiles de clientes y profesionales.
- Búsqueda y filtrado por especialidad.
- Solicitudes de trabajo y cambios de estado.
- Reseñas y valoraciones.
- Gestión administrativa de usuarios.
- Carga y visualización de imágenes.

## 🧱 Arquitectura y stack

| Área | Tecnologías |
| --- | --- |
| Backend | Java 17 · Spring Boot 2.7.18 · Spring Security · Spring Data JPA |
| Vistas | Thymeleaf · HTML · CSS · JavaScript · Bootstrap |
| Persistencia | MySQL · Hibernate/JPA |
| Build | Maven |
| Demo | HTML/CSS/JavaScript · localStorage |
| CI/CD | GitHub Actions · GitHub Pages |

```text
.
├── src/
│   ├── main/java/com/egg/homerepair/
│   ├── main/resources/
│   │   ├── static/
│   │   └── templates/
│   └── test/
├── demo/
├── docs/
├── .github/workflows/
└── pom.xml
```

## ▶️ Ejecutar el backend

### Requisitos

- Java 17
- MySQL
- Maven Wrapper incluido

Configura la conexión mediante variables de entorno:

```text
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

## 🌐 Demo local

La demo no necesita Java ni MySQL.

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

## 🔑 Cuentas de la demo

| Rol | Email | Contraseña |
| --- | --- | --- |
| Cliente | `cliente@homerepair.demo` | `demo123` |
| Profesional | `fontanero@homerepair.demo` | `demo123` |
| Administrador | `admin@homerepair.demo` | `demo123` |

Estas credenciales pertenecen únicamente a la simulación frontend.

## ♿ UX/UI y accesibilidad

El proyecto documenta criterios de navegación por teclado, contraste, formularios, targets táctiles, responsive design y `prefers-reduced-motion`.

El objetivo declarado es **WCAG 2.2 AA**; no se presenta como una certificación independiente.

📘 [Documentación UX/UI y accesibilidad](docs/UX_UI_ACCESSIBILITY.md)

## 🧪 CI/CD

El workflow `.github/workflows/ci-pages.yml` ejecuta la verificación del proyecto y gestiona el despliegue de la demo en GitHub Pages.

## 🔒 Seguridad

Las credenciales reales de base de datos no deben versionarse. El backend usa configuración externa para la conexión.

---

**Federico Trucco / [@truquinio](https://github.com/truquinio)** · [LinkedIn](https://www.linkedin.com/in/federico-trucco/)
