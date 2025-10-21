@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(List.of("*")); // allow all origins for dev, restrict in production
                cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                cfg.setAllowedHeaders(List.of("*"));
                cors.configurationSource(request -> cfg);
            })
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // allow public endpoints
                .requestMatchers(
                    "/api/auth/**",
                    "/index.html",
                    "/login.html",
                    "/register.html",
                    "/resume.html",
                    "/",
                    "/css/**",
                    "/js/**"
                ).permitAll()
                // require auth for PDF download
                .requestMatchers("/api/pdf/download").authenticated()
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
