package my.project.petsnap.config

import my.project.petsnap.filter.JwtAuthenticationFilter
import my.project.petsnap.service.JwtTokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtTokenService: JwtTokenService) {

    @Bean
    fun passwordEncoder(): PasswordEncoder { // хеширование паролей
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/user/login").permitAll() // Разрешает доступ к URL /login всем пользователям без аутентификации.
                    .requestMatchers("/user/register").permitAll() // также для регистрации
                    .requestMatchers("/swagger-ui/", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                        "/swagger-resources", "/swagger-resources/**").permitAll() // Разрешить доступ к Swagger UI
//                    .anyRequest().authenticated() // Требует аутентификации для всех остальных запросов.
                    .anyRequest().permitAll() // Не требует аутентификации для всех остальных запросов.
            }
            .csrf { csrf ->
                csrf.disable()
            } // Отключаем CSRF, так как мобильные приложения обычно не используют формы
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Отключает создание сессий.
            }
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java) // обрабатывать JWT токены, извлекать их из заголовков запросов и проверять их валидность

        return http.build() // Создает и возвращает SecurityFilterChain, который будет использоваться для обработки запросов
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtTokenService)
    }

}