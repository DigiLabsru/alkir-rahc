package ru.digilabs.alkir.rahc.controller.v2.validation;

import org.apache.commons.lang3.StringUtils;
import ru.digilabs.alkir.rahc.controller.v2.api.ConnectionDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClusterIdIsNotEmptyValidator implements ConstraintValidator<ClusterIdIsNotEmpty, ConnectionDTO> {

  @Override
  public boolean isValid(
    ConnectionDTO connection,
    ConstraintValidatorContext context
  ) {
    return connection.getClusterId().filter(StringUtils::isNotBlank).isPresent()
           || connection.getClusterName().filter(StringUtils::isNotBlank).isPresent();
  }
}
