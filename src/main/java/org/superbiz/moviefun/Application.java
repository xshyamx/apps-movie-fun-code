package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    DatabaseServiceCredentials databaseServiceCredentials(
            @Value("${vcap.services}") String vcapServices
    ) {
        return new DatabaseServiceCredentials(vcapServices);
    }


    @Bean
    public DataSource albumsDataSource(
            DatabaseServiceCredentials serviceCredentials
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql", "p-mysql"));
        return dataSource;
    }

    @Bean
    public DataSource moviesDataSource(
            DatabaseServiceCredentials serviceCredentials
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql", "p-mysql"));
        return dataSource;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        jpaAdapter.setGenerateDdl(true);
        jpaAdapter.setShowSql(true);
        jpaAdapter.setDatabasePlatform(MySQLDialect.class.getName());
//        jpaAdapter.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "create");
//        jpaAdapter.getJpaPropertyMap().put("hibernate.id.new_generator_mappings", "false");
//        jpaAdapter.getJpaPropertyMap().put("org.hibernate.SQL", "true");
        return jpaAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsEntityManager(
            HibernateJpaVendorAdapter jpaVendorAdapter,
            @Qualifier("albumsDataSource") DataSource albumsDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setDataSource(albumsDataSource);
        em.setPackagesToScan("org.superbiz.moviefun.albums");
        em.setPersistenceUnitName("albums");

        return em;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityManager(
            HibernateJpaVendorAdapter jpaVendorAdapter,
            @Qualifier("moviesDataSource") DataSource moviesDataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setDataSource(moviesDataSource);
        em.setPackagesToScan("org.superbiz.moviefun.movies");
        em.setPersistenceUnitName("movies");
        return em;
    }

    @Bean
    public PlatformTransactionManager albumsTransactionManager(
            @Qualifier("albumsEntityManager") EntityManagerFactory albumsEntityManager
    ) {
        PlatformTransactionManager tm = new JpaTransactionManager(albumsEntityManager);
        return tm;
    }

    @Bean
    public PlatformTransactionManager moviesTransactionManager(
            @Qualifier("moviesEntityManager") EntityManagerFactory moviesTransactionManager
    ) {
        PlatformTransactionManager tm = new JpaTransactionManager(moviesTransactionManager);
        return tm;
    }

    @Bean
    public TransactionTemplate moviesTransactionTemplate(
            @Qualifier("moviesTransactionManager") PlatformTransactionManager moviesTransactionManager
    ) {
        return new TransactionTemplate(moviesTransactionManager);
    }

    @Bean
    public TransactionTemplate albumsTransactionTemplate(
            @Qualifier("albumsTransactionManager") PlatformTransactionManager albumsTransactionManager
    ) {
        return new TransactionTemplate(albumsTransactionManager);
    }

}
