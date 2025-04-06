package com.cst323.eventsapp.util;



import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy
public class AopConfiguration {

    @Pointcut("execution(* com.cst323..Controllers..*(..)) || execution(* com.cst323..business..*(..)) || execution(* com.cst323..data..*(..)) || execution(* com.cst323..service..*(..))")
    public void monitor(){

    }

    @Bean
    public Tracer tracer(){
        return new Tracer(true);
    } 

    @Bean
    public Advisor performanceMonitorAdvisor()
    {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.gcu.util.AopConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, tracer());
    }
}
