package com.example.springbatch.step;

import com.example.springbatch.job.CustomJobParameterValidator;
import com.example.springbatch.job.CustomJobParametersIncrementer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class StepBuilderConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job batchJob() {
    return jobBuilderFactory.get("batchJob")
      .incrementer(new RunIdIncrementer())  // 같은 파라미터로 실행 가능하도록
      .start(taskStep())
      .next(step2())
      .next(step3())
      .build();
  }

  @Bean
  public Step taskStep() {
    return stepBuilderFactory.get("taskStep")
      .tasklet((stepContribution, chunkContext) -> {
        System.out.println("taskStep was executed");

        return RepeatStatus.FINISHED;
      })
      .allowStartIfComplete(true)   // 성공해도 반복
      .startLimit(20)   // 반복 횟수
      .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
      .<String, String> chunk(3)
      .reader(() -> null)
      .processor((ItemProcessor<String, String>) s -> null)
      .writer(items -> {})
      .build();
  }

  @Bean
  public Step step3() {
    return stepBuilderFactory.get("step3")
      .partitioner(taskStep())
      .gridSize(2)
      .build();
  }

  @Bean
  public Step step4() {
    return stepBuilderFactory.get("step4")
      .job(job())
      .build();
  }

  @Bean
  public Step step5() {
    return stepBuilderFactory.get("step5")
      .flow(flow())
      .build();
  }

  private Flow flow() {
    FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
    flowBuilder.start(step2()).end();

    return flowBuilder.build();
  }

  private Job job() {
    return jobBuilderFactory.get("job")
      .start(taskStep())
      .next(step2())
      .next(step3())
      .build();
  }
}
