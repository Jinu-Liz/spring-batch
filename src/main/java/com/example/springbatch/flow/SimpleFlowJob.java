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

@RequiredArgsConstructor
@Configuration
public class SimpleFlowJob {
  
  private final JobBuilderFactory jobBuilderFactory;
  
  private final StepBuilderFactory stepBuilderFactory;
  
  @Bean("simpleflowJob")
  public Job job() {
    return jobBuilderFactory.get("simpleflowJob")
      // 여기서부터
      .start(flow1())  // SimpleFlow 내에서 SimpleFlow 실행
        .on("COMPLETED")
        .to(flow2())  // SimpleFlow 내에서 SimpleFlow 실행
      .from(flow1())
        .on("FAILED")
        .to(flow3())
      .end()
      // 여기까지가 SimpleFlow 객체 생성
      .build();
  }

  @Bean("simpleFlow1")
  public Flow flow1() {
    FlowBuilder<Flow> builder = new FlowBuilder<>("simpleFlow1");
    builder
      .start(step1())
      .next(step2())
      .end();

    return builder.build();
  }

  @Bean("simpleFlow2")
  public Flow flow2() {
    FlowBuilder<Flow> builder = new FlowBuilder<>("simpleFlow2");
    builder
      .start(flow3())
      .next(step5())
      .next(step6())
      .end();

    return builder.build();
  }

  @Bean("simpleFlow3")
  public Flow flow3() {
    FlowBuilder<Flow> builder = new FlowBuilder<>("simpleFlow3");
    builder
      .start(step3())
      .next(step4())
      .end();

    return builder.build();
  }

  @Bean("simpleflowJobStep1")
  public Step step1() {
    return stepBuilderFactory.get("simpleflowJobStep1")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("simpleflowJobStep1 has executed");
        contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
      }).build();
  }

  @Bean("simpleflowJobStep2")
  public Step step2() {
    return stepBuilderFactory.get("simpleflowJobStep2")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("simpleflowJobStep2 has executed");

        throw new RuntimeException("simpleflowJobStep2 was failed");
//        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean("simpleflowJobStep3")
  public Step step3() {
    return stepBuilderFactory.get("simpleflowJobStep3")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("simpleflowJobStep3 has executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean("simpleflowJobStep4")
  public Step step4() {
    return stepBuilderFactory.get("simpleflowJobStep4")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("simpleflowJobStep4 has executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean("simpleflowJobStep5")
  public Step step5() {
    return stepBuilderFactory.get("simpleflowJobStep5")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("simpleflowJobStep5 has executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }

  @Bean("simpleflowJobStep6")
  public Step step6() {
    return stepBuilderFactory.get("simpleflowJobStep6")
      .tasklet((contribution, chunkContext) -> {
        System.out.println("simpleflowJobStep6 has executed");

        return RepeatStatus.FINISHED;
      })
      .build();
  }
}
