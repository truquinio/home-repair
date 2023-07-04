# 🧰 Proyecto web app servicios 

• Visita mi <a href="https://github.com/truquinio/home-repair.git">repositorio</a> 🗃
<!-- • Ir al <a href="https://truquinio.github.io/alura-challenge-1-Encriptador/">encriptador</a> 🔗 -->
• Mi perfil de <a href="https://www.linkedin.com/in/federico-trucco/">LinkedIn</a> 🪪

## 👷🏻‍♂️ Objetivo:

Tres barrios cerrados, de la localidad de Chacras de Coria en Mendoza, quieren armar un proveedor de contacto para servicios. Una aplicación que permite contactar a un proveedor de servicio (gasistas, plomeros, electricistas, etc) que carga su perﬁl con aquello que ofrece. Se podrá dejar comentarios y puntaje a cada perﬁl, además de contactarlo para que crezca la red de proveedores conﬁables.

### ⚠️Problema
Diﬁcultad para encontrar proveedores de servicios conﬁables y de forma inmediata.

### ✅Solución
Brindar una lista de proveedores caliﬁcados por la experiencia de otros usuarios.

### 👥 Audiencia
Proveedores  de  servicios,  propietarios  o  residentes  de  los barrios privados que necesiten de algún proveedor.


## 🛠 Casos de uso
• El administrador determinará los servicios que se pueden ofrecer y otorgar
roles.<br>
• Los  administradores  pueden  eliminar/censurar comentarios ofensivos, pero dejando la caliﬁcación.<br>
• El proveedor puede aceptar un trabajo o cancelarlo. También puede marcarlo como ﬁnalizado una vez que el trabajo haya sido aceptado.<br>
• El usuario puede pedir un servicio o cancelarlo. Una vez aceptado el trabajo puede cancelarlo o darlo por ﬁnalizado.<br>
• Una vez que el trabajo fue ﬁnalizado el usuario está habilitado a caliﬁcar el servicio, caliﬁcación que se agregará al perﬁl del proveedor.<br>
• El  usuario  puede  contactar  al  proveedor,  calcular  un  estimativo  de  los honorarios del proveedor por horas y caliﬁcarlo luego de prestado el servicio.

## 🛡 Requerimientos obligatorios: 
**1-** Registro y Login con Spring Security<br>
**2-** Dos o más roles distintos para los usuarios<br>
**3-** Tabla html en alguna vista<br>
**4-** Carga y actualización de imagen<br>
**5-** Query de búsqueda personalizada<br>
**6-** CRUD<br>
**7-** Uno o más formularios.<br>
**8-** Tres o más vistas distintas.<br>
**9-** Diagrama UML de entidades

## 🃏 Requerimientos optativos:
**10-** Motor de búsqueda<br>
**11-** Tabla de agregar / modiﬁcar y eliminar registros<br>
**12-** Atributos booleanos de alta y baja de usuarios (y poder modiﬁcarlos)<br>
**13-** Listado en tabla por ﬁltro (nombre, fechas, altas o bajas, etc)<br>
**14-** Implementen th:fragments en al menos cinco vistas distintas<br>
**15-** Dashboard de administración de la app (el rol Administrador tendrá acceso a información que un rol User o Guest no tiene)<br>
**16-** Aplicar principios de código limpio y buenas prácticas<br>
**17-** Añadir diagrama de casos de uso en UML

## 🔰 Requerimientos específicos:
**18-** La app cuenta inicialmente con la posibilidad de crear un perﬁl de USUARIO QUE OFRECE SERVICIOS (PROVEEDOR), o un perﬁl de USUARIO QUE QUIERE CONTACTAR EL SERVICIO (USER)<br>
**19-** La app muestra cada perﬁl con la cantidad de usuarios que lo contactaron y la cantidad  de  reseñas/puntaje/caliﬁcación  que  corresponde (sólo pueden "caliﬁcar a un PROVEEDOR" aquellos USER que ya contactaron/contrataron el servicio de este perﬁl)<br>
**20-** Cada PROVEEDOR puede ser caliﬁcado por quienes hayan utilizado su servicio (esto lo decide el equipo, si con promedio entre 1/5 o con estrellas, etcétera)<br>
**21-** Un  PROVEEDOR  (gasista,  plomero,  etc)  genera  un perﬁl propio, con foto, contacto y descripción del servicio que ofrece<br>
**22-** Un USER puede acceder a la app, navegar en los servicios que quiera, y
seleccionar a un proveedor de servicios para contactarlo o para caliﬁcarlo<br>
**23-** Un GUEST puede ver los servicios pero no puede ver la información de
contacto<br>
**24-** Un ADMIN puede otorgar permisos, cambiar roles, eliminar comentarios, crear
nuevos servicios<br>
**25-** Los  USER  deben  poder  encontrar  fácilmente a un proveedor por rubro y ordenarlo bajo distintos criterios<br>
**26-** Opcionalmente un USER puede elegir pasar su perﬁl a PROVEEDOR


## 💻 Tech Stack:
![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=flat&logo=Java&logoColor=white) ![MySQL](https://img.shields.io/badge/-MySQL-4479A1.svg?style=flat&logo=MySQL&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=flat&logo=spring&logoColor=white) ![Thymeleaf](https://img.shields.io/badge/-Thymeleaf-darkgreen.svg?style=flat&logo=Thymeleaf&logoColor=white) ![Bootstrap](https://img.shields.io/badge/bootstrap-%23563D7C.svg?style=flat&logo=bootstrap&logoColor=white) ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=flat&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=flat&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=flat&logo=javascript&logoColor=%23F7DF1E) 

## 🎞 Screenshot:

![img1](https://i.ibb.co/J3g91Wp/01-Mi-marido-te-lo-hace-index-1.jpg)
![img2](https://i.ibb.co/gvGwHXy/02-Mi-marido-te-lo-hace-register-user-1.jpg)
![img3](https://i.ibb.co/K5nv2xw/02-Mi-marido-te-lo-hace-login-1.jpg)
![img4](https://i.ibb.co/McbfzwT/04-Mi-marido-te-lo-hace-mi-perfil-User-1.jpg)
![img5](https://i.ibb.co/Ptw3MRy/07-Mi-marido-te-lo-hace-mi-perfil-Provider-foto-1.jpg)
![img6](https://i.ibb.co/yhtgBf4/09-Mi-marido-te-lo-hace-Profesionales-1.jpg)
![img7](https://i.ibb.co/4prxtnf/10-Mi-marido-te-lo-hace-plomero-1.jpg)
![img8](https://i.ibb.co/zmt03qD/14-Mi-marido-te-lo-hace-Calificaci-n-1.jpg)
![img9](https://i.ibb.co/ZXH0jRm/15-Mi-marido-te-lo-hace-puntuaci-n-1.jpg)
![img10](https://i.ibb.co/wQ9qT6j/16-Mi-marido-te-lo-hace-panel-admin-1.jpg)

## 👨🏻‍💻 Autor:

<b>Federico Trucco</b> | @truquinio 

[![LinkedIn](https://img.shields.io/badge/LinkedIn-%230077B5.svg?logo=linkedin&logoColor=white)](https://linkedin.com/in/federico-trucco/) [![Discord](https://img.shields.io/badge/Discord-%237289DA.svg?logo=discord&logoColor=white)](https://discord.gg/https://discord.gg/zcRG3dSt)  [![Stack Overflow](https://img.shields.io/badge/-Stackoverflow-FE7A16?logo=stack-overflow&logoColor=white)](https://stackoverflow.com/users/317680) [![Twitch](https://img.shields.io/badge/Twitch-%239146FF.svg?logo=Twitch&logoColor=white)](https://twitch.tv/truquinio)