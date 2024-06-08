package com.example.springbatch.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowStartNext {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean("flowStartNextJob")
  public Job batchJob() {
    return jobBuilderFactory.get("flowStartNextJob")
      .start(flowA())
      .next(step3())
      .next(flowB())
      .next(step6())
      .end()
      .build();
  }

  @Bean("startNextFlowA")
  public Flow flowA() {
    FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("startNextFlowA");
    flowBuilder.start(step1())
      .next(step2())
      .end();

    return flowBuilder.build();
  }

  @Bean("startNextFlowB")
  public Flow flowB() {
    FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("startNextFlowB");
    flowBuilder.start(step4())
      .next(step5())
      .end();

    return flowBuilder.build();
  }

  @Bean("flowStartStep1")
  public Step step1() {
    return stepBuilderFactory.get("flowStartStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowStartStep1 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowStartStep2")
  public Step step2() {
    return stepBuilderFactory.get("flowStartStep2")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowStartStep2 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowStartStep3")
  public Step step3() {
    return stepBuilderFactory.get("flowStartStep3")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowStartStep3 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  /**
   * 단순히 start와 next로 이루어진 경우엔
   * 하나라도 실패하면 해당 부분에서 멈추며 전체가 실패로 처리된다.
   */
  @Bean("flowStartStep4")
  public Step step4() {
    return stepBuilderFactory.get("flowStartStep4")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowStartStep4 has executed");
        throw new RuntimeException("flowStartStep4 was failed");
//        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowStartStep5")
  public Step step5() {
    return stepBuilderFactory.get("flowStartStep5")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowStartStep5 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("flowStartStep6")
  public Step step6() {
    return stepBuilderFactory.get("flowStartStep6")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("flowStartStep6 has executed");

        return RepeatStatus.FINISHED;
      }).build();
  }
}
