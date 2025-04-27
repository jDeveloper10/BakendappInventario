# Backend de Inventario

Backend para la aplicación de inventario desarrollado con Spring Boot.

## Despliegue en Railway

### Requisitos previos

1. Cuenta en [Railway](https://railway.app/)
2. Cuenta en [GitHub](https://github.com/) (opcional para integración continua)

### Pasos para desplegar

1. Inicia sesión en [Railway](https://railway.app/)
2. Crea un nuevo proyecto desde el panel de control
3. Selecciona "Deploy from GitHub" o "Deploy from Template" (también puedes usar "Empty Project")
4. Conecta tu repositorio de GitHub o sube el código directamente
5. Railway detectará automáticamente el Procfile y system.properties

### Variables de entorno necesarias

Configura las siguientes variables de entorno en Railway:

Para el entorno de pruebas:
```
SPRING_PROFILES_ACTIVE=test
DATABASE_URL=jdbc:postgresql://[tu-bd-postgresql-url]:5432/inventario
DATABASE_USERNAME=[tu-usuario-bd]
DATABASE_PASSWORD=[tu-contraseña-bd]
JWT_SECRET=[tu-clave-secreta-para-jwt]
```

Para el entorno de producción:
```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://[tu-bd-postgresql-url]:5432/inventario
DATABASE_USERNAME=[tu-usuario-bd]
DATABASE_PASSWORD=[tu-contraseña-bd]
JWT_SECRET=[tu-clave-secreta-para-jwt]
```

### Base de datos

1. Dentro de tu proyecto en Railway, añade un nuevo servicio de PostgreSQL
2. Railway proporcionará automáticamente las variables de entorno para la conexión
3. Puedes copiar estas variables al servicio de tu backend

## Entornos disponibles

- **dev**: Entorno de desarrollo local
- **test**: Entorno de pruebas en Railway
- **prod**: Entorno de producción en Railway

## Usuarios por defecto

En los entornos de desarrollo y pruebas se crean automáticamente:

- Usuario administrador: 
  - Username: `admin`
  - Password: `admin123`

- Usuario regular:
  - Username: `usuario`
  - Password: `usuario123`