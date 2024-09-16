# Spring Boot User API

Esta aplicación expone una API RESTful para la creación de usuarios. Está desarrollada con Spring Boot y dockerizada para facilitar su despliegue.

## Requisitos previos

- Git
- Docker
- cURL (para probar la API)

## Instalación y ejecución del servicio

1. Clona el repositorio:
   ```
   git clone https://github.com/lucasleon2107/smartjob-assessment
   ```

2. Navega al directorio del proyecto:
   ```
   cd smartjob-assessment
   ```

3. Asegúrate de tener Docker instalado y en ejecución en tu sistema.

4. Construye la imagen Docker con el siguiente comando:
   ```
   docker build -t user-api .
   ```

5. Una vez construida la imagen, ejecuta el contenedor con:
   ```
   docker run -p 8080:8080 user-api
   ```

   Esto iniciará la aplicación y la hará accesible en `http://localhost:8080`.

## Documentación de la API

Una vez que el servicio esté en ejecución, puedes acceder a la documentación completa de la API a través de Swagger UI en la siguiente URL:

```
http://localhost:8080/swagger-ui/index.html
```

## Probando el endpoint de registro

Para probar el endpoint de registro de usuarios, puedes usar el siguiente comando cURL:

```bash
curl --request POST \
  --url http://localhost:8080/users \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "Test",
  "email": "test@test.com",
  "password": "password",
  "phones": [
    {
      "number": "123",
      "cityCode": "1",
      "countryCode": "1"
    }
  ]
}'
```

Este comando enviará una solicitud POST al endpoint `/users` con los datos de un nuevo usuario.

## Respuesta esperada

En caso de éxito, la API responderá con un código de estado HTTP 200 y un JSON con el siguiente formato:

```json
{
  "id": "4f4760f3-6d17-44a6-a26c-5db4f58baab5",
  "created": "2024-09-16T08:03:02.920966",
  "modified": "2024-09-16T08:03:02.920981",
  "lastLogin": "2024-09-16T08:03:03.121587",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNzI2NDkxNzgzLCJleHAiOjE3MjY0OTUzODN9.y8Vs1Oc3NaVRoA6cx734tbvWCQCEpnF760WQO6p6OFc",
  "isActive": true
}
```

Donde:
- `id`: Es un UUID que identifica al usuario.
- `created`: Fecha y hora de creación del usuario.
- `modified`: Fecha y hora de la última modificación del usuario.
- `lastLogin`: Fecha y hora del último ingreso del usuario.
- `token`: JWT (JSON Web Token) para la autenticación del usuario.
- `isActive`: Booleano que indica si el usuario está activo en el sistema.

## Notas adicionales

- Todos los endpoints aceptan y retornan únicamente JSON, incluso para los mensajes de error.
- Los mensajes de error siguen el formato: `{"message": "mensaje de error"}`.
- Si el correo ya está registrado en la base de datos, se retornará un error.
- La contraseña es validada mediante una expresión regular configurable.


## Diagrama de secuencia del servicio
```mermaid
sequenceDiagram
    participant Cliente
    participant UserController
    participant AuthenticationService
    participant UserRepository
    participant PasswordEncoder
    participant AuthenticationManager
    participant JwtService
    participant Base de Datos

    Cliente->>UserController: POST /users (UserRequest)
    UserController->>AuthenticationService: signUp(userRequest)
    AuthenticationService->>UserRepository: findByEmail(email)
    UserRepository->>Base de Datos: SELECT
    Base de Datos-->>UserRepository: Resultado
    
    alt Email ya existe
        UserRepository-->>AuthenticationService: Usuario encontrado
        AuthenticationService-->>UserController: Lanza UserAlreadyExistsException
        UserController-->>Cliente: Error 409: Conflict - Usuario ya existe
    else Email no existe
        UserRepository-->>AuthenticationService: Optional.empty()
        AuthenticationService->>PasswordEncoder: encode(password)
        PasswordEncoder-->>AuthenticationService: Contraseña encriptada
        AuthenticationService->>AuthenticationService: createUserFromRequest()
        AuthenticationService->>UserRepository: save(newUser)
        UserRepository->>Base de Datos: INSERT
        Base de Datos-->>UserRepository: Usuario guardado
        UserRepository-->>AuthenticationService: Usuario creado
        AuthenticationService->>AuthenticationManager: authenticate()
        AuthenticationManager-->>AuthenticationService: Autenticación exitosa
        AuthenticationService->>JwtService: generateToken(savedUser)
        JwtService-->>AuthenticationService: Token JWT
        AuthenticationService->>AuthenticationService: updateUserWithTokenAndLastLogin()
        AuthenticationService->>UserRepository: save(updatedUser)
        UserRepository->>Base de Datos: UPDATE
        Base de Datos-->>UserRepository: Usuario actualizado
        AuthenticationService->>UserController: UserResponse
        UserController-->>Cliente: 201 Created: UserResponse (con token)
    end
   ```

## Diagrama del flujo del servicio
```mermaid
flowchart TD
    A[Inicio] --> B{Recibir solicitud POST /users}
    B --> C{Validar email existente}
    C -->|Email existe| D[Lanzar UserAlreadyExistsException]
    D --> E[Retornar Error 409: Conflict]
    C -->|Email no existe| F[Encriptar contraseña]
    F --> G[Crear nuevo usuario]
    G --> H[Guardar usuario en BD]
    H --> I[Autenticar usuario]
    I --> J[Generar token JWT]
    J --> K[Actualizar usuario con token y último login]
    K --> L[Crear UserResponse]
    L --> M[Retornar 201 Created con UserResponse]
    E --> N[Fin]
    M --> N[Fin]
```