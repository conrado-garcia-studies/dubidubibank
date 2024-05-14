package br.dubidubibank.mappers;

import br.dubidubibank.entities.Restriction;
import br.dubidubibank.records.RestrictionGetRecord;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestrictionGetMapper {
  @BeanMapping(ignoreByDefault = true)
  @Mapping(
      target = "account",
      expression =
          "java(new br.dubidubibank.records.IdRecord(entity.getAccount().getId().orElse(null)))")
  @Mapping(target = "amount", source = "entity.amount")
  @Mapping(
      target = "command",
      expression =
          "java(new br.dubidubibank.records.IdRecord(entity.getCommand().getId().orElse(null)))")
  @Mapping(target = "endTime", source = "entity.endTime", dateFormat = "HH:mm:ss.SSS")
  @Mapping(target = "id", expression = "java(entity.getId().orElse(null))")
  @Mapping(target = "startTime", source = "entity.startTime", dateFormat = "HH:mm:ss.SSS")
  RestrictionGetRecord toRecord(Restriction entity);

  List<RestrictionGetRecord> toRecord(List<Restriction> entities);
}
