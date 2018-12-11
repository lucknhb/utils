package com.linghong.molian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


@Configuration
public class QuartzConfig {

//    @Bean(name = "jobDetail")
//    public MethodInvokingJobDetailFactoryBean detailFactoryBean(SchedulerTask task) {
//        // ScheduleTask为需要执行的任务
//        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//        /*
//         *  是否并发执行
//         *  例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
//         *  如果此处为true，则下一个任务会bing执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
//         */
//        jobDetail.setConcurrent(true);
//        jobDetail.setName("scheduler");// 设置任务的名字
//        jobDetail.setGroup("scheduler_group");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
//        //设置目标对象
//        jobDetail.setTargetObject(task);
//        //设置目标对象中需要执行的方法
//        jobDetail.setTargetMethod("execute");
//        return jobDetail;
//    }
//
//    @Bean(value = "cronTrigger")
//    public CronTriggerFactoryBean cronJobTrigger(MethodInvokingJobDetailFactoryBean jobDetail) {
//        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
//        trigger.setJobDetail(jobDetail.getObject());
//        // 表示每隔2秒钟执行一次  暂时不启用
//        //trigger.setCronExpression("0/2 * * * * ?");
//        trigger.setName("schedulerTrigger");
//        return trigger;
//
//    }
//
//    @Bean(name = "scheduler")
//    public SchedulerFactoryBean schedulerFactory(Trigger cronJobTrigger) {
//        SchedulerFactoryBean bean = new SchedulerFactoryBean();
//        //设置是否任意一个已定义的Job会覆盖现在的Job。默认为false，即已定义的Job不会覆盖现有的Job。
//        bean.setOverwriteExistingJobs(true);
//        // 注册定时触发器
//        bean.setTriggers(cronJobTrigger);
//        return bean;
//    }

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        return schedulerFactoryBean;
    }
}
