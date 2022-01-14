package guru.springframework.msscbeerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilitamos ejecuciones asincronas y programadas y definimos un TaskExecutor para ejecutar tareas asincronas en otros
 * hilos
 *
 * @EnableAsync Lo necesitamos para que se ejecute el servicio BrewingService cada cierto intervalo de manera asincrona
 * @EnableScheduling Enables Spring's scheduled task execution capability
 */
@EnableAsync
@EnableScheduling
@Configuration
public class TaskConfig {

    @Bean
    TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor(); // Create a new SimpleAsyncTaskExecutor with default thread name prefix.
    }
}
