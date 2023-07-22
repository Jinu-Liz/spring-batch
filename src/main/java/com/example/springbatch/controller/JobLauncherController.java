package com.example.springbatch.controller;

import com.example.springbatch.Member;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {

  @Autowired
  private Job job;

  private JobLauncher jobLauncher;

  public String launch(@RequestBody Member member) {

    return "";
  }

}
