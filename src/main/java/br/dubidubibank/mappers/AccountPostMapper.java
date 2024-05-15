package br.dubidubibank.mappers;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.AccountPostRecord;
import br.dubidubibank.services.AccountTypeService;
import br.dubidubibank.services.PersonService;
import br.dubidubibank.utils.SecurityUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountPostMapper {
  @Autowired AccountTypeService accountTypeService; // NOSONAR
  @Autowired PersonService personService; // NOSONAR
  @Autowired SecurityUtils securityUtils; // NOSONAR

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "agencyNumber", source = "record.agencyNumber")
  @Mapping(target = "balance", source = "record.balance")
  @Mapping(target = "enabled", source = "record.enabled")
  @Mapping(target = "number", source = "record.number")
  @Mapping(
      target = "password",
      expression = "java(securityUtils.encodeAndPrefix(record.password()))")
  @Mapping(
      target = "person",
      expression =
          "java(personService.findOptionalByEmail(record.person().email())"
              + ".orElse(personService.save(new br.dubidubibank.entities.Person("
              + "record.person().email(), record.person().name()))))")
  @Mapping(
      target = "type",
      expression =
          "java(accountTypeService.findByCode(br.dubidubibank.enums.AccountTypeCode.valueOf(record.type())))")
  @Mapping(target = "zoneId", expression = "java(java.time.ZoneId.of(record.zoneId()))")
  public abstract Account toEntity(AccountPostRecord record); // NOSONAR
}
