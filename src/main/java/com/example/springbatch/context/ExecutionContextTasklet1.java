package com.example.springbatch.context;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet1 implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    System.out.println("Step1 실행");

    StepExecution contributionStepExecution = stepContribution.getStepExecution();
    ExecutionContext jobExecutionContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext();
    ExecutionContext stepExecutionContext = contributionStepExecution.getExecutionContext();

    StepExecution chunkStepExecution = chunkContext.getStepContext().getStepExecution();
    String jobName = chunkStepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = chunkStepExecution.getStepName();

    if (jobExecutionContext.get("jobName") == null) jobExecutionContext.put("jobName", jobName);

    if (stepExecutionContext.get("stepName") == null) stepExecutionContext.put("stepName", stepName);

    System.out.println("jobName : " + jobExecutionContext.get("jobName"));
    System.out.println("stepName : " + stepExecutionContext.get("stepName"));

    return RepeatStatus.FINISHED;
  }
}
