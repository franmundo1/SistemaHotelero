package lpda.SistemaHotelero.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthenticationEntryPoint=restAuthenticationEntryPoint;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/habitaciones/limpieza").hasAnyRole("ADMIN", "LIMPIEZA")
                        .requestMatchers(HttpMethod.PATCH, "/api/habitaciones/limpieza/*/estado-limpieza").hasAuthority("CAMBIAR_ESTADO_LIMPIEZA")

                        .requestMatchers(HttpMethod.GET, "/api/habitaciones/**").hasAuthority("VER_HABITACIONES")
                        .requestMatchers(HttpMethod.POST, "/api/habitaciones/**").hasAuthority("CREAR_HABITACION")
                        .requestMatchers(HttpMethod.PUT, "/api/habitaciones/**").hasAuthority("ACTUALIZAR_HABITACION")
                        .requestMatchers(HttpMethod.PATCH, "/api/habitaciones/*/estado-ocupacion").hasAuthority("CAMBIAR_ESTADO_OCUPACION")
                        .requestMatchers(HttpMethod.PATCH, "/api/habitaciones/**").hasAuthority("ACTUALIZAR_HABITACION")
                        .requestMatchers(HttpMethod.DELETE, "/api/habitaciones/**").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.GET, "/api/reservas/**").hasAuthority("VER_RESERVAS")
                        .requestMatchers(HttpMethod.POST, "/api/reservas/**").hasAuthority("CREAR_RESERVA")
                        .requestMatchers(HttpMethod.PUT, "/api/reservas/**").hasAuthority("ACTUALIZAR_RESERVA")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservas/**").hasAuthority("CANCELAR_RESERVA")

                        .requestMatchers(HttpMethod.GET, "/api/huespedes/**").hasAuthority("VER_HUESPEDES")
                        .requestMatchers(HttpMethod.POST, "/api/huespedes/**").hasAuthority("CREAR_HUESPED")
                        .requestMatchers(HttpMethod.PUT, "/api/huespedes/**").hasAuthority("ACTUALIZAR_HUESPED")

                        .requestMatchers("/api/checkIn/**").hasAuthority("REALIZAR_CHECK_IN")
                        .requestMatchers("/api/checkOut/**").hasAuthority("REALIZAR_CHECK_OUT")

                        .requestMatchers(HttpMethod.GET, "/pagos/**").hasAuthority("VER_PAGOS")
                        .requestMatchers(HttpMethod.POST, "/pagos/**").hasAuthority("REGISTRAR_PAGO")

                        .requestMatchers(HttpMethod.GET, "/canalesReserva/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.POST, "/canalesReserva/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/canalesReserva/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/canalesReserva/**").hasRole("ADMIN")
                        .requestMatchers("/api/acompanantes/**").hasAnyRole("ADMIN", "RECEPCIONISTA")

                        .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/consumos/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.POST, "/api/consumos/**").hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.DELETE, "/api/consumos/**").hasAnyRole("ADMIN", "RECEPCIONISTA")


                        .requestMatchers(HttpMethod.GET, "/api/dashboard/**").hasAnyRole("ADMIN", "RECEPCIONISTA")


                        .anyRequest().hasRole("ADMIN")
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}