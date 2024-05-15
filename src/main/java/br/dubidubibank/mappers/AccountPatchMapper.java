package br.dubidubibank.mappers;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.AccountPatchRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper
public interface AccountPatchMapper {
  @BeanMapping(ignoreByDefault = true)
  Account toEntity(AccountPatchRecord record); // NOSONAR
}
