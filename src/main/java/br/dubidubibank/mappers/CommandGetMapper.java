package br.dubidubibank.mappers;

import br.dubidubibank.entities.Command;
import br.dubidubibank.records.CommandGetRecord;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandGetMapper {
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "code", expression = "java(entity.getCode().name())")
  @Mapping(target = "id", expression = "java(entity.getId().orElse(null))")
  @Mapping(target = "name", source = "entity.name")
  @Mapping(target = "restrictable", source = "entity.restrictable")
  @Mapping(target = "terminal", source = "entity.terminal")
  CommandGetRecord toRecord(Command entity);

  List<CommandGetRecord> toRecord(List<Command> entities);
}
