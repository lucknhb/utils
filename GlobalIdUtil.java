package com.xsy.power.utils;

import com.xsy.power.exception.GlobalIdException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version 1.0
 * @description: 分布式环境下为数据库主键提供全局唯一Id值(基于雪花算法)<br>
 * 算法核心如下:<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1bit 标识位（最高位） 0表示正数 1表示负数<br>
 * 41bit 时间戳差值（当前时间-开始时间）<br>
 * 5bit 数据中心编号<br>
 * 5bit 机器标识<br>
 * 12bit 序列号(同一机器同一毫秒内产生不同的序列号，12 bit 可以支持 4096 个序列号)<br>
 * @author: luck_nhb
 * @date: 2019/9/4 08:33
 */
@Slf4j
public class GlobalIdUtil {

    /**
     * 各数据所占位数
     */
    private final long markBits = 1L;
    private final long timeStampBits = 41L;
    private final long dataCenterIdBits = 5L;
    private final long workerIdBits = 5L;
    private final long sequenceNumberBits = 12L;
    /**
     * 移位操作
     */
    private final long timeStampShift = workerIdBits + dataCenterIdBits + sequenceNumberBits;
    private final long dataCenterIdShift = workerIdBits + sequenceNumberBits;
    private final long workerIdShift = sequenceNumberBits;
    /**
     * 开始时间戳  2019-9-4
     */
    private final long startTime = 1567559000000L;
    /**
     * 数据中心编码 范围 2^5 - 1 即 0-31
     */
    private long dataCenterId;
    /**
     * 机器编码 范围 2^5 - 1 即 0-31
     */
    private long workerId;
    /**
     * 序列号 范围 2^12-1 0-4095
     */
    private long sequenceNumber = 0L;
    /**
     * 上一次生成Id的时间戳
     */
    private long lastTimeStamp = -1L;
    /**
     * 数据中心最大序号 2^5 -1
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxSequenceNumber = -1L ^ (-1L << sequenceNumberBits);

    /**
     * 统计阻塞次数
     */
    private final AtomicLong waitCount = new AtomicLong(0);


    public GlobalIdUtil(long dataCenterId, long workerId) {
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new GlobalIdException("数据中心编号有误");
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new GlobalIdException("机器编号有误");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long currentTimeStamp = currentTimeStamp();
        if (currentTimeStamp < lastTimeStamp) {
            throw new GlobalIdException("拒绝生成Id,当前时间小于上一次生成时间");
        } else if (currentTimeStamp == lastTimeStamp) {
            sequenceNumber = (sequenceNumber + 1) & maxSequenceNumber;
            if (sequenceNumber == 0) {
                currentTimeStamp = waitNextMills(currentTimeStamp);
            }
        } else {
            sequenceNumber = 0L;
        }
        lastTimeStamp = currentTimeStamp;
        return ((currentTimeStamp - startTime) << timeStampShift)|
                (dataCenterId << dataCenterIdShift)|
                (workerId << workerIdShift)|
                sequenceNumber;
    }

    private long waitNextMills(long currentTimeStamp) {
        waitCount.incrementAndGet();
        while (currentTimeStamp <= lastTimeStamp) {
            currentTimeStamp = currentTimeStamp();
        }
        return currentTimeStamp;
    }

    private long currentTimeStamp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        GlobalIdUtil globalIdUtil = new GlobalIdUtil(1L,1L);
        Set<Long> count = new HashSet<>();
        for (int i = 0;i< 10000;i++){
            long nextId = globalIdUtil.nextId();
            count.add(nextId);
            log.info(nextId+"");
        }
        System.out.println("总量:"+count.size());
    }
}
