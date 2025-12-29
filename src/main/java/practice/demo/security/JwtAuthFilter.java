package practice.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // 🔴 Send proper 401 JSON
    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("""
                {
                  "success": false,
                  "message": "%s"
                }
                """.formatted(message));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // ✅ No token → let Spring Security handle protected routes
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);

            // 🔐 Extract JWT data
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                unauthorized(response, "Invalid token user");
                return;
            }

            // 🔥 Token version validation (ADMIN / SUPER_ADMIN)
            if ("ADMIN".equals(role) || "SUPER_ADMIN".equals(role)) {
                Integer tokenVersion = jwtUtil.extractTokenVersion(token);

                if (tokenVersion == null ||
                        !tokenVersion.equals(user.getTokenVersion())) {
                    unauthorized(response, "Token expired. Please login again.");
                    return;
                }
            }

            // ✅ Authentication success
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(() -> "ROLE_" + role)
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // ❌ Expired / Invalid JWT
            unauthorized(response, "Unauthorized or token expired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
