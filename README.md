<p align="center">
  <a href="https://gitea.io/">
    <img alt="Gitea" src="https://raw.githubusercontent.com/go-gitea/gitea/main/public/img/gitea.svg" width="220"/>
  </a>
</p>
<h1 align="center">EasyTask - Make concurrent task processing simpler</h1>


#### 介绍
EasyTask是一种Java语言编写的并发任务执行框架，目的在于提高任务处理性能，采用多线程，屏蔽处理细节，封装线程池和阻塞队列每个批量任务拥有自己的上下文环境，并发安全的容器保存每个任务，自动清除已完成和过期任务实现DelayQueue延时队列，实现简单，方便注册提交工作任务，提供查询任务执行情况。
#### 功能
提高性能，采用多线程，屏蔽细节
	
	封装线程池和阻塞队列

每个批量任务拥有自己的上下文环境

	并发安全的容器保存每个任务

自动清除已完成和过期任务
 	
	实现DelayQueue延时队列

#### 优点

	实现简单，方便注册提交工作任务，提供查询任务执行情况


#### 使用说明

测试用例在test

