package isi.dan.ms.clientes.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Tratar de no usar. Genera demasiados logs
    /* 
        *                       -> cualquier retorno
        isi.dan.ms.clientes..    -> cualquier subpaquete de isi.dan.ms.pedidos 
        *                       -> cualquier clase
        .*                      -> cualquier metodo
        (..)                    -> cualquier cantidad y tipo de argumentos
     */
    @Pointcut("execution(* isi.dan.ms.clientes..*.*(..))")
    public void allMethods() {}

    @Pointcut("execution(public * isi.dan.ms.clientes.controller.*.*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(public * isi.dan.ms.clientes.service.*.*(..))")
    public void serviceMethods() {}
    
    @Around("controllerMethods() || serviceMethods()")
    private Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        
        Object[] args = joinPoint.getArgs();
        String nombreClase = joinPoint.getSignature().getClass().getSimpleName();
        String nombreMetodo = joinPoint.getSignature().getName();
        log.info("Entrando:\n\tclase: {}; \n\tmetodo: {}; \n\tparametros: {}", nombreClase, nombreMetodo, Arrays.toString(args));
        
        Object resultado = joinPoint.proceed();
        log.info("Saliendo:\n\tclase: {}; \n\tmetodo: {}; \n\tparametros: {}; \n\tresultado: {}", nombreClase, nombreMetodo, Arrays.toString(args), resultado);
        
        return resultado;
    }
}
