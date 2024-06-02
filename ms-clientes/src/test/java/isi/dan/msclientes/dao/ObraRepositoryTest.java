package isi.dan.msclientes.dao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertTrue;

// https://docs.spring.io/spring-boot/reference/testing/testcontainers.html
// https://java.testcontainers.org/modules/databases/jdbc/
// https://www.youtube.com/watch?v=erp-7MCK5BU


@ActiveProfiles("db") // Usar application-db.properties
//@ExtendWith(SpringExtension.class) // Spring es cargado para tests
//@DataJpaTest // Usar solo componentes de JPA
@SpringBootTest
@Testcontainers // Usar db mysql de prueba dentro de container docker
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Desactivar db H2 que usa springboot por defecto
public class ObraRepositoryTest {

    Obra obra;
    Logger log = LoggerFactory.getLogger(ObraRepositoryTest.class);

    //@SuppressWarnings("resource")
    @Container
    //@ServiceConnection
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.36");
    //        .withDatabaseName("testdb")
    //        .withUsername("test")
    //        .withPassword("test");

    @Autowired
    private ObraRepository obraRepository;

    
    // Pasar credenciales (en application-db.properties estan con ${...})
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
    

    @BeforeAll
    static void startContainer() {
        //mysqlContainer.start();
    }

    @BeforeEach
    void iniciarDatos() {

        

        obraRepository.deleteAll();
        
        obra = new Obra();
        obra.setDireccion("Juan del Campillo 3047");
        obra.setPresupuesto(BigDecimal.valueOf(100.0));
        obra.setLatitud(BigDecimal.valueOf(-31.628810));
        obra.setLongitud(BigDecimal.valueOf(-60.707567));
    }

    @AfterAll
    static void stopContainer() {
        //mysqlContainer.close(); // ?? 
        //mysqlContainer.stop();
    }

    @Test
    void saveFindByIdAndDelete() {
        obra = obraRepository.save(obra);
        Obra obraDb = obraRepository.findById(obra.getId()).get();
        
        log.info(obra.toString());
        log.info(obraDb.toString());

        assertTrue(obra.equals(obraDb));

        obraRepository.deleteById(obra.getId());
        List<Obra> obras = obraRepository.findAll();

        assertTrue(obras.isEmpty());
    }

    @Test 
    void findByEstado() {
        obra.setEstado(EstadoObra.PENDIENTE);
        obraRepository.save(obra);

        obra.setEstado(EstadoObra.HABILITADA);
        obraRepository.save(obra);

        obra.setEstado(EstadoObra.FINALIZADA);
        obraRepository.save(obra);

        List<Obra> obrasPendientes = obraRepository.findByEstado(EstadoObra.PENDIENTE);
        List<Obra> obrasHabilitadas = obraRepository.findByEstado(EstadoObra.HABILITADA);
        List<Obra> obrasFinalizadas = obraRepository.findByEstado(EstadoObra.FINALIZADA);

        log.info(obrasPendientes.toString());
        log.info(obrasHabilitadas.toString());
        log.info(obrasFinalizadas.toString());

        assertTrue(obrasPendientes.size() == 1);
        assertTrue(obrasHabilitadas.size() == 1);
        assertTrue(obrasFinalizadas.size() == 1);
    }
}

