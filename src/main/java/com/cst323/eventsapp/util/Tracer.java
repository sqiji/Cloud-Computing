package com.cst323.eventsapp.util;

import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;


/**
 * This class extends Spring's AbstractMonitoringInterceptor to provide tracing
 * functionality for method executions. It logs the start and end times of method
 * invocations, as well as the total execution time. It also includes a warning
 * if the execution time exceeds a defined threshold (10 milliseconds).
 */
public class Tracer extends AbstractMonitoringInterceptor {


    private static final long serialVersionUID = -5378974652504403928L;

    /**
     * Default constructor for the Tracer.
     */
    public Tracer() {
    }

	/**
     * Constructor for the Tracer that allows specifying whether to use a dynamic
     * Spring logger.
     * @param useDynamicLogger If true, the interceptor will use a dynamic Spring logger;
     * otherwise, it will use the default logger.
     */
    public Tracer(boolean useDynamicLogger) 
    {
    	setUseDynamicLogger(useDynamicLogger);
    }

    /**
     * This method is called around the method invocation being traced. It logs
     * information before and after the method execution.
     * @param invocation The MethodInvocation object representing the method being invoked.
     * @param log        The Log object used for logging trace and warning messages.
     * @return The result of the method invocation.
     * @throws Throwable If the invoked method throws an exception, it is propagated.
     */
    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log log) throws Throwable 
    {
    	// Create a descriptive name for the method invocation for logging purposes.
        String name = createInvocationTraceName(invocation);
        // Record the start time of the method execution.
        long start = System.currentTimeMillis();
        // Log a trace message indicating the start of the method execution, including the method name and start time.
        log.trace("GCU Method " + name + " execution started at:" + new Date());
        try {
        	// Proceed with the actual method invocation. The return value of this call is the
            // result of the original method.
            return invocation.proceed();
        }
        finally {
            // This block is executed regardless of whether the method invocation completes
            // normally or throws an exception.
            // Record the end time of the method execution.
            long end = System.currentTimeMillis();
             // Calculate the total execution time of the method.
            long time = end - start;
            // Log a trace message indicating the duration of the method execution in milliseconds.
            log.trace("GCU Method " + name + " execution lasted:" + time + " ms");
             // Log a trace message indicating the end time of the method execution.
            log.trace("GCU Method " + name + " execution ended at:" + new Date()); 
            // Check if the execution time exceeds a predefined threshold (10 milliseconds).           
            if (time > 10){
                // Log a warning message if the method execution took longer than the threshold,
                // indicating potential performance issues.
                log.warn("GCU Method execution longer than 10 ms!");
            }
        }
    }
    
}
