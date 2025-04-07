package com.cst323.eventsapp.util;



import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * This configuration class sets up Aspect-Oriented Programming (AOP) for the application.
 * It defines a pointcut to monitor methods in specific packages and configures an advisor
 * to apply a tracing aspect to those methods.
 */
@Configuration // Indicates that this class provides Spring configuration
@EnableAspectJAutoProxy // Enables AspectJ auto-proxying, allowing Spring to manage aspects
public class AopConfiguration {

    /**
     * Defines a pointcut that matches the execution of any method within the Controllers,
     * business services, data access objects (repositories), and service layers of the
     * com.cst323 package and its sub-packages.
     * The "monitor()" method serves as the identifier for this pointcut.
     */
    @Pointcut("execution(* com.cst323..Controllers..*(..)) || execution(* com.cst323..business..*(..)) || execution(* com.cst323..data..*(..)) || execution(* com.cst323..service..*(..))")
    public void monitor(){

    }

    /**
     * Creates and configures a Tracer bean.
     * The Tracer class (presumably) provides the logic for tracing method execution.
     * The 'true' argument passed to the constructor likely enables the tracing functionality.
     *
     * @return An instance of the Tracer bean.
     */
    @Bean
    public Tracer tracer(){
        return new Tracer(true);
    } 

    /**
     * Configures an Advisor that links the defined pointcut with the Tracer aspect.
     * An Advisor determines when and where an aspect (advice) should be applied.
     * In this case, the Tracer's advice will be applied to all methods matching the "monitor()" pointcut.
     *
     * @return An Advisor instance that combines the pointcut and the tracer.
     */
    @Bean
    public Advisor performanceMonitorAdvisor(){
        // Create an AspectJ expression pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // Set the expression to the "monitor()" pointcut defined in this class
        pointcut.setExpression("com.gcu.util.AopConfiguration.monitor()");
        // Create a DefaultPointcutAdvisor that associates the pointcut with the tracer (advice)
        return new DefaultPointcutAdvisor(pointcut, tracer());
    }
}
