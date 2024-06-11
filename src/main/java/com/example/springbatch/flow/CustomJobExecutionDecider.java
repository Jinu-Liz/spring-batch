package com.example.springbatch.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CustomJobExecutionDecider {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean("jobExecutionDeciderJob")
  public Job batchJob() {
    return jobBuilderFactory.get("jobExecutionDeciderJob")
      .incrementer(new RunIdIncrementer())
      .start(step())
      .next(decider())
      .from(decider())
        .on("ODD")
        .to(oddStep())
      .from(decider())
        .on("EVEN")
        .to(evenStep())
      .end()
      .build();
  }

  @Bean
  public JobExecutionDecider decider() {
    return new CustomDecider();
  }

  @Bean("jobExecutionDeciderStep1")
  public Step step() {
    return stepBuilderFactory.get("jobExecutionDeciderStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("jobExecutionDeciderStep1 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean
  public Step oddStep() {
    return stepBuilderFactory.get("oddStep")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("oddStep has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean
  public Step evenStep() {
    return stepBuilderFactory.get("evenStep")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("evenStep has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }
}
