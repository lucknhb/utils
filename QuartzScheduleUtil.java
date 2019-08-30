package com.nhb.quartz.utils;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;

/**
 * @Auther: luck_nhb
 * @Date: 2019/8/30 14:07
 * @Version 1.0
 * @Description: quartz定时器工具类
 */
@Slf4j
public class QuartzScheduleUtil {

    /**
     * 添加调度任务
     *
     * @param scheduleJobClass 任务实现类
     * @param scheduler        任务调度器
     * @param jobName          任务名称   与trigger共用一个jobName
     * @param jobGroup         任务分组   与trigger共用一个jobGroup
     * @param cronExpress      cron表达式
     * @param dataMap          任务参数
     */
    public static void addScheduleJob(Class scheduleJobClass,
                                      Scheduler scheduler,
                                      String jobName,
                                      String jobGroup,
                                      String cronExpress,
                                      Map dataMap) {
        JobDetail jobDetail = JobBuilder.newJob(scheduleJobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        if (null != dataMap && dataMap.size() > 0) {
            jobDetail.getJobDataMap().putAll(dataMap);
        }
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpress))
                .startNow()
                .build();
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            log.error("[添加] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
        }
    }

    /**
     * 触发/立即执行 任务
     *
     * @param jobName
     * @param jobGroup
     * @param scheduler
     */
    public static void triggerScheduleJob(String jobName,
                                          String jobGroup,
                                          Scheduler scheduler) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error("[触发] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
        }
    }

    /**
     * 更新调度任务
     *
     * @param jobName     任务名称
     * @param jobGroup    任务分组
     * @param cronExpress 新的cron表达式
     * @param scheduler   任务调度器
     */
    public static void updateScheduleJob(String jobName,
                                         String jobGroup,
                                         String cronExpress,
                                         Scheduler scheduler) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            cronTrigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpress))
                    .startNow()
                    .build();
            //重启触发器
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        } catch (SchedulerException e) {
            log.error("[更新] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
        }
    }


    /**
     * 取消/删除 任务
     *
     * @param jobName   任务名称
     * @param jobGroup  任务分组
     * @param scheduler 任务调度器
     */
    public static void deleteScheduleJob(String jobName,
                                         String jobGroup,
                                         Scheduler scheduler) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (null == jobDetail)
                return;
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("[删除] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
        }
    }

    /**
     * 暂停单个任务
     *
     * @param jobName   任务名称
     * @param jobGroup  任务分组
     * @param scheduler 任务调度器
     */
    public static void pauseScheduleJob(String jobName,
                                        String jobGroup,
                                        Scheduler scheduler) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (null == jobDetail)
                return;
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error("[暂停] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
        }
    }

    /**
     * 恢复单个调度任务
     *
     * @param jobName   任务名称
     * @param jobGroup  任务分组
     * @param scheduler 任务调度器
     */
    public static void repauseScheduleJob(String jobName,
                                          String jobGroup,
                                          Scheduler scheduler) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (null == jobDetail)
                return;
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error("[恢复] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
        }
    }

    /**
     * 暂停所有任务
     *
     * @param scheduler 任务调度器
     */
    public static void pauseAllScheduleJob(Scheduler scheduler) {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            log.error("[暂停所有] 发生异常:{}", e.getMessage());
        }
    }

    /**
     * 恢复所有任务
     *
     * @param scheduler 任务调度器
     */
    public static void repauseAllScheduleJob(Scheduler scheduler) {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            log.error("[恢复所有] 发生异常:{}", e.getMessage());
        }
    }

    /**
     * 获取单个任务信息
     *
     * @param jobName
     * @param jobGroup
     * @param scheduler
     * @return
     */
    public static Map<String, Object> getSchduleJob(String jobName,
                                                    String jobGroup,
                                                    Scheduler scheduler) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("jobDetail", jobDetail);
            result.put("trigger", trigger);
            return result;
        } catch (SchedulerException e) {
            log.error("[获取] 调度任务名称: {} 分组: {} 发生异常:{}", jobName, jobGroup, e.getMessage());
            return null;
        }
    }

    /**
     * 获取所有任务
     *
     * @param scheduler
     * @return
     */
    public static List<Map<String, Object>> getAllScheduleJob(Scheduler scheduler) {
        List<Map<String, Object>> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("jobName", jobKey.getName());
                    map.put("jobGroup", jobKey.getGroup());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    map.put("jobStatus", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("cronExpress", cronExpression);
                    }
                    jobList.add(map);
                }
            }
        } catch (SchedulerException e) {
            log.error("[获取所有] 发生异常:{}", e.getMessage());
        }
        return jobList;
    }
}
