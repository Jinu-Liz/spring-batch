package com.example.springbatch.flow.transition;

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
public class CustomExitStatus {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean("customExitStatusJob")
  public Job batchJob() {
    return this.jobBuilderFactory.get("customExitStatusJob")
      .start(step1())
        .on("FAILED")
        .to(step2())
        .on("PASS") // 실패이기 때문에 SpringBatch가 자동으로 ExitStatus를 FAILED로 지정한다.
        .stop() // stop에 의해 Job의 Status가 STOPPED로 변경된다.
      .end()
      .build();
  }

  @Bean("customExitStep1")
  public Step step1() {
    return stepBuilderFactory.get("customExitStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("customExitStep1 has executed");
        contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("customExitStep2")
  public Step step2() {
    return stepBuilderFactory.get("customExitStep2")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("customExitStep2 has executed");

        return RepeatStatus.FINISHED;
      })
      .listener(new PassCheckingListener()) // ExitStatus를 PASS로 변경
      .build();
  }

}
