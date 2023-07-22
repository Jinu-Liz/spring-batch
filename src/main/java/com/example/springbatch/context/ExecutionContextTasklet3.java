package com.example.springbatch.context;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionContextTasklet3 implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    System.out.println("Step3 실행");
    ExecutionContext chunkExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    Object name = chunkExecutionContext.get("name");

    if (name == null) {
      chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("name", "user1");
      throw new RuntimeException("step3 실패");
    }

    return RepeatStatus.FINISHED;
  }
}
