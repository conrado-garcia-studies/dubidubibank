package br.dubidubibank.configurations;

import br.dubidubibank.clis.MainCli;
import br.dubidubibank.sowers.DatabaseSower;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cli")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CliProfileConfiguration implements CommandLineRunner {
  @NonNull private DatabaseSower databaseSower;
  @NonNull private MainCli mainCli;

  @Override
  public void run(String... args) throws Exception {
    databaseSower.seed();
    mainCli.start();
  }
}
