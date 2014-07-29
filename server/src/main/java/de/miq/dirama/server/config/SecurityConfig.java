package de.miq.dirama.server.config;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Security settings.
 * 
 * @author mkuss
 * 
 */
@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
@PropertySources({
        @PropertySource(value = "classpath:/users.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:${user.dir}/config/users.properties", ignoreResourceNotFound = true) })
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log LOG = LogFactory.getLog(SecurityConfig.class);

    @Autowired
    private Environment env;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/nowplaying/**", "/history/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ANONYMOUS')")
                .and().csrf().disable();
        http.authorizeRequests().antMatchers("/**").hasRole("USER").and()
                .httpBasic().and().csrf().disable();
    }

    @Override
    protected void configure(
            final AuthenticationManagerBuilder authManagerBuilder)
            throws Exception {
        authManagerBuilder.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                String userProps = env.getProperty(username);

                if (userProps != null) {
                    int rolePos = userProps.lastIndexOf(":");
                    if (rolePos < 1) {
                        LOG.error("User <" + username
                                + "> not correctly configured! (ROLE missing)");
                        return null;
                    }
                    String userRole = userProps.substring(rolePos + 1);
                    StringTokenizer tokenizer = new StringTokenizer(userRole,
                            "#");

                    String password = userProps.substring(0, rolePos);

                    LOG.info("Configured user <" + username + "> with pwd <"
                            + password + "> and role <" + userRole + ">");

                    List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
                    while (tokenizer.hasMoreTokens()) {
                        auths.add(new SimpleGrantedAuthority(tokenizer
                                .nextToken()));
                    }

                    return new User(username, password, auths);
                } else {
                    LOG.error("User <" + username + "> not found!");
                }
                return null;
            }
        });
    }
}
