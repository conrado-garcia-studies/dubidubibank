package br.dubidubibank.configurations;

import br.dubidubibank.clis.Cli;
import br.dubidubibank.clis.MainCli;
import br.dubidubibank.clis.impl.*;
import br.dubidubibank.services.*;
import br.dubidubibank.utils.CliUtils;
import br.dubidubibank.utils.SecurityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CliConfiguration {
  @Bean
  public Cli commandCli(
      ApplicationContext applicationContext, CliUtils cliUtils, SessionService sessionService) {
    return new CommandCli(applicationContext, cliUtils, sessionService);
  }

  @Bean
  public Cli depositCli(
      AccountService accountService, CliUtils cliUtils, SessionService sessionService) {
    return new DepositCli(accountService, cliUtils, sessionService);
  }

  @Bean
  public Cli exportCli(
      CliUtils cliUtils,
      SessionService sessionService,
      TransactionExportService transactionExportService) {
    return new ExportCli(cliUtils, sessionService, transactionExportService);
  }

  @Bean
  public Cli logInCli(CliUtils cliUtils, SessionService sessionService) {
    return new LogInCli(cliUtils, sessionService);
  }

  @Bean
  public Cli logOutCli(CliUtils cliUtils, SessionService sessionService) {
    return new LogOutCli(cliUtils, sessionService);
  }

  @Bean
  public MainCli mainCli(Cli commandCli) {
    return new DefaultMainCli((CommandCli) commandCli);
  }

  @Bean
  public Cli openCli(
      AccountService accountService,
      AccountTypeService accountTypeService,
      CliUtils cliUtils,
      PersonService personService,
      RestrictionService restrictionService,
      SecurityUtils securityUtils,
      SessionService sessionService) {
    return new OpenCli(
        accountService,
        accountTypeService,
        cliUtils,
        personService,
        restrictionService,
        securityUtils,
        sessionService);
  }

  @Bean
  public Cli quitCli(CliUtils cliUtils, SessionService sessionService) {
    return new QuitCli(cliUtils, sessionService);
  }

  @Bean
  public Cli restrictCli(
      AccountService accountService,
      RestrictionService restrictionService,
      CliUtils cliUtils,
      SessionService sessionService) {
    return new RestrictCli(accountService, cliUtils, restrictionService, sessionService);
  }

  @Bean
  public Cli transferCli(
      AccountService accountService, CliUtils cliUtils, SessionService sessionService) {
    return new TransferCli(accountService, cliUtils, sessionService);
  }

  @Bean
  public Cli withdrawCli(
      AccountService accountService, CliUtils cliUtils, SessionService sessionService) {
    return new WithdrawCli(accountService, cliUtils, sessionService);
  }
}
