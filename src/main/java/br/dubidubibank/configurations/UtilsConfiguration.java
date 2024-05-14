package br.dubidubibank.configurations;

import br.dubidubibank.utils.CliUtils;
import br.dubidubibank.utils.SecurityUtils;
import br.dubidubibank.utils.impl.DefaultCliUtils;
import br.dubidubibank.utils.impl.DefaultSecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UtilsConfiguration {
  @Bean
  public CliUtils cliUtils() {
    return new DefaultCliUtils();
  }

  @Bean
  public SecurityUtils securityUtils(PasswordEncoder passwordEncoder) {
    return new DefaultSecurityUtils(passwordEncoder);
  }
}
