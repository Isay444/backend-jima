package isa.jima.ventas.validation.anotaciones;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.lang.reflect.Field;

public class UpperCaseListener {

    @PrePersist
    @PreUpdate
    public void convertirAMayusculas(Object entidad){
        // OBtener todos los atributos privados de la entidad que se esta guardando
        for (Field field: entidad.getClass().getDeclaredFields()){
            // Si el atributo tiene la noatcion @Uppercase y es de tipo String
            if (field.isAnnotationPresent(Uppercase.class) && field.getType() == String.class){
                try {
                    field.setAccessible(true); // Ignorar el private
                    String valor = (String) field.get(entidad); // Obtener el valor actual
                    if (valor != null){
                        field.set(entidad, valor.trim().toUpperCase()); // Mayusculas
                    }
                }catch (IllegalAccessException e){
                    // Si falla, no romper lo guardado, solo logueamos
                    System.err.println("Error al aplicar @Uppercase en campo: "+ field.getName());
                }
            }
        }
    }
}
