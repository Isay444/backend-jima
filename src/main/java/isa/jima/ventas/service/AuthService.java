package isa.jima.ventas.service;

import isa.jima.ventas.entity.Permiso;
import isa.jima.ventas.entity.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import isa.jima.ventas.dto.request.LoginRequest;
import isa.jima.ventas.dto.response.LoginResponse;
import isa.jima.ventas.repository.UsuarioRepository;
import isa.jima.ventas.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public LoginResponse login(LoginRequest request) {
        // 1. Autenticar con el AuthenticationManager (Valida contra la BD usando UserDetailsServiceImpl)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.nombre(), request.contrasenia())
        );
        // 2. Obtener el objeto principal autenticado castéandolo a UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Buscar el usuario real en la base de datos para extraer los metadatos extendidos
        Usuario usuario = usuarioRepository.findByNombre(request.nombre())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado en el sistema"));

        // 4. Generar el JWT pasando el UserDetails
        String token = jwtUtil.generateToken(userDetails);

        // 5. Mapear los permisos de la entidad a strings simples
        List<String> permisos = usuario.getRol().getPermisos().stream()
                .map(Permiso::getNombre)
                .toList();

        // 6. Retornar el DTO completo
        return new LoginResponse(
                token,
                usuario.getNombre(),
                usuario.getIdUsuario(),
                usuario.getRol().getNombre(),
                permisos
        );
    }
    
}
