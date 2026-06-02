package isa.jima.ventas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor // Reemplaza los @Autowired usando inyección por constructor
@Slf4j // Facilita el loggeo de errores solicitado en las instrucciones
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Leer header de autorización
            String header = request.getHeader("Authorization");
                    // List<GrantedAuthority> authorities = jwtUtil.getAuthoritiesFromToken(token);
            // 2. Si es nulo o no empieza con "Bearer ", dejar pasar sin autenticar
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            // 3. Extraer el token (quitando "Bearer ")
            String token = header.substring(7);

            // 4. Extraer el username
            String username = jwtUtil.getUsernameFromToken(token);

            // 5. Validar que el username exista y que no haya ya una autenticación previa en el contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validar si el token sigue siendo correcto y vigente
                if (jwtUtil.validarToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities() // 'authorities' en lugar de userDetails.getAuthorities() solo si no quisiera consultar la BD en cada peticion
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Sellar la autenticación en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch (Exception e){
            // 7. Envolver T0D0 en un try-catch y loggear si algo sale mal (token alterado, expirado, etc.)
            log.error("Error en JWT filter", e);
        }
        // 6. Continuar con el siguiente filtro en la cadena pase lo que pase
        filterChain.doFilter(request, response);
    }
    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // No hay token → dejar que el entryPoint maneje el 401
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            String username = jwtUtil.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validarToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    // Token inválido (expirado o no coincide el usuario)
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                    return; // ← importante: no continuar
                }
            }
        } catch (Exception e) {
            log.error("Error validando token JWT: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return; // ← no continuar la cadena
        }

        filterChain.doFilter(request, response);
    }
    */
}
