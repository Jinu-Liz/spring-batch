package com.example.springbatch.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BatchAndExitStatus {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean("batchAndExitStatusJob")
  public Job batchJob() {
    return this.jobBuilderFactory.get("batchAndExitStatusJob")
      .start(step1())
      .next(step2())
      .build();
  }

  @Bean("batchAndExitStatusFlowJob")
  public Job batchFlowJob() {
    return this.jobBuilderFactory.get("batchAndExitStatusFlowJob")
      .start(step3())
      .on("FAILED")
      .to(step4())
      .end()
      .build();
  }

  @Bean("statusStep1")
  public Step step1() {
    return stepBuilderFactory.get("statusStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("statusStep1 has executed");
//        throw new RuntimeException("statusStep1 was failed");
        return RepeatStatus.FINISHED;
      }).build();
  }

  /**
   * BatchStatus와 ExitStatus가 다른 경우.
   * Job의 경우 마지막 step의 ExitStatus와 BatchStatus가 반영된다.
   */
  @Bean("statusStep2")
  public Step step2() {
    return stepBuilderFactory.get("statusStep2")
      .tasklet((contribution, chunkContext) -> {
        contribution.setExitStatus(ExitStatus.FAILED);
        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("statusStep3")
  public Step step3() {
    return stepBuilderFactory.get("statusStep3")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("statusStep3 has executed");
        contribution.setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  /**
   * BatchStatus와 ExitStatus가 다른 경우.
   * Job의 경우 마지막 step의 ExitStatus와 BatchStatus가 반영된다.
   */
  @Bean("statusStep4")
  public Step step4() {
    return stepBuilderFactory.get("statusStep4")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("statusStep4 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }
}
