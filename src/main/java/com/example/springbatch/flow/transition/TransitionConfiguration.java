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
public class TransitionConfiguration {
  
  private final JobBuilderFactory jobBuilderFactory;
  
  private final StepBuilderFactory stepBuilderFactory;

  /**
   * 1. step1에서 FAILED가 발생하면 step2로 이동하고, step2에서 FAILED가 발생하면 Job을 중단한다.
   * 2. step1에서 FAILED가 발생하지 않으면 step3으로 이동하고, 성공 시 step4로 이동한다.
   * 3. step2에서 FAILED가 발생하지 않으면 step5로 이동한다.
   *
   * 따라서 step1 성공 시, step1 -> step3 -> step4
   * step1 실패 시 -> step2 성공 시 -> step5
   * step1 실패 시 -> step2 실패 시 -> 중단
   */
  @Bean("transitionJob")
  public Job batchJob() {
    return this.jobBuilderFactory.get("transitionJob")
      .start(step1())
        .on("FAILED")
        .to(step2())
        .on("FAILED")
        .stop() // Flow1
      .from(step1())
        .on("*")
        .to(step3())
        .next(step4())  // Flow2
      .from(step2())
        .on("*")
        .to(step5())  // Flow3
      .end()
      .build();
  }

  @Bean("transitionStep1")
  public Step step1() {
    return stepBuilderFactory.get("transitionStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("transitionStep1 has executed");
        contribution.setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("transitionStep2")
  public Step step2() {
    return stepBuilderFactory.get("transitionStep2")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("transitionStep2 has executed");
        contribution.setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("transitionStep3")
  public Step step3() {
    return stepBuilderFactory.get("transitionStep3")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("transitionStep3 has executed");
//        contribution.setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("transitionStep4")
  public Step step4() {
    return stepBuilderFactory.get("transitionStep4")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("transitionStep4 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("transitionStep5")
  public Step step5() {
    return stepBuilderFactory.get("transitionStep5")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("transitionStep5 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }
}
