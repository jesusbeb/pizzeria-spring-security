package com.jbeb.pizza.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private static String SECRET_KEY = "pizz4"; // creamos una palabra clave
    private static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY); // creamos un algoritmo con HMAC256 y le enviamos una palabra clave

    // Metodo para crear un JWT (JSON Web Token) para un usuario
    // Creamos un JWT con el asunto que sera el nombre del usuario, withIssuer es quien crea el JWT, fecha en que
    // se crea, cuando expirara, en este caso durara 15 dias
    // con sign firmamos el token y le enviamos un algoritmo que creamos
    public String create(String username){
        return JWT.create()
                .withSubject(username)
                .withIssuer("pizza")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date( System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15) )) // fecha actual en milisegundos + 15 dias en milisegundos
                .sign(ALGORITHM);
    }

    // Metodo para validar un JWT
    // Usamos require() de JWT y le enviamos el algoritmo que usamos para crear el token
    // construimos y verificamos enviando el JWT. Si es valido retornamos true, si falla la verificacion lanzamos
    // una excepcion y retornamos false.
    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Metodo que obtiene el usuario del JWT que se este tratando
    public String getUsername(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }

}





/*
¿Qué es un JSON Web Token y cómo funciona?
Los JSON Web Tokens (JWT) son un estándar de código abierto diseñado para el intercambio seguro de información entre
partes. Este tipo de tokens utiliza el formato JSON y es especialmente útil para autenticación y autorización.
La estructura de un JWT consta de tres partes: Header, Payload y Signature, que aseguran la validez y seguridad del token.

- Header: Incluye el algoritmo de encriptación (como HMAC, RSA) y el tipo de token, que generalmente es JWT.
- Payload: Contiene la información que se desea transmitir (claims). Algunos parameters estándar son:
  * iss (issuer): Quién emitió el token.
  * iat (issued at): Cuándo fue emitido.
  * exp (expiration): Cuándo expira.
  * Claims personalizados: Puede agregar datos específicos según sus necesidades.
- Signature: Se usa para verificar que el mensaje no haya sido alterado. Combina el Header, Payload y una
clave secreta utilizando el algoritmo especificado.
 */