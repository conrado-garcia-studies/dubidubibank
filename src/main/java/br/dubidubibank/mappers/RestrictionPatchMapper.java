package br.dubidubibank.mappers;

import br.dubidubibank.entities.Restriction;
import br.dubidubibank.records.RestrictionPatchRecord;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RestrictionPatchMapper {
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "amount", source = "record.amount")
  Restriction toEntity(RestrictionPatchRecord record); // NOSONAR
}
