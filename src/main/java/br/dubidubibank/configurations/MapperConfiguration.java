package br.dubidubibank.configurations;

import br.dubidubibank.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
  @Bean
  public AccountGetMapper accountGetMapper() {
    return new AccountGetMapperImpl();
  }

  @Bean
  public AccountPatchMapper accountPatchMapper() {
    return new AccountPatchMapperImpl();
  }

  @Bean
  public AccountPostMapper accountPostMapper() {
    return new AccountPostMapperImpl();
  }

  @Bean
  public CommandGetMapper commandGetMapper() {
    return new CommandGetMapperImpl();
  }

  @Bean
  public PersonGetMapper personGetMapper() {
    return new PersonGetMapperImpl();
  }

  @Bean
  public RestrictionGetMapper restrictionGetMapper() {
    return new RestrictionGetMapperImpl();
  }

  @Bean
  public RestrictionPatchMapper restrictionPatchMapper() {
    return new RestrictionPatchMapperImpl();
  }

  @Bean
  public RestrictionPostMapper restrictionPostMapper() {
    return new RestrictionPostMapperImpl();
  }
}
