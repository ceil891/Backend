package com.smartretail.backend.security;

import com.smartretail.backend.entity.NhanVien;
import com.smartretail.backend.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NhanVienDetailsService implements UserDetailsService {

    private final NhanVienRepository nhanVienRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(nhanVien.getEmail())
                .password(nhanVien.getMatKhauHash())
                .disabled(!"active".equals(nhanVien.getTrangThai()))
                .authorities(nhanVien.getVaiTros().stream()
                        .map(vaiTro -> new SimpleGrantedAuthority("ROLE_" + vaiTro.getTenVaiTro().toUpperCase()))
                        .collect(Collectors.toList()))
                .build();
    }
}