# Apuntes del Curso de ReactiveJ

Enlace al curso: [Develop a Reactive Java Microservice with ReactiveJ](https://www.udemy.com/course/develop-a-reactive-java-microservice-with-reactivej/learn/lecture/12449446#overview)

## Preguntas

### ¿Qué diferencia a la programación reactiva de la programación imperativa y síncrona?

En informática, la programación reactiva es un paradigma de programación declarativo relacionado con los flujos de datos y la propagación del cambio. Con este paradigma, es posible expresar fácilmente flujos de datos estáticos (p. Ej., Matrices) o dinámicos (p. Ej., Emisores de eventos), y también comunicar que existe una dependencia inferida dentro del modelo de ejecución asociado, lo que facilita la propagación automática de los datos modificados.

Por ejemplo, en una configuración de programación imperativa, `a := b + c` significaría que a `a` se le asigna el resultado de `b + c` en el instante en que se evalúa la expresión. Después, los valores de `b` y `c` pueden cambiar sin afectar el valor de `a`.

Por otro lado, en la programación reactiva, el valor de `a` se actualiza automáticamente siempre que los valores de `b` o `c` cambien, sin que el programa tenga que volver a ejecutar la declaración `a := b + c` para determinar el valor asignado actualmente de `a`.

### ¿Qué es un servidor web incorporado?

*(Sin respuesta en el texto original)*

### ¿Cuáles son las principales ventajas de usar programación reactiva?

- Variables actualizadas a lo largo del tiempo.

### ¿Qué es reactiveJ?

*(Sin respuesta en el texto original)*

### ¿Con ReactiveJ solo puede desarrollar aplicaciones totalmente reactivas?

*(Sin respuesta en el texto original)*

## Ejemplos de cURL para Endpoints

Asegúrate de que la aplicación se esté ejecutando en `localhost:8383`.

### Crear un ToDo

**Bash:**
```bash
curl -X POST -H "Content-Type: application/json" -d '{"title":"Mi Nuevo ToDo", "description":"Una descripción para mi nuevo todo"}' http://localhost:8383/api/v1/create
```
**CMD**
```cmd
curl -X POST -H "Content-Type: application/json" -d "{\"title\":\"Mi Nuevo ToDo\", \"description\":\"Una descripción para mi nuevo todo\"}" http://localhost:8383/api/v1/create
```

### Leer un ToDo específico

Reemplaza `your-todo-id` con el ID real de un ToDo.
```bash
curl http://localhost:8383/api/v1/read/your-todo-id
```

### Leer todos los ToDos (o usuarios)

Este endpoint actualmente devuelve una lista de usuarios de la base de datos.
```bash
curl http://localhost:8383/api/v1/read
```

### Actualizar un ToDo

Reemplaza `your-todo-id` con el ID real del ToDo que deseas actualizar.
```bash
curl -X POST -H "Content-Type: application/json" -d '{"id":"your-todo-id", "title":"Título Actualizado", "description":"Descripción actualizada"}' http://localhost:8383/api/v1/update
```

### Eliminar un ToDo

Reemplaza `your-todo-id` con el ID real del ToDo que deseas eliminar.
```bash
curl http://localhost:8383/api/v1/delete/your-todo-id
```

### actualizar maven
>- mvnw -N io.takari:maven:0.7.7:wrapper -Dmaven=3.9.6


###
>- mvnw clean install
>- mvnw exec:java



# Proyecto Apache Kafka con Java 21

Este proyecto demuestra cómo usar Apache Kafka con Java 21, incluyendo características modernas como Virtual Threads.

### curso kafka
>- https://www.udemy.com/course/kafka-cluster-deployment-and-java-springboot/

## Requisitos Previos

1. Java 21 o superior
2. Maven 3.6+
3. Docker y Docker Compose (opcional, para Kafka local)
4. Git
5. modificar el server.properties la sección log.dirs=C:/kafka_2.13-2.8.0/data/kafka-logs
## Configuración del Proyecto

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd kafka-java21-project
```

### probar endpoint
>- http://localhost:8080/api/publish/T4/blog

####
>- sb-nb6fj46885103@personal.example.com
>- ly3^tOQ0