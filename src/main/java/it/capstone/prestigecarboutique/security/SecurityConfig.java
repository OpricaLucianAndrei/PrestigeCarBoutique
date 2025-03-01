package it.capstone.prestigecarboutique.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true) //con debug true, nel caso di errori ci darà ancora piu info riguardo l'errore
@EnableMethodSecurity
// EnableMethodSecurity PERMETTE DI ATTIVARE LA SICUREZZA SU I METODI DEL CONTROLLER CON ANNOTAZIONE @PRE AUTHORIZED
public class SecurityConfig {

    //disabilitiamo alcuni filtri, tipo l'autenticazione con il form, csrf(protezione contro accessi impropri), sessionManagement
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/prestigecarboutique/auto").permitAll();
                    auth.requestMatchers("/api/prestigecarboutique/allauto").permitAll();
                    auth.requestMatchers("/api/prestigecarboutique/current-user").permitAll();
                    auth.requestMatchers("/api/**").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .cors(withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://prestige-car-boutique-website-eb1v.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

