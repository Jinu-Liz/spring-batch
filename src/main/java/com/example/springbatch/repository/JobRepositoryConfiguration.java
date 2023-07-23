//package com.example.springbatch.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@RequiredArgsConstructor
//@Configuration
//public class JobRepositoryConfiguration {
//
//  private final JobBuilderFactory jobBuilderFactory;
//
//  private final StepBuilderFactory stepBuilderFactory;
//
//  private final JobExecutionListener jobRepositoryListener;
//
//  @Bean
//  public Job job() {
//  return jobBuilderFactory.get("job")
//      .start(step1())
//      .next(step2())
//      .listener(jobRepositoryListener)
//      .build();
//  }
//
//  @Bean
//  public Step step1() {
//  return stepBuilderFactory.get("step1")
//      .tasklet((stepContribution, chunkContext) -> {
//          System.out.println("step1 has executed");
//
//          return RepeatStatus.FINISHED;
//      })
//      .build();
//  }
//
//  @Bean
//  public Step step2() {
//  return stepBuilderFactory.get("step2")
//      .tasklet((stepContribution, chunkContext) -> {
//          System.out.println("step2 has executed");
//
//          return RepeatStatus.FINISHED;
//      })
//      .build();
//  }
//}
