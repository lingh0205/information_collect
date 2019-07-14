package com.lingh.information.collect.engine;

import com.google.common.util.concurrent.ListenableFuture;
import com.lingh.information.collect.dao.entity.Resource;
import com.lingh.information.collect.service.ResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EngineTests {

    @Autowired
    SubscribeEngine engine;

    @Autowired
    ResourceService resourceService;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    public void testSubmitXmlTask() throws InterruptedException {
//        String url = "http://www.ityouknow.com/feed.xml";
        String url = "https://coolshell.cn/feed";
        Resource resource = resourceService.selectByUrl(url);
        final ListenableFuture<Boolean> future = engine.submit(resource);
        future.addListener(() -> {
            try{
                while (true) {
                    if (future.isDone()) {
                        System.out.println(future.get());
                        break;
                    }
                    Thread.sleep(10);
                }
            }catch (Exception e){
                ;
            }
        }, executorService);
        Thread.sleep(100000);
    }

    @Test
    public void testExecuteAll() throws InterruptedException {
        engine.refreshResource();
        Thread.sleep(100000);
    }

}
