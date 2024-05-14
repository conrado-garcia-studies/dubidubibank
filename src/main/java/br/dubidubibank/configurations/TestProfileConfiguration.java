package br.dubidubibank.configurations;

import br.dubidubibank.sowers.DatabaseSower;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestProfileConfiguration implements CommandLineRunner {
  @NonNull private DatabaseSower databaseSower;

  @Override
  public void run(String... args) throws Exception {
    databaseSower.seed();
  }
}
