package com.example.springbatch.context;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet2 implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    System.out.println("Step2 실행");

    StepExecution contributionStepExecution = stepContribution.getStepExecution();
    ExecutionContext jobExecutionContext = contributionStepExecution.getJobExecution().getExecutionContext();
    ExecutionContext stepExecutionContext = contributionStepExecution.getExecutionContext();

    System.out.println("jobName : " + jobExecutionContext.get("jobName"));
    System.out.println("stepName : " + stepExecutionContext.get("stepName"));

    String stepName = chunkContext.getStepContext().getStepExecution().getStepName();

    if (stepExecutionContext.get("stepName") == null) stepContribution.getStepExecution().getExecutionContext().put("stepName", stepName);

    return RepeatStatus.FINISHED;
  }
}
