package de.htwberlin.kba.gr7.vocabduel.configuration_core;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@EnableTransactionManagement
public class EntityFactoryManagement {

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("VocabduelJPA_PU");
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    // TODO Implement => any useful cod below?
    // TODO If running, adjust tests

    //    @Bean
    //    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    //        LocalContainerEntityManagerFactoryBean em
    //                = new LocalContainerEntityManagerFactoryBean();
    //        em.setDataSource(dataSource());
    //        em.setPackagesToScan(new String[]{
    //                "user_administration.export",
    //                "user_administration",
    //                "game_administration.export",
    //                "vocabulary_administration.export"
    //        });
    //        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    //        em.setJpaVendorAdapter(vendorAdapter);
    //        PersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
    //        em.setPersistenceProvider(persistenceProvider);
    //        em.setJpaProperties(additionalProperties());
    //
    //        return em;
    //    }
    //
    //    @Bean
    //    public DataSource dataSource() {
    //        DriverManagerDataSource dataSource = new DriverManagerDataSource();
    //        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    //        dataSource.setUrl("jdbc:mysql://localhost:3306/vocabduel?createDatabaseIfNotExist=true&relaxAutoCommit=true");
    //        dataSource.setUsername("root");
    //        dataSource.setPassword("Test#-#44");
    //        return dataSource;
    //    }
    //
    //    Properties additionalProperties() {
    //        Properties properties = new Properties();
    //        properties.setProperty("hibernate.hbm2ddl.auto", "update");
    //        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    //        properties.setProperty("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.WeblogicJtaPlatform");
    //        return properties;
    //    }

    //    @Bean
    //    @Autowired
    //    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
    //        PlatformTransactionManager txManager = new ();
    //        txManager.setSessionFactory(sessionFactory);
    //        return txManager;
    //    }
}
