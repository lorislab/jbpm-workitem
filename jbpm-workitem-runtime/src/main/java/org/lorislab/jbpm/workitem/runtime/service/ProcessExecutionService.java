/*
 * Copyright 2015 Andrej_Petras.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.jbpm.workitem.runtime.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lorislab.jbpm.workitem.api.model.ExecutionData;
import org.lorislab.jbpm.workitem.runtime.model.ExecutionDataImpl;
import org.lorislab.jbpm.workitem.runtime.model.ProcessServiceExecutor;
import org.lorislab.jbpm.workitem.runtime.model.ProcessServiceRegistry;

/**
 *
 * @author Andrej_Petras
 */
public class ProcessExecutionService {
    
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(ProcessExecutionService.class.getName());

    /**
     * The process service registry.
     */
    private static ProcessServiceRegistry REGISTER = null;
    
    /**
     * The time constant.
     */
    private static final double TIME_CONST = 1000000000d;
    
    public static Map<String, Object> execute(ClassLoader classLoader, String deploymentId, String processId, Map<String, Object> parameters, String itemName, long processInstanceId) throws Exception {
        Map<String, Object> result = null;        
        if (REGISTER == null) {
            loadRegister(classLoader);
        }
        ProcessServiceExecutor executor = REGISTER.getExecute(processId, itemName);
        if (executor != null) {            
            result = execute(executor, deploymentId, processId, parameters, itemName, processInstanceId);
        } else {
            LOGGER.log(Level.WARNING, "Not supported execute method for {0}/{1}", new Object[]{processId, itemName});
        }
        return result;
    }
    
    public static void abort(ClassLoader classLoader, String deploymentId, String processId, Map<String, Object> parameters, String itemName, long processInstanceId) throws Exception {       
        if (REGISTER == null) {
            loadRegister(classLoader);
        }        
        ProcessServiceExecutor executor = REGISTER.getAbort(processId, itemName);        
        if (executor != null) {                                    
            execute(executor, deploymentId, processId, parameters, itemName, processInstanceId);
        } else {
            LOGGER.log(Level.WARNING, "Not supported abort method for {0}/{1}", new Object[]{processId, itemName});
        }
    }
    

    /**
     * Executes the service method with execution data.
     *
     * @param data the execution data.
     * @return the result.
     * @throws java.lang.Exception if the execution fails.
     */
    private static Map<String, Object> execute(final ProcessServiceExecutor executor, String deploymentId, String processId, Map<String, Object> parameters, String itemName, long processInstanceId) throws Exception {
        final long startTime = System.nanoTime();
        final Object service = executor.getService();
        final Method method = executor.getMethod();
                
        // log method
        Logger logger = Logger.getLogger(service.getClass().getName());
        logger.info(method.getName());
        
        Map<String, Object> result = null;
        try {
            
            // create the input data
            ExecutionData tmp = null;
            if (executor.isInput()) {
                tmp = new ExecutionDataImpl(deploymentId, parameters, itemName, processInstanceId);
            }
            
            // execute the method
            if (executor.isOutput()) {
                result = (Map<String, Object>) method.invoke(service, tmp);
            } else {
                method.invoke(service, tmp);
            }

        } catch (IllegalAccessException | IllegalArgumentException ex) {
            LOGGER.log(Level.SEVERE, "System error by execute the work item handler method", ex);
            throw ex;
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.FINEST, "Error invocation the target method", e);
            
            Throwable ex = e.getTargetException();
            Exception exception;
            if (ex instanceof Exception) {
                exception = (Exception) ex;
            } else {
                exception = new Exception("Error in execution the work item handler.", ex);
            }
            
            LOGGER.log(Level.SEVERE, "Error by execution the work item handler method", exception);
            
            logger.log(Level.INFO, "{0} [{1}s] failed.", new Object[]{method.getName(), geTime(startTime)});
            throw exception;
        }
        
        logger.log(Level.INFO, "{0} [{1}s] succeed.", new Object[]{method.getName(), geTime(startTime)});
        return result;
    }
    
    /**
     * Loads the register.
     * @param classLoader the project class loader.
     */
    private static synchronized void loadRegister(ClassLoader classLoader) {
        if (REGISTER == null) {
            REGISTER = ProcessServiceRegistryLoader.load(classLoader);
        }
    }
    
    /**
     * Gets the duration from
     * <code>startTime</code> to now.
     *
     * @param startTime start time.
     * @return the duration from <code>startTime</code> to now.
     */
    private static double geTime(long startTime) {
        return (1.0d * (System.nanoTime() - startTime)) / TIME_CONST;
    }    
}
