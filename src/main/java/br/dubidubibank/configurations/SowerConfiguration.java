package br.dubidubibank.configurations;

import br.dubidubibank.repositories.*;
import br.dubidubibank.sowers.DatabaseSower;
import br.dubidubibank.sowers.impl.DefaultDatabaseSower;
import br.dubidubibank.utils.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SowerConfiguration {
  @Bean
  public DatabaseSower databaseSower(
      AccountRepository accountRepository,
      AccountTypeRepository accountTypeRepository,
      CommandRepository commandRepository,
      PersonRepository personRepository,
      RestrictionRepository restrictionRepository,
      SecurityUtils securityUtils) {
    return new DefaultDatabaseSower(
        accountRepository,
        accountTypeRepository,
        commandRepository,
        personRepository,
        restrictionRepository,
        securityUtils);
  }
}
