package isi.dan.msclientes.dao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.containers.PostgreSQLContainer;

import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertTrue;

/*
* General:
* https://docs.spring.io/spring-boot/reference/testing/testcontainers.html
* https://java.testcontainers.org/modules/databases/jdbc/
* https://www.youtube.com/watch?v=erp-7MCK5BU
* https://testcontainers.com/modules/mysql/
*
* Proyectos probados con mysql y postres. Todos tienen distintas anotaciones:
* https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
* https://danielme.com/2023/04/13/testing-spring-boot-docker-with-testcontainers-and-junit-5-mysql-and-other-images/
* https://github.com/ivangfr/springboot-testing-mysql
*/

@Slf4j
@ActiveProfiles("db") // Usar application-db.properties
//@DataJpaTest // Usar solo componentes de JPA
@SpringBootTest // Carga todo el "application context". Hace funcionar mvn test, necesario para jacoco
@Testcontainers // Usar db de prueba dentro de container docker
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Desactivar db H2 que usa springboot por defecto
public class ObraRepositoryTest {

    Obra obra1, obra2, obra3;

    @Container
    @ServiceConnection
    //private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine");
    private static MySQLContainer<?> container = new MySQLContainer<>("mysql:latest");

    @Autowired
    private ObraRepository obraRepository;

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @BeforeEach
    void beforeEach() {
        obraRepository.deleteAll();
        
        obra1 = new Obra();
        obra1.setDireccion("Juan del Campillo 3047");
        obra1.setPresupuesto(BigDecimal.valueOf(100.0));
        obra1.setLatitud(BigDecimal.valueOf(-31.628810));
        obra1.setLongitud(BigDecimal.valueOf(-60.707567));
        obra1.setEstado(EstadoObra.PENDIENTE);
        
        obra2 = new Obra();
        obra2.setDireccion(obra1.getDireccion());
        obra2.setPresupuesto(obra1.getPresupuesto());
        obra2.setLatitud(obra1.getLatitud());
        obra2.setLongitud(obra1.getLongitud());
        obra2.setEstado(EstadoObra.HABILITADA);

        obra3 = new Obra();
        obra3.setDireccion(obra1.getDireccion());
        obra3.setPresupuesto(obra1.getPresupuesto());
        obra3.setLatitud(obra1.getLatitud());
        obra3.setLongitud(obra1.getLongitud());
        obra3.setEstado(EstadoObra.FINALIZADA);
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void saveFindByIdAndDelete() {
        Obra obra1Db = obraRepository.save(obra1);
        obra1.setId(obra1Db.getId());
        
        log.info("Obra: " + obra1.toString());
        log.info("Obra DB: " + obra1Db.toString());

        assertTrue(obra1.equals(obra1Db));

        obraRepository.deleteById(obra1.getId());
        List<Obra> obras = obraRepository.findAll();

        assertTrue(obras.isEmpty());
    }

    @Test 
    void findByEstado() {
        obraRepository.save(obra1);  
        obraRepository.save(obra2);    
        obraRepository.save(obra3);

        log.info("Obras: " + obraRepository.findAll().toString());

        List<Obra> obrasPendientes = obraRepository.findByEstado(EstadoObra.PENDIENTE);
        List<Obra> obrasHabilitadas = obraRepository.findByEstado(EstadoObra.HABILITADA);
        List<Obra> obrasFinalizadas = obraRepository.findByEstado(EstadoObra.FINALIZADA);

        log.info("Obras pendientes: " + obrasPendientes.toString());
        log.info("Obras habilitadas: " + obrasHabilitadas.toString());
        log.info("Obras finalizadas: " + obrasFinalizadas.toString());
        
        assertTrue(obrasPendientes.size() == 1);
        assertTrue(obrasHabilitadas.size() == 1);
        assertTrue(obrasFinalizadas.size() == 1);
    }
}

