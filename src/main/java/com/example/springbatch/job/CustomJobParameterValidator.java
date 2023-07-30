package com.example.springbatch.job;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class CustomJobParameterValidator implements JobParametersValidator {

  @Override
  public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
    if (jobParameters.getString("name") == null) {
      throw new JobParametersInvalidException("name parameters is not found");
    }
  }
}
