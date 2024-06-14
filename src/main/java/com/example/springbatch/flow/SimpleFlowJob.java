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
      .start(flow())  // SimpleFlow 내에서 SimpleFlow 실행
      .next(step3())
      .end()
      // 여기까지가 SimpleFlow 객체 생성
      .build();
  }

  @Bean("simpleFlow")
  public Flow flow() {
    FlowBuilder<Flow> builder = new FlowBuilder<>("simpleFlow");
    builder.start(step1())
      .next(step2())
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

        return RepeatStatus.FINISHED;
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
}
