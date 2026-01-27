# Sistema de Gestión de Inventario, Ventas y Usuarios

## 1. Introducción

Este repositorio contiene la **documentación completa y el backend** del *Sistema de Gestión de Inventario, Ventas y Usuarios*, un proyecto enfocado **exclusivamente en el backend**, diseñado bajo principios de **Arquitectura Limpia**, **Seguridad por Diseño (Security by Design)** y **Desarrollo Guiado por Pruebas (TDD)**.

El objetivo principal del proyecto es centralizar el control de inventario de una sucursal, gestionar ventas y abonos de clientes, y asegurar el acceso al sistema mediante roles y autenticación de doble factor (2FA).

Este repositorio está estructurado para que **la documentación sea la base del desarrollo**, permitiendo que cualquier desarrollador (o agente de IA) entienda claramente las reglas antes de escribir una sola línea de código.

---

## 2. Objetivos del Proyecto

* Centralizar el control de stock de productos en una única API backend.
* Gestionar ventas y abonos de clientes de forma segura y auditable.
* Implementar control de acceso por roles (RBAC).
* Garantizar trazabilidad completa mediante logs de auditoría.
* Aplicar TDD como metodología obligatoria de desarrollo.
* Definir reglas claras de arquitectura y estilo desde el inicio.

---

## 3. Alcance Funcional

El sistema permitirá:

* Gestión de usuarios con roles:

  * Administrador
  * Gestor
  * Vendedor
* Registro y autenticación de usuarios con 2FA.
* Gestión de productos mediante ID y/o QR.
* Control de inventario (entradas y salidas).
* Registro de ventas.
* Gestión de abonos y saldos de clientes.
* Auditoría completa de todas las operaciones críticas.

---

## 4. Stack Tecnológico (Backend)

| Capa                    | Tecnología                             |
| ----------------------- | -------------------------------------- |
| Backend principal       | Java 21 – Spring Boot 4.0.1            |
| Servicios auxiliares    | No definidos (fuera de alcance actual) |
| Base de datos           | PostgreSQL 16 (Docker local)           |
| Seguridad               | Spring Security, 2FA (TOTP)            |
| Testing                 | JUnit 5, Mockito, Testcontainers       |
| Gestión de dependencias | Maven / Gradle                         |

---

## 5. Metodología de Desarrollo

### 5.1 Desarrollo Guiado por Pruebas (TDD)

El desarrollo sigue estrictamente el ciclo:

1. **Red** – Escribir la prueba que falla.
2. **Green** – Implementar el mínimo código para pasar la prueba.
3. **Refactor** – Mejorar el código manteniendo las pruebas verdes.

> No se permite código en producción sin pruebas asociadas.

### 5.2 Metodología Ágil (SCRUM)

* Historias de usuario claras y trazables.
* Criterios de aceptación en formato Gherkin.
* Backlog priorizado por valor y riesgo.
* Incrementos pequeños y verificables.

---

## 6. Seguridad (Principios Fundamentales)

Este proyecto adopta **Security by Design** y **Security by Default**:

* Acceso denegado por defecto.
* Contraseñas hasheadas (BCrypt/Argon2).
* Autenticación multifactor obligatoria.
* Separación estricta de roles.
* Validación de permisos en cada caso de uso.
* Logs de auditoría inmutables.

---

## 7. Estructura del Repositorio (Backend)

```text
inventory_system_backend/
├── README.md
├── docs/
│   ├── architecture.md
│   ├── project_plan.md
│   ├── security.md
│   ├── tdd_strategy.md
│   ├── coding_guidelines.md
│   ├── api_contracts.md
│   └── diagrams/
├── src/
│   ├── main/
│   │   ├── java/com/inventory/management/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   └── test/java/com/inventory/management/
├── docker-compose.yml
├── pom.xml
└── .gitignore
```

---

## 8. Documentación del Proyecto

Toda la documentación vive en la carpeta `/docs`:

| Archivo              | Descripción                                    |
| -------------------- | ---------------------------------------------- |
| architecture.md      | Arquitectura del sistema y decisiones técnicas |
| project_plan.md      | Roadmap, historias de usuario y milestones     |
| security.md          | Modelo de seguridad, roles y amenazas          |
| tdd_strategy.md      | Estrategia de pruebas y cobertura              |
| coding_guidelines.md | Reglas de estilo y buenas prácticas            |
| api_contracts.md     | Contratos REST y DTOs                          |

---

## 9. Reglas del Proyecto (Obligatorias)

* No se permite código sin pruebas.
* No se permite acceso directo a repositorios desde controladores.
* Toda acción sensible debe generar un log de auditoría.
* Los roles no se validan en el frontend, solo en backend.
* La documentación es parte del entregable.

---

## 10. Estado del Proyecto (Backend)

**Fase actual:** Backend – Diseño de arquitectura y base de datos

El desarrollo de código comenzará **únicamente** cuando:

* Todas las historias de usuario estén definidas.
* Los criterios de aceptación estén completos.
* La arquitectura esté validada.
* Las reglas de estilo estén aprobadas.

---

## 11. Público Objetivo

Este proyecto está orientado a:

* Desarrolladores backend Java/Spring.
* Estudiantes de ingeniería de software.
* Equipos que deseen aprender arquitectura limpia, seguridad y TDD en backend.

---

## 12. Licencia

Este proyecto se distribuye bajo licencia MIT (pendiente de confirmación).

---

## 13. Nota Final

Este repositorio prioriza **calidad, seguridad y mantenibilidad** sobre velocidad. Si algo no está documentado, **no debe implementarse**.
