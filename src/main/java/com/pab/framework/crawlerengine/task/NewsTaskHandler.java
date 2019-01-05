package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author xumx
 * @date 2018/11/16
 */

@Component
@Configurable
@EnableScheduling
@Slf4j
public class NewsTaskHandler implements TaskHandler {
    @Autowired
    ExecutorService threadFactory;
    @Autowired
    private FlowProcessor flowProcessor;
    @Scheduled(cron = "0 0 8 * * ?")
    @Override
    public void taskRun() {
        threadFactory.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    flowProcessor.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
