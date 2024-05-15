package br.dubidubibank.mappers;

import br.dubidubibank.entities.Account;
import br.dubidubibank.records.AccountGetRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountGetMapper {
  @Autowired CommandGetMapper commandGetMapper; // NOSONAR
  @Autowired PersonGetMapper personGetMapper; // NOSONAR
  @Autowired RestrictionGetMapper restrictionGetMapper; // NOSONAR

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "agencyNumber", source = "entity.agencyNumber")
  @Mapping(target = "balance", source = "entity.balance")
  @Mapping(
      target = "commands",
      expression = "java(commandGetMapper.toRecord(entity.getCommands()))")
  @Mapping(target = "enabled", source = "entity.enabled")
  @Mapping(target = "id", expression = "java(entity.getId().orElse(null))")
  @Mapping(target = "number", source = "entity.number")
  @Mapping(target = "person", expression = "java(personGetMapper.toRecord(entity.getPerson()))")
  @Mapping(
      target = "restrictions",
      expression = "java(restrictionGetMapper.toRecord(entity.getRestrictions()))")
  @Mapping(target = "type", expression = "java(entity.getType().getCode().name())")
  @Mapping(target = "zoneId", source = "entity.zoneId.id")
  public abstract AccountGetRecord toRecord(Account entity);
}
