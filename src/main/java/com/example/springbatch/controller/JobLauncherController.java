package com.example.springbatch.controller;

import com.example.springbatch.Member;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JobLauncherController {

  @Autowired
  private Job job;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private BasicBatchConfigurer basicBatchConfigurer;

  @PostMapping("/batch")
  public String launch(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

    JobParameters jobParameters = new JobParametersBuilder()
      .addString("id", member.getId())
      .addDate("date", new Date())
      .toJobParameters();

    SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher)basicBatchConfigurer.getJobLauncher();
//    SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher) jobLauncher;    // Proxy객체이기 때문에 Casting 할 수 없음.
    simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    simpleJobLauncher.run(job, jobParameters);
//    jobLauncher.run(job, jobParameters);

    return "batch completed";
  }

}
