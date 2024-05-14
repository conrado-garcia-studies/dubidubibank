package br.dubidubibank.configurations;

import br.dubidubibank.repositories.*;
import br.dubidubibank.services.*;
import br.dubidubibank.services.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class ServiceConfiguration {
  @Bean
  public AccountService accountService(
      AccountTypeService accountTypeService,
      CommandService commandService,
      AccountRepository accountRepository,
      RestrictionService restrictionService,
      TransactionService transactionService) {
    return new DefaultAccountService(
        accountTypeService,
        commandService,
        accountRepository,
        restrictionService,
        transactionService);
  }

  @Bean
  public AccountTypeService accountTypeService(AccountTypeRepository accountTypeRepository) {
    return new DefaultAccountTypeService(accountTypeRepository);
  }

  @Bean
  public CommandService commandService(CommandRepository commandRepository) {
    return new DefaultCommandService(commandRepository);
  }

  @Bean
  public RestrictionService restrictionService(RestrictionRepository restrictionRepository) {
    return new DefaultRestrictionService(restrictionRepository);
  }

  @Bean
  public SessionService sessionService(
      AccountService accountService, AuthenticationManager authenticationManager) {
    return new DefaultSessionService(accountService, authenticationManager);
  }

  @Bean
  public TransactionExportService transactionExportService(SessionService sessionService, TransactionService transactionService) {
    return new DefaultTransactionExportService(sessionService, transactionService);
  }

  @Bean
  public TransactionService transactionService(TransactionRepository transactionRepository) {
    return new DefaultTransactionService(transactionRepository);
  }
}
