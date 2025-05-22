# CRUD_ClientApp

Aplicación de escritorio desarrollada en Java con JavaFX que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una API REST. Este proyecto ha sido desarrollado como trabajo final del segundo curso del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAMi).

##  Autores

- [MeylinM](https://github.com/MeylinM)
- [olaialor](https://github.com/olaialor)
- [Elbirehl](https://github.com/Elbirehl)
- [iratisimon](https://github.com/iratisimon)

##  Tecnologías utilizadas

- Java 17  
- JavaFX  
- HTTP Client de Java  

>  El backend está desarrollado en Spring Boot con Hibernate. Puedes encontrarlo en:  
>  https://github.com/olaialor/CRUD_ServerApp

##  Estructura del proyecto

```
CRUD_ClientApp/
├── src/
│   └── eus/
│       └── tartanga/
│           └── crud/
│               ├── controller/
│               ├── model/
│               ├── service/
│               └── view/
├── resources/
│   └── fxml/
├── pom.xml
└── README.md
```

## Cómo ejecutar la aplicación

### 1. Clona el repositorio

```bash
git clone https://github.com/MeylinM/CRUD_ClientApp.git
cd CRUD_ClientApp
```

### 2. Abre el proyecto

Abre el proyecto directamente desde tu IDE favorito (NetBeans, IntelliJ, Eclipse, etc.) y ejecuta la clase principal.

### 3. Asegúrate de tener:

- Java 17 o superior instalado
- El backend en ejecución (ver siguiente sección)

## Comunicación con el backend

Esta aplicación se conecta con una API REST proporcionada por un servidor. El cliente utiliza `HttpClient` para enviar peticiones y recibir respuestas.

**Repositorio del servidor (API REST):**  
https://github.com/olaialor/CRUD_ServerApp

 **Importante:** Antes de ejecutar la aplicación cliente, asegúrate de que el servidor esté corriendo correctamente (por defecto en `http://localhost:8080`).


---

Proyecto realizado como parte del módulo de Desarrollo de Interfaces en el CIFP Tartanga LHII.
