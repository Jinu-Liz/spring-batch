package com.example.springbatch.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  /**
   * Flow의 경우, 조건에 따라 유동적으로 처리할 수 있다.
   * step1을 시작 후, 성공("COMPLETED")하면 step3으로 이동하고, 실패("FAILED")하면 step2로 이동한다.
   */
  @Bean("flowBatchJob")
  public Job batchJob() {
    return jobBuilderFactory.get("flowBatchJob")
      .start(step1())
      .on("COMPLETED").to(step3())
      .from(step1())
      .on("FAILED").to(step2())
      .end()
      .build();
  }

  @Bean("flowStep1")
  public Step step1() {
    return stepBuilderFactory.get("flowStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("step1 has executed");
        throw new RuntimeException("step1 was failed");
//        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowStep2")
  public Step step2() {
    return stepBuilderFactory.get("flowStep2")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("step2 has executed");
        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowStep3")
  public Step step3() {
    return stepBuilderFactory.get("flowStep3")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("step3 has executed");
        return RepeatStatus.FINISHED;
      }).build();
  }
}
