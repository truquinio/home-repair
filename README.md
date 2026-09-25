<p align="center">
<img src="https://readme-typing-svg.herokuapp.com?size=20&color=C9D1D9&vCenter=true&width=800&height=50&lines=Home+Repair;Plataforma+de+reparaciones+hogare%C3%B1as;Java+%7C+Spring+Boot+%7C+MySQL+%7C+Thymeleaf"/>
</p>

## 💫 Sobre el proyecto

Aplicación web full stack que conecta usuarios con proveedores de servicios del hogar (gasistas, plomeros, electricistas, albañiles, etc.). Los usuarios pueden buscar proveedores verificados, contactarlos, y dejar comentarios y puntajes que construyen una red de confianza.

> Desarrollado originalmente en equipo como trabajo final de la formación Full Stack (Egg / Universidad de Alicante). Este repositorio es una copia independiente mantenida y evolucionada por [Federico Trucco](https://github.com/truquinio).
<br><br>

## 📸 Capturas

<!-- Arrastrar aquí las imágenes de /capturas-home-repair (home.jpg, registro.jpg, admin.jpg, lista-perfiles.jpg) -->
<br><br>

## ✨ Funcionalidades

- **Roles diferenciados**: Guest, User, Proveedor y Admin, cada uno con permisos distintos
- **Registro y login** con Spring Security
- **Perfil de proveedor**: foto, contacto, servicio ofrecido, carga y actualización de imagen
- **Sistema de contacto y calificación**: solo puede calificar a un proveedor quien ya lo contrató
- **Búsqueda de proveedores** por rubro, con motor de búsqueda personalizado
- **Panel de administración**: gestión de usuarios, roles, moderación de comentarios y alta de servicios
- **CRUD completo** sobre proveedores, servicios y comentarios
<br><br>

## 🛠️ Stack técnico

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F.svg?style=flat&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?style=flat&logo=mysql&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-%23E34F26.svg?style=flat&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-%231572B6.svg?style=flat&logo=css3&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3.svg?style=flat&logo=bootstrap&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-%23323330.svg?style=flat&logo=javascript&logoColor=%23F7DF1E)
![Maven](https://img.shields.io/badge/Maven-C71A36.svg?style=flat&logo=apachemaven&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032.svg?style=flat&logo=git&logoColor=white)
<br><br>

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
<br><br>

## 🧑‍💻 Autor

[![GitHub](https://img.shields.io/badge/GitHub-181717.svg?style=flat&logo=github&logoColor=white)](https://github.com/truquinio) [![LinkedIn](https://img.shields.io/badge/LinkedIn-%230077B5.svg?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/federico-trucco/)
