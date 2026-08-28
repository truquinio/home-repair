# 🧰 Proyecto web app servicios / Web App Services Project

🗃 My <a href="https://github.com/truquinio/home-repair.git">Repository</a>
<!-- • Ir al <a href="https://truquinio.github.io/alura-challenge-1-Encriptador/">encriptador</a> 🔗 -->
🪪 My <a href="https://www.linkedin.com/in/federico-trucco/">LinkedIn</a> 

## 👷🏻‍♂️ Objetivo / Objective:

Tres barrios cerrados, de la localidad de Chacras de Coria en Mendoza, quieren armar un proveedor de contacto para servicios. Una aplicación que permite contactar a un proveedor de servicio (gasistas, plomeros, electricistas, etc) que carga su perﬁl con aquello que ofrece. Se podrá dejar comentarios y puntaje a cada perﬁl, además de contactarlo para que crezca la red de proveedores conﬁables.<br>

Three gated communities in the town of Chacras de Coria in Mendoza want to create a contact provider for services. An application that allows users to contact service providers (gas fitters, plumbers, electricians, etc.) who create their profiles with the services they offer. Users will be able to leave comments and ratings on each profile, as well as contact providers to expand the network of reliable service providers.

### ⚠️Problema / Problem:
Diﬁcultad para encontrar proveedores de servicios conﬁables y de forma inmediata.<br>
Difficulty in finding reliable service providers immediately.

### ✅Solución / Solution:
Brindar una lista de proveedores caliﬁcados por la experiencia de otros usuarios.<br>
Provide a list of providers rated based on the experience of other users.

### 👥 Audiencia / Audience:
Proveedores  de  servicios,  propietarios  o  residentes  de  los barrios privados que necesiten de algún proveedor.<br>
Service providers, owners, or residents of private communities in need of service providers.

## 🛠 Casos de uso / Use Cases:
• El administrador determinará los servicios que se pueden ofrecer y otorgar
roles.<br>
• Los  administradores  pueden  eliminar/censurar comentarios ofensivos, pero dejando la caliﬁcación.<br>
• El proveedor puede aceptar un trabajo o cancelarlo. También puede marcarlo como ﬁnalizado una vez que el trabajo haya sido aceptado.<br>
• El usuario puede pedir un servicio o cancelarlo. Una vez aceptado el trabajo puede cancelarlo o darlo por ﬁnalizado.<br>
• Una vez que el trabajo fue ﬁnalizado el usuario está habilitado a caliﬁcar el servicio, caliﬁcación que se agregará al perﬁl del proveedor.<br>
• El  usuario  puede  contactar  al  proveedor,  calcular  un  estimativo  de  los honorarios del proveedor por horas y caliﬁcarlo luego de prestado el servicio.

• The administrator will determine the services that can be offered and assign roles.<br>
• Administrators can remove/censor offensive comments while retaining the rating.<br>
• Providers can accept or cancel a job. They can also mark it as completed once the job has been accepted.<br>
• Users can request a service or cancel it. Once the job is accepted, they can cancel it or mark it as completed.<br>
• Once the job is completed, users can rate the service, and the rating will be added to the provider's profile.<br>
• Users can contact the provider, calculate an estimate of the provider's fees per hour, and rate them after the service has been provided.

## 🛡 Requerimientos obligatorios / Mandatory Requirements: 
**1-** Registro y Login con Spring Security<br>
**2-** Dos o más roles distintos para los usuarios<br>
**3-** Tabla html en alguna vista<br>
**4-** Carga y actualización de imagen<br>
**5-** Query de búsqueda personalizada<br>
**6-** CRUD<br>
**7-** Uno o más formularios.<br>
**8-** Tres o más vistas distintas.<br>
**9-** Diagrama UML de entidades<br>

**1-** Registration and login with Spring Security<br>
**2-** Two or more distinct user roles<br>
**3-** HTML table in at least one view<br>
**4-** Image upload and update<br>
**5-** Custom search query<br>
**6-** CRUD operations<br>
**7-** One or more forms<br>
**8-** Three or more distinct views<br>
**9-** UML entity diagram
## 🃏 Requerimientos optativos / Optional Requirements:
**10-** Motor de búsqueda<br>
**11-** Tabla de agregar / modiﬁcar y eliminar registros<br>
**12-** Atributos booleanos de alta y baja de usuarios (y poder modiﬁcarlos)<br>
**13-** Listado en tabla por ﬁltro (nombre, fechas, altas o bajas, etc)<br>
**14-** Implementen th:fragments en al menos cinco vistas distintas<br>
**15-** Dashboard de administración de la app (el rol Administrador tendrá acceso a información que un rol User o Guest no tiene)<br>
**16-** Aplicar principios de código limpio y buenas prácticas<br>
**17-** Añadir diagrama de casos de uso en UML

**10-** Search engine<br>
**11-** Table to add/modify and delete records<br>
**12-** Boolean attributes for user activation and deactivation (with the ability to modify them)<br>
**13-** Table listing with filters (by name, dates, activations or deactivations, etc.)<br>
**14-** Implement th:fragments in at least five different views<br>
**15-** Admin dashboard for the app (the Administrator role will have access to information not available to User or Guest roles)<br>
**16-** Apply clean code principles and best practices<br>
**17-** Add UML use case diagram

## 🔰 Requerimientos específicos / Specific Requirements:
**18-** La app cuenta inicialmente con la posibilidad de crear un perﬁl de **USUARIO QUE OFRECE SERVICIOS** (PROVEEDOR), o un perﬁl de **USUARIO QUE QUIERE CONTACTAR EL SERVICIO** (USER)<br>
**19-** La app muestra cada perﬁl con la cantidad de usuarios que lo contactaron y la cantidad  de  reseñas/puntaje/caliﬁcación  que  corresponde (sólo pueden "caliﬁcar a un **PROVEEDOR** aquellos **USER** que ya contactaron/contrataron el servicio de este perﬁl)<br>
**20-** Cada **PROVEEDOR** puede ser caliﬁcado por quienes hayan utilizado su servicio (esto lo decide el equipo, si con promedio entre 1/5 o con estrellas, etcétera)<br>
**21-** Un  **PROVEEDOR**  (gasista,  plomero,  etc)  genera  un perﬁl propio, con foto, contacto y descripción del servicio que ofrece<br>
**22-** Un **USER** puede acceder a la app, navegar en los servicios que quiera, y seleccionar a un proveedor de servicios para contactarlo o para caliﬁcarlo<br>
**23-** Un **GUEST** puede ver los servicios pero no puede ver la información de
contacto<br>
**24-** Un **ADMIN** puede otorgar permisos, cambiar roles, eliminar comentarios, crear nuevos servicios<br>
**25-** Los  **USERS**  deben  poder  encontrar  fácilmente a un proveedor por rubro y ordenarlo bajo distintos criterios<br>
**26-** Opcionalmente un USER puede elegir pasar su perﬁl a **PROVEEDOR**

**18-** The app initially allows creating a profile for a **USER WHO PROVIDES SERVICES** (PROVIDER), or a profile for a **USER WHO WANTS TO CONTACT A SERVICE** (USER)<br>
**19-** The app displays each profile with the number of users who contacted it and the corresponding number of reviews/ratings (only USERS who have already contacted/hired the services of a **PROVIDER** can rate them)<br>
**20-** Each PROVIDER can be rated by those who have used their service (the team decides whether to use an average rating between 1/5 or stars, etc.)<br>
**21-** A **PROVIDER** (gas fitter, plumber, etc.) creates their own profile with a photo, contact information, and a description of the service they offer<br>
**22-** A **USER** can access the app, browse the desired services, and select a service provider to contact or rate<br>
**23-** A **GUEST** can view the services but cannot view the contact information<br>
**24-** An **ADMIN** can grant permissions, change roles, delete comments, and create new services<br>
**25-** **USERS** should be able to easily find a provider by category and sort them by various criteria<br>
**26-** Optionally, a **USER** can choose to convert their profile to a **PROVIDER**

## 💻 Tech Stack:
![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=flat&logo=Java&logoColor=white) ![MySQL](https://img.shields.io/badge/-MySQL-4479A1.svg?style=flat&logo=MySQL&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=flat&logo=spring&logoColor=white) ![Thymeleaf](https://img.shields.io/badge/-Thymeleaf-darkgreen.svg?style=flat&logo=Thymeleaf&logoColor=white) ![Bootstrap](https://img.shields.io/badge/bootstrap-%23563D7C.svg?style=flat&logo=bootstrap&logoColor=white) ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=flat&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=flat&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=flat&logo=javascript&logoColor=%23F7DF1E) 

## 🎞 Screenshots:

![img1](https://i.ibb.co/kDqfLXC/01-Mi-marido-te-lo-hace-index1.png)
![img2](https://i.ibb.co/W0dyYcj/02-Mi-marido-te-lo-hace-register-user.png)
![img3](https://i.ibb.co/Pts6DgB/02-Mi-marido-te-lo-hace-login1.png)
![img4](https://i.ibb.co/wQr3kjB/04-Mi-marido-te-lo-hace-mi-perfil-User.png)
![img5](https://i.ibb.co/52xZ8pw/07-Mi-marido-te-lo-hace-mi-perfil-Provider-foto.png)
![img6](https://i.ibb.co/1LRrzsT/09-Mi-marido-te-lo-hace-Profesionales.png)
![img7](https://i.ibb.co/5rYX9QB/14-Mi-marido-te-lo-hace-Calificaci-n.png)
![img8](https://i.ibb.co/cTVRdcy/15-Mi-marido-te-lo-hace-puntuaci-n.png)
![img9](https://i.ibb.co/qgtq1hQ/16-Mi-marido-te-lo-hace-panel-admin.png)

## 👨🏻‍💻 Autor:

<b>Federico Trucco</b> | @truquinio

[![LinkedIn](https://img.shields.io/badge/LinkedIn-%230077B5.svg?logo=linkedin&logoColor=white)](https://linkedin.com/in/federico-trucco/) [![Discord](https://img.shields.io/badge/Discord-%237289DA.svg?logo=discord&logoColor=white)](https://discord.gg/https://discord.gg/zcRG3dSt)  [![Stack Overflow](https://img.shields.io/badge/-Stackoverflow-FE7A16?logo=stack-overflow&logoColor=white)](https://stackoverflow.com/users/317680) [![Twitch](https://img.shields.io/badge/Twitch-%239146FF.svg?logo=Twitch&logoColor=white)](https://twitch.tv/truquinio)
