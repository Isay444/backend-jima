package isa.jima.ventas.security;

import isa.jima.ventas.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import isa.jima.ventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true) // debido al LazyInitializationException de FetchType.LAZY
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. Buscar usuario y lanzar excepcion dinámica si no existe
        Usuario usuario = usuarioRepository.findByNombre(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        // 2. COnvertir los permisos de rol a GrantedAuthority y guardarlos en una lista
        List<GrantedAuthority> authorities = usuario.getRol().getPermisos().stream()
                .map(permiso -> new SimpleGrantedAuthority(permiso.getNombre()))
                .collect(Collectors.toList());

        // 3. retornar el UserDetails construido correctamente
        return User.builder()
        .username(usuario.getNombre())
        .password(usuario.getContrasenia())
        .authorities(authorities)
        .disabled(!usuario.getActivo())
        .build();
    }
}
