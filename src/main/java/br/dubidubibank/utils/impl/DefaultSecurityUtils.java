package br.dubidubibank.utils.impl;

import br.dubidubibank.utils.SecurityUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class DefaultSecurityUtils implements SecurityUtils {
  @NonNull private PasswordEncoder passwordEncoder;

  @Override
  public String encodeAndPrefix(String rawPassword) {
    String encodedPassword = passwordEncoder.encode(rawPassword);
    return String.format("{bcrypt}%s", encodedPassword);
  }
}
