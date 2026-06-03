package isa.jima.ventas.validation.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Esta anotacion solo se podra poner sobre atributos de una clase
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) // Necesario para que la reflexion de java la pueda leer en tiempo de ejecucion
public @interface Uppercase {
}
