# 🛠️ Home Repair

Plataforma web para conectar personas que necesitan reparaciones del hogar con profesionales de distintas especialidades.

<p align="center">
  <a href="https://truquinio.github.io/home-repair/">
    <img src="https://img.shields.io/badge/🚀_Live_Demo-GitHub_Pages-2ea44f?style=for-the-badge" alt="Live Demo"/>
  </a>
  <a href="https://github.com/truquinio/home-repair/actions/workflows/ci-pages.yml">
    <img src="https://github.com/trauquinio/home-repair/actions/workflows/ci-pages.yml/badge.svg" alt="CI and GitHub Pages"/>
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java_17-ED8B00?style=flat&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/GitHub_Pages-222222?style=flat&logo=githubpages&logoColor=white" alt="GitHub Pages"/>
  <img src="https://img.shields.io/badge/WCAG_2.2-AA-005A9C?style=flat" alt="WCAG 2.2 AA"/>
</p>

<p align="center">
  <a href="https://github.com/trauquinio">
    <img src="https://img.shields.io/badge/GitHub-trauquinio-181717?style=flat&logo=github&logoColor=white" alt="GitHub"/>
  </a>
  <a href="https://www.linkedin.com/in/federico-trucco/">
    <img src="https://img.shields.io/badge/LinkedIn-Federico_Trucco-0A66C2?style=flat&logo=linkedin&logoColor=white" alt="LinkedIn"/>
  </a>
</p>

---

## 🚀 Demo funcional

**https://truquinio.github.io/home-repair/**

La demo permite probar los principales flujos del producto directamente en el navegador:

- 👤 registro e inicio de sesión
- 🧑‍🔧 roles Cliente, Profesional y Administrador
- 🔎 búsqueda y filtrado de profesionales
- 🪪 perfiles profesionales
- 🧰 solicitudes de trabajo
- 🔄 cambios de estado
- ⭐ reseñas y valoraciones
- 🛡️ administración simulada

> [!NOTE]
> La demo utiliza almacenamiento local del navegador. La autenticación, autorización y persistencia de esta versión son simuladas y no sustituyen la seguridad ni la base de datos del backend Java original.

## 📸 Vista previa

![Home Repair - Inicio](docs/screenshots/home.png)

<details>
<summary><strong>Ver más capturas</strong></summary>

<br>

### 🔐 Inicio de sesión

![Home Repair - Inicio de sesión](docs/screenshots/login.png)

### 🔎 Profesionales

![Home Repair - Profesionales](docs/screenshots/professionals.png)

### 🧑‍🔧 Perfil profesional

![Home Repair - Perfil profesional](docs/screenshots/professional-profile.png)

### 🛡️ Administración

![Home Repair - Administración](docs/screenshots/admin.png)

</details>

## ✨ Funcionalidades

- 👥 Roles Cliente, Profesional y Administrador
- 🔐 Registro e inicio de sesión
- 🪪 Perfiles de clientes y profesionales
- 🔎 Búsqueda y filtrado por especialidad
- 🧰 Solicitudes de trabajo
- ✅ Aceptación, cancelación y finalización de trabajos
- ⭐ Reseñas y valoraciones
- 🛡️ Gestión administrativa de usuarios
- 🖼️ Carga y visualización de imágenes

## 🧱 Stack tecnológico

### Backend

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_2.7-6DB33F?style=flat&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)

### Frontend

![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-323330?style=flat&logo=javascript&logoColor=F7DF1E)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=bootstrap&logoColor=white)

### Datos y despliegue

![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat&logo=githubactions&logoColor=white)
![GitHub Pages](https://img.shields.io/badge/GitHub_Pages-222222?style=flat&logo=githubpages&logoColor=white)

## 🗂️ Estructura del proyecto

```text
.
├── src/                               # Aplicación Java/Spring Boot original
│   ├── main/java/com/egg/homerepair/ # Backend
│   ├── main/resources/
│   │   ├── static/                    # CSS, JS e imágenes
│   │   └── templates/                 # Vistas Thymeleaf
│   └── test/                          # Tests
├── demo/                              # Demo estática para GitHub Pages
│   ├── index.html
│   ├── styles.css
│   └── app.js
├── docs/                              # Documentación y capturas
├── .github/workflows/                 # CI y despliegue
└── pom.xml
```

## ▶️ Ejecutar el backend Java

### Requisitos

- Java 17
- MySQL
- Maven o Maven Wrapper

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

## 🌐 Ejecutar la demo localmente

La demo no requiere Node, Java ni MySQL, pero reutiliza las imágenes de la aplicación Java.

### Linux / macOS

```bash
rm -rf public
mkdir -p public/img
cp demo/index.html demo/styles.css demo/app.js public/
cp -R src/main/resources/static/img/. public/img/
python -m http.server 8080 --directory public
```

### Windows / PowerShell

```powershell
Remove-Item public -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force public/img | Out-Null
Copy-Item demo/index.html,demo/styles.css,demo/app.js public/
Copy-Item src/main/resources/static/img/* public/img/ -Recurse
python -m http.server 8080 --directory public
```

Después abre `http://localhost:8080`.

## 🔑 Cuentas demo

| Rol | Email | Contraseña |
| --- | --- | --- |
| Cliente | `cliente@homerepair.demo` | `demo123` |
| Profesional | `fontanero@homerepair.demo` | `demo123` |
| Administrador | `admin@homerepair.demo` | `demo123` |

Los datos se guardan en `localStorage` y pueden restaurarse desde el panel de Administración.

## ♿ UX/UI y accesibilidad

La demo y el frontend Java comparten criterios visuales y de interacción orientados a:

- navegación por teclado
- targets táctiles cómodos
- contraste suficiente
- formularios comprensibles
- diseño responsive
- soporte para `prefers-reduced-motion`

El objetivo de accesibilidad es **WCAG 2.2 AA**.

📘 Documentación: [`docs/UX_UI_ACCESSIBILITY.md`](docs/UX_UI_ACCESSIBILITY.md)

## 🧪 CI/CD

GitHub Actions ejecuta los tests Maven y valida el artefacto estático. En `main`, el mismo workflow prepara y despliega la demo mediante GitHub Pages.

[![CI and GitHub Pages](https://github.com/trauquinio/home-repair/actions/workflows/ci-pages.yml/badge.svg)](https://github.com/trauquinio/home-repair/actions/workflows/ci-pages.yml)

## 🔒 Seguridad

Las credenciales de base de datos no deben versionarse. El backend utiliza configuración externa mediante variables de entorno.

Las credenciales de la sección **Cuentas demo** son deliberadamente públicas porque pertenecen únicamente a la simulación frontend.

## 👨‍💻 Autor

**Federico Trucco**

[![GitHub](https://img.shields.io/badge/GitHub-trauquinio-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/trauquinio)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Federico_Trucco-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/federico-trucco/)
