package com.example.springbatch.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FlowJob 예제
 */
@RequiredArgsConstructor
@Configuration
public class FlowJob {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean("flowJob")
  public Job job() {
    return jobBuilderFactory.get("flowJob")
      .start(flow())
      .next(step3())
      .end()
      .build();
  }

  @Bean
  public Flow flow() {
    FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
    flowBuilder
      .start(step1())
      .next(step2())
      .end();

    return flowBuilder.build();
  }

  @Bean("flowJobStep1")
  public Step step1() {
    return stepBuilderFactory.get("flowJobStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowJobStep1 has executed");
        contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowJobStep2")
  public Step step2() {
    return stepBuilderFactory.get("flowJobStep2")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowJobStep2 has executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean("flowJobStep3")
  public Step step3() {
    return stepBuilderFactory.get("flowJobStep3")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowJobStep3 has executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }
}
