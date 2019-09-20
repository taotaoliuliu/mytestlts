package com.aebiz.mytest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private JobClient jobClient;
	
	@Value("${lts.tasktracker.node-group}")
	String taskTrackerNodeGroup;

	@RequestMapping("/createJob")
	public String  createJob() {
		Job job = new Job();
		job.setTaskId(new Date().getTime()+"");
		// 是否要反馈给客户端
		job.setNeedFeedback(false);
		// 是否依赖上一次的
		job.setReplaceOnExist(false);
		// 重试次数
		job.setMaxRetryTimes(0);
		// 优先级
		job.setPriority(100);
		job.setTaskTrackerNodeGroup(taskTrackerNodeGroup);
		Map<String,String> extParams=new HashMap<>();
		
		extParams.put("pppp", "1111");
		
		job.setExtParams(extParams);

		/*
		 * if (StringUtils.isNotBlank(jobDTO.getCronExpression())) { // 执行表达式 和
		 * quartz 的一样 job.setCronExpression(jobDTO.getCronExpression()); } else
		 * { // 定时触发的时间戳 job.setTriggerTime(jobDTO.getTriggerTime()); }
		 */
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 1);

		job.setTriggerTime(nowTime.getTimeInMillis());
		Response response = jobClient.submitJob(job);
		boolean success = response.isSuccess();
		
		System.out.println(success);
		
		return success+"";
	}

	
}
