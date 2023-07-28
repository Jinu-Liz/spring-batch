package com.example.springbatch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SimpleJobConfig {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job batchJob() {
    return jobBuilderFactory.get("batchJob")
      .incrementer(new RunIdIncrementer())  // 같은 파라미터로 실행 가능하도록
      .start(step1())
      .next(step2())
      .next(step3())
      .validator(new JobParametersValidator() {
        @Override
        public void validate(JobParameters jobParameters) throws JobParametersInvalidException {

        }
      })
      .preventRestart()
      .listener(new JobExecutionListener() {
        @Override
        public void beforeJob(JobExecution jobExecution) {

        }

        @Override
        public void afterJob(JobExecution jobExecution) {

        }
      })
      .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
      .tasklet((stepContribution, chunkContext) -> {
        System.out.println("step1 was executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
      .tasklet((stepContribution, chunkContext) -> {
        System.out.println("step2 was executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean
  public Step step3() {
    return stepBuilderFactory.get("step3")
      .tasklet((stepContribution, chunkContext) -> {
        chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
        stepContribution.setExitStatus(ExitStatus.STOPPED);
        System.out.println("step3 was executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }
}