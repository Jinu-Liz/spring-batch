package com.example.springbatch.flow;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * Decider를 사용하여 조건적인 흐름들을 제어할 수 있다.
 */
public class CustomDecider implements JobExecutionDecider {

  private int count = 1;

  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

    count++;

    if (count % 2 == 0) return new FlowExecutionStatus("EVEN");
    else return new FlowExecutionStatus("ODD");
  }
}
