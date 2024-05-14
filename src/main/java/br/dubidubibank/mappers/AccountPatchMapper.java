package br.dubidubibank.mappers;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.AccountPatchRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountPatchMapper {
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "balance", source = "record.balance")
  Account toEntity(AccountPatchRecord record); // NOSONAR
}
