package spring.security.jdbcauthentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    DataSource dataSource;

    //We need to inform AuthenticationManagerBuilder how we will perform authentication...
    //we are passing dataSource as an argument. If you have dependency h2 database, spring
    //knows that we will be using that for authentication and dataSource will be configured automatically
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username , password , enabled "
                + "from users "
                + "where username = ?")
                .authoritiesByUsernameQuery("select username , authority "
                + "from authorities "
                + "where username = ?");
        //^^ spring will create for us user table with roles...
        //^^ we are hardcoding users for learning purposes
        //^^ we are writting own queries if we are using custom created tables..
        //^^ this are the defaults values.. but we can ovveride them how we would like to..
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/").permitAll()
                .and().formLogin();
    }

    // We need to create a bean with PasswordEncoder
    //We shoud not use NoOpPasswordEncoder, only for learning purposes
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
