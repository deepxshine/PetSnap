package my.project.petsnap.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import my.project.petsnap.service.JwtTokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.core.context.SecurityContextHolder

class JwtAuthenticationFilter(private val jwtTokenService: JwtTokenService) : OncePerRequestFilter() { // расширяет OncePerRequestFilter
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) { // doFilterInternal выполняется для каждого запроса. Он извлекает токен, проверяет его валидность и создает объект Authentication
        val token = extractToken(request) // извлекает JWT токен из заголовка запроса.
        if (token != null && jwtTokenService.validateToken(token)) { // токен не равен null и является валидным
            val username = jwtTokenService.getUsernameFromToken(token)
            val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList()) // аутентификация пользователя в Spring Security
            SecurityContextHolder.getContext().authentication = authentication // Объект Authentication устанавливается в контекст безопасности (SecurityContextHolder), что позволяет Spring Security знать, что пользователь аутентифицирован.
        }
        filterChain.doFilter(request, response) // для передачи запроса и ответа следующему фильтру в цепочке.
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") // Получает значение заголовка Authorization из запроса.
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7) // Если заголовок соответствует формату, извлекает токен, удаляя префикс "Bearer " (7 символов).
        } else null
    }
}