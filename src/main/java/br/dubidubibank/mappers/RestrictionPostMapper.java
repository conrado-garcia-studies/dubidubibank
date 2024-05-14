package br.dubidubibank.mappers;

import br.dubidubibank.entities.Restriction;
import br.dubidubibank.records.RestrictionPostRecord;
import br.dubidubibank.services.AccountService;
import br.dubidubibank.services.CommandService;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RestrictionPostMapper {
  @Autowired AccountService accountService; // NOSONAR
  @Autowired CommandService commandService; // NOSONAR

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "account", expression = "java(accountService.findById(record.account().id()))")
  @Mapping(target = "amount", source = "record.amount")
  @Mapping(target = "command", expression = "java(commandService.findById(record.command().id()))")
  @Mapping(target = "endTime", source = "record.endTime", dateFormat = "HH:mm:ss.SSS")
  @Mapping(target = "startTime", source = "record.startTime", dateFormat = "HH:mm:ss.SSS")
  public abstract Restriction toEntity(RestrictionPostRecord record); // NOSONAR
}
