package com.promptduel.util;

import com.promptduel.entity.User;
import com.promptduel.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Extracts the authenticated user's info from the SecurityContext.
 */
@Component
public class SecurityUtil {

    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the currently authenticated user's database ID.
     */
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("No authenticated user found");
        }
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));
        return user.getId();
    }
}
