package my.project.petsnap.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.crypto.SecretKey

@Service
@Transactional
class JwtTokenService {

    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    private val expirationTime = 3600000 // 1 час

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey) // Подписывает токен с использованием секретного ключа.
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            // setSigningKey(secretKey): устанавливает секретный ключ, который будет использоваться для проверки подписи токена.
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token) // Парсит JWT токен и возвращает объект Jws<Claims>, который содержит заголовки и полезную нагрузку токена.
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body.subject // Возвращает значение поля sub (subject) из полезной нагрузки токена. В данном случае, это имя пользователя.
    }
}