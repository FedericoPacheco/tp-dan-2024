package isi.dan.ms_productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 * Como usar JaCoCo:
 * 1) Ejecutar en línea de comandos: 
 * mvn clean test 
 * mvn jacoco:report
 * 2) Instalar la extensión "open in browser"
 * 3) Buscar el reporte en ./target/site/jacoco y hacer sobre index.html 
 * click derecho + "Open in default browser"
 */

@SpringBootApplication
@EnableDiscoveryClient
public class MsProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProductosApplication.class, args);
	}

}
