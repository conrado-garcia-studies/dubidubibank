package br.dubidubibank.mappers;

import br.dubidubibank.entities.Person;
import br.dubidubibank.records.PersonGetRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonGetMapper {
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "email", source = "entity.email")
  @Mapping(target = "id", expression = "java(entity.getId().orElse(null))")
  @Mapping(target = "name", source = "entity.name")
  PersonGetRecord toRecord(Person entity);
}
