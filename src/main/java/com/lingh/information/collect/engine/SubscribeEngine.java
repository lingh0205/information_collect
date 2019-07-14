package com.lingh.information.collect.engine;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.lingh.information.collect.dao.entity.*;
import com.lingh.information.collect.engine.task.ResourceTask;
import com.lingh.information.collect.service.*;
import com.lingh.information.collect.util.DingDing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class SubscribeEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeEngine.class);

    Cache<String, String> TASK_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(Integer.MAX_VALUE)
            .build();

    @Value("${environment.cpu}")
    Integer cpuNum;

    private ExecutorService executorService;

    private ExecutorService futureExecutorService = Executors.newSingleThreadExecutor();

    @Autowired
    ServiceFacade facade;

    @Autowired
    ResourceService resourceService;

    @Autowired
    ContentService contentService;

    @Autowired
    AccountService accountService;

    @Autowired
    ContactNotifyService contactNotifyService;

    @Autowired
    AccountResourceSubscribeService accountResourceSubscribeService;

    @Autowired
    ContentSubscribeService contentSubscribeService;

    @PostConstruct
    private void init(){
        LOGGER.info("Set Executor Thread num to {}.", cpuNum * 2);
        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(cpuNum * 2, r -> {
            Thread t = new Thread(r);
            t.setDaemon(false);
            return t;
        }));
    }

    public void start(){

    }

    @Scheduled(fixedRate = 1000)
    public void retrySubscribe(){
        List<Account> accountList = accountService.queryActiveAccount();
        accountList.forEach(account -> {
            List<AccountResourceSubscribe> resourceSubscribeList = accountResourceSubscribeService.querySubscribeResources(account.getId());

            Long accountId = account.getId();
            List<ContactNotify> notifies = contactNotifyService.queryNotifyConfigByAccountId(accountId);
            List<String> tokens = new ArrayList<>();
            List<String> mails = new ArrayList<>();
            processContacts(notifies, tokens, mails);
            resourceSubscribeList.forEach(subscribe -> {
                try {
                    Date date = new Date();
                    Long resourceId = subscribe.getResourceId();
                    Resource resource = resourceService.queryById(resourceId);
                    List<ContentSubscribe> retryList = contentSubscribeService.queryRetryContents(accountId, resourceId);

                    if (CollectionUtil.isEmpty(retryList)) {
                        return;
                    }

                    int size = retryList.size();
                    int end = 0;
                    int start = 0;
                    while (end < size) {
                        end = start + 3;   // 一次最多3条订阅消息
                        if (end > size) {
                            end = size;
                        }
                        List<ContentSubscribe> subList = retryList.subList(start, end);
                        List<Content> contents = contentService.queryByIdList(subList);
                        boolean success = notified(tokens, mails, resource, contents);
                        contentSubscribeService.updateContentSubscribeList(subList, success);
                        start = end;
                    }
                    subscribe.setLastSubscribeTime(date);
                    accountResourceSubscribeService.updateLastSubscribeTime(subscribe);
                } catch (Exception e) {
                    LOGGER.error("Failed to send notify msg to Account {} with Resource {}.", account.getUsername(), subscribe.getResourceId(), e);
                }
            });
        });
    }

    private void processContacts(List<ContactNotify> notifies, List<String> tokens, List<String> mails) {
        notifies.forEach(notify -> {
            switch (notify.getNotifyType()) {
                case "dingding":
                    tokens.add(notify.getNotifyValue());
                    break;
                case "mail":
                    mails.add(notify.getNotifyValue());
                    break;
                default:
                    break;
            }
        });
    }

    @Scheduled(fixedRate = 1000)
    public void subscribe(){
        List<Account> accountList = accountService.queryActiveAccount();
        accountList.forEach(account -> {
            List<AccountResourceSubscribe> resourceSubscribeList = accountResourceSubscribeService.querySubscribeResources(account.getId());

            Long accountId = account.getId();
            List<ContactNotify> notifies = contactNotifyService.queryNotifyConfigByAccountId(accountId);
            List<String> tokens = new ArrayList<>();
            List<String> mails = new ArrayList<>();
            processContacts(notifies, tokens, mails);
            resourceSubscribeList.forEach(subscribe -> {
                try {
                    Date date = new Date();
                    Date lastSubscribeTime = subscribe.getLastSubscribeTime();
                    Long resourceId = subscribe.getResourceId();
                    Resource resource = resourceService.queryById(resourceId);
                    List<Content> contents = contentService.queryActiveProperContents(resourceId, lastSubscribeTime);

                    if (CollectionUtil.isEmpty(contents)){
                        return;
                    }

                    int size = contents.size();
                    int end = 0;
                    int start = 0;
                    while (end < size) {
                        end = start + 3;   // 一次最多3条订阅消息
                        if (end > size) {
                            end = size;
                        }
                        List<Content> subList = contents.subList(start, end);
                        boolean success= notified(tokens, mails, resource, subList);
                        contentSubscribeService.saveContentSubscribeList(account, resource, subList, success);
                        start = end;
                    }
                    subscribe.setLastSubscribeTime(date);
                    accountResourceSubscribeService.updateLastSubscribeTime(subscribe);
                }catch (Exception e){
                    LOGGER.error("Failed to send notify msg to Account {} with Resource {}.", account.getUsername(), subscribe.getResourceId(), e);
                }
            });
        });
    }

    private boolean notified(List<String> tokens, List<String> mails, Resource resource, List<Content> subList) {
        boolean successDingDing = false;
        boolean successMail = true;
        if (CollectionUtil.isNotEmpty(tokens)) {
            successDingDing = DingDing.sendMsg(resource, subList, tokens, new ArrayList<>());
        }
        if (CollectionUtil.isNotEmpty(mails)) {
            //TODO
            successMail = false;
        }
        return successDingDing && successMail;
    }

    @Scheduled(fixedRate = 5000)
    public void refreshResource(){
        List<Resource> resourceList = resourceService.queryActiveResource();
        resourceList.forEach(resource -> {
            if (checkExist(resource.getUrl())){
                LOGGER.warn("Task {} has bean execute in 10 minutes, Just no need to execute again!!!", resource.getUrl());
                return;
            }
            ListenableFuture<Boolean> future = this.submit(resource);
            future.addListener(() -> {
                try{
                    if (future.isDone()) {
                        Boolean success = future.get();
                        if (success) {
                            resourceService.updateNextExecTime(resource);
                            return;
                        }
                    }
                }catch (Exception e) {
                    LOGGER.error("Task {} Running found Error.", resource.getUrl());
                }
            }, futureExecutorService);
            TASK_CACHE.put(resource.getUrl(), resource.getUrl());
        });
    }

    private boolean checkExist(String key) {
        String url = TASK_CACHE.getIfPresent(key);
        if (StrUtil.isNotBlank(url)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public ListenableFuture<Boolean> submit(Resource resource){
        ResourceTask task = new ResourceTask(resource);
        task.setTraceId(UUID.randomUUID());
        task.setContentService(contentService);
        return (ListenableFuture<Boolean>) executorService.submit(task);
    }

}
