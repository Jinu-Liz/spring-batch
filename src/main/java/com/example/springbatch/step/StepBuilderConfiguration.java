package com.example.springbatch.step;

import com.example.springbatch.job.CustomJobParameterValidator;
import com.example.springbatch.job.CustomJobParametersIncrementer;
import com.example.springbatch.tasklet.CustomTasklet;
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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class StepBuilderConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job batchJob() {
    return jobBuilderFactory.get("batchJob")
//      .incrementer(new RunIdIncrementer())  // 같은 파라미터로 실행 가능하도록
      .start(taskStep())
      .next(customTasklet())
//      .next(chunkStep())
//      .next(step3())
      .build();
  }

  @Bean
  public Step taskStep() {
    return stepBuilderFactory.get("taskStep")
      .tasklet((stepContribution, chunkContext) -> {
        System.out.println("taskStep was executed");

        return RepeatStatus.FINISHED;
      })
//      .allowStartIfComplete(true)   // 성공해도 반복
      .build();
  }

  @Bean
  public Step customTasklet() {
    return stepBuilderFactory.get("customTask")
//      .tasklet(new CustomTasklet())
      .tasklet((stepContribution, chunkContext) -> {throw new RuntimeException();})
      .startLimit(3)   // 반복 횟수. 해당 횟수가 넘으면 Step이 동작하지 않는다. 근데 Job에서 '.incrementer(new RunIdIncrementer())'랑 같이 쓰면 동작하지 않는듯..
      .build();
  }

  @Bean
  public Step chunkStep() {
    return stepBuilderFactory.get("chunkStep")
      .<String, String> chunk(10)
      .reader(new ListItemReader<>(Arrays.asList("item1","item2","item3","item4","item5")))
      .processor((ItemProcessor<String, String>) String::toUpperCase)
      .writer(items -> items.forEach(System.out::println))
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
    flowBuilder.start(chunkStep()).end();

    return flowBuilder.build();
  }

  private Job job() {
    return jobBuilderFactory.get("job")
      .start(taskStep())
      .next(chunkStep())
      .next(step3())
      .build();
  }
}
