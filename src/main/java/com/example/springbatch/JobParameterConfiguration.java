package com.example.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JobParameterConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job batchJob() {
    return jobBuilderFactory.get("Job")
      .start(step1())
      .next(step2())
      .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
      .tasklet((stepContribution, chunkContext) -> {

        // stepContribution의 경우에 JobParameters를 반환. Job에 셋팅한 파라미터를 그대로 반환
        JobParameters jobParameters = stepContribution.getStepExecution().getJobExecution().getJobParameters();
        jobParameters.getString("name");
        jobParameters.getLong("seq");
        jobParameters.getDate("date");
        jobParameters.getDouble("age");

        // chunkContext의 경우에 Map을 반환. 값을 확인하는 용도.
        Map<String, Object> jobParameterMap = chunkContext.getStepContext().getJobParameters();

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
      .tasklet((stepContribution, chunkContext) -> RepeatStatus.FINISHED)
      .build();
  }

}
