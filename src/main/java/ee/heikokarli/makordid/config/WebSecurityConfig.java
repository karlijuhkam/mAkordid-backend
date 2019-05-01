package ee.heikokarli.makordid.config;

import ee.heikokarli.makordid.security.CORSFilter;
import ee.heikokarli.makordid.security.MakordidUserDetailsService;
import ee.heikokarli.makordid.security.auth.AuthorizationFilter;
import ee.heikokarli.makordid.security.auth.TokenAuthorizationHandler;
import ee.heikokarli.makordid.security.auth.TokenAuthorizationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) {
        managerBuilder.authenticationProvider(tokenAuthorizationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/", "/api/**").permitAll()
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public FilterChainProxy springSecurityFilterChain() throws Exception {
        SecurityFilterChain restChain = new DefaultSecurityFilterChain(
                new NegatedRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/api-docs"),
                        new AntPathRequestMatcher("/v2/api-docs"),
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/webjars/**"),
                        new AntPathRequestMatcher("/swagger-resources/**"),
                        new AntPathRequestMatcher("/actuator/**"),
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/register"),
                        new AntPathRequestMatcher("/forgotpassword"),
                        new AntPathRequestMatcher("/allbands/**"),
                        new AntPathRequestMatcher("/searchbands/**"),
                        new AntPathRequestMatcher("/searchsongs/**"),
                        new AntPathRequestMatcher("/bandsongs/**"),
                        new AntPathRequestMatcher("/activesongs/**"),
                        new AntPathRequestMatcher("/recentsongs"),
                        new AntPathRequestMatcher("/popularsongs")
                )),
                new CORSFilter(),
                new WebAsyncManagerIntegrationFilter(),
                new HeaderWriterFilter(java.util.Arrays.asList(new HstsHeaderWriter())),
                authorizationFilter()
        );

        return new FilterChainProxy(restChain);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthorizationFilter authorizationFilter() throws Exception {
        AuthorizationFilter filter = new AuthorizationFilter();
        filter.setAuthenticationManager(authenticationManager());

        filter.setAuthenticationSuccessHandler(tokenAuthorizationHandler());
        filter.setAuthenticationFailureHandler(tokenAuthorizationHandler());
        filter.setAllowSessionCreation(false);
        return filter;
    }

    @Bean
    public TokenAuthorizationProvider tokenAuthorizationProvider() {
        return new TokenAuthorizationProvider(userDetailsService());
    }

    @Bean
    public TokenAuthorizationHandler tokenAuthorizationHandler() {
        return new TokenAuthorizationHandler();
    }

    @Bean
    public MakordidUserDetailsService userDetailsService() {
        return new MakordidUserDetailsService();
    }

}
