package es.emilio.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, es.emilio.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, es.emilio.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, es.emilio.domain.User.class.getName());
            createCache(cm, es.emilio.domain.Authority.class.getName());
            createCache(cm, es.emilio.domain.User.class.getName() + ".authorities");
            createCache(cm, es.emilio.domain.Region.class.getName());
            createCache(cm, es.emilio.domain.Country.class.getName());
            createCache(cm, es.emilio.domain.Location.class.getName());
            createCache(cm, es.emilio.domain.Department.class.getName());
            createCache(cm, es.emilio.domain.Department.class.getName() + ".employees");
            createCache(cm, es.emilio.domain.Task.class.getName());
            createCache(cm, es.emilio.domain.Task.class.getName() + ".jobs");
            createCache(cm, es.emilio.domain.Employee.class.getName());
            createCache(cm, es.emilio.domain.Employee.class.getName() + ".jobs");
            createCache(cm, es.emilio.domain.Job.class.getName());
            createCache(cm, es.emilio.domain.Job.class.getName() + ".tasks");
            createCache(cm, es.emilio.domain.JobHistory.class.getName());
            createCache(cm, es.emilio.domain.Empleado.class.getName());
            createCache(cm, es.emilio.domain.Departamento.class.getName());
            createCache(cm, es.emilio.domain.Departamento.class.getName() + ".empleados");
            createCache(cm, es.emilio.domain.Departamento.class.getName() + ".tareas");
            createCache(cm, es.emilio.domain.Empresa.class.getName());
            createCache(cm, es.emilio.domain.Empresa.class.getName() + ".departaments");
            createCache(cm, es.emilio.domain.Tarea.class.getName());
            createCache(cm, es.emilio.domain.Tarea.class.getName() + ".departamentos");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
