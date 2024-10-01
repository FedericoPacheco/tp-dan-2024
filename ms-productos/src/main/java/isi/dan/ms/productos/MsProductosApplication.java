package isi.dan.ms.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/*
 * Como usar JaCoCo:
 * 1) Ejecutar en línea de comandos: 
 * 		mvn clean test 
 * 		mvn jacoco:report
 * 2) Instalar la extensión "open in browser"
 * 3) Buscar el reporte en ./target/site/jacoco y hacer sobre index.html 
 * click derecho + "Open in default browser"
 * 
 * Como usar checkstyle:
 * 1) Ejecutar en línea de comandos:
 * 		mvn checkstyle:checkstyle
 * 2) Buscar el reporte ./target/site/checkstyle.xml y abrir en el browser
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy // AOP
public class MsProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProductosApplication.class, args);
	}

}
