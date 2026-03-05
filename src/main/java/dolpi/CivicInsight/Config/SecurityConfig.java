package dolpi.CivicInsight.Config;

import dolpi.CivicInsight.JwtFilter.JwtFilter;
import dolpi.CivicInsight.Service.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private MyUserDetails userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/user/complaint/**").hasRole("USER")
////                        .requestMatchers("/department_officer/Analysis/**").hasAnyRole("OFFICER", "ADMIN")
////                        .requestMatchers("/admin/fetch/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // ← ADD karo
//
//                        .anyRequest().permitAll()
//                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // YEH
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/create/**").permitAll()
                    .requestMatchers("/admin/signup/**").permitAll()
                    .requestMatchers("/api/v1/create/login/**").permitAll()
                    .requestMatchers("/create/department_user/**").permitAll()
                    .requestMatchers("/user/complaint/**").hasRole("USER")
                    .requestMatchers("/department_officer/Analysis/**").hasAnyRole("OFFICER", "ADMIN")
                    .requestMatchers("/admin/fetch/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Frontend ke dono possible origins allow karein
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 ghante ke liye preflight cache karein

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
