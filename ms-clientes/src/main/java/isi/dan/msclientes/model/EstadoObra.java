package isi.dan.msclientes.model;

/* Soluciones probadas:
 * Agregar espacio de almacenamiento en la DB (funciona): https://stackoverflow.com/a/73996763
 * Usar anotaciones de Hibernate 6 o de libreria que extiende a Hibernate (pre versión 6): https://stackoverflow.com/a/46303099 
 * 
 * No probadas:
 * Simplemente usar una clase en lugar de enum: https://www.geeksforgeeks.org/enum-in-java/
 * 
 * General: https://www.baeldung.com/jpa-persisting-enums-in-jpa
 */

public enum EstadoObra {
    HABILITADA,//("Habilitada"),
    PENDIENTE,//("Pendiente"),
    FINALIZADA;//("Finalizada");

    /*
    private String str;

    EstadoObra(String str) {
        this.str = str;
    }
    */
}
