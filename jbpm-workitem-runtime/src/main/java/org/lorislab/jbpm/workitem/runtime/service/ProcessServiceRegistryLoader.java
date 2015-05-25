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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.lorislab.jbpm.workitem.api.annotation.WorkItem;
import org.lorislab.jbpm.workitem.api.annotation.WorkProcess;
import org.lorislab.jbpm.workitem.api.processor.WorkProcessProcessor;
import org.lorislab.jbpm.workitem.api.model.ExecutionData;
import org.lorislab.jbpm.workitem.runtime.model.ProcessServiceExecutor;
import org.lorislab.jbpm.workitem.runtime.model.ProcessServiceRegistry;

/**
 * The work process loader.
 *
 * @author Andrej_Petras
 */
public final class ProcessServiceRegistryLoader {

    /**
     * The default constructor.
     */
    private ProcessServiceRegistryLoader() {
        // empty constructor
    }

    public static ProcessServiceRegistry load(final ClassLoader loader) {
        ProcessServiceRegistry result = new ProcessServiceRegistry();

        try {
            Enumeration<URL> urls = loader.getResources(WorkProcessProcessor.FILE_NAME);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    Set<String> classes = loadClassesFromURL(url);
                    createObject(result, classes);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private static void createObject(final ProcessServiceRegistry registry, final Set<String> classes) {
        if (classes != null) {
            for (String name : classes) {
                try {
                    Class<?> clazz = Class.forName(name);

                    WorkProcess workProcess = clazz.getAnnotation(WorkProcess.class);
                    if (workProcess != null) {

                        Object service = clazz.newInstance();
                        
                        Method[] methods = clazz.getMethods();
                        if (methods != null && methods.length > 0) {
                            for (Method method : methods) {
                                WorkItem workItem = method.getAnnotation(WorkItem.class);
                                if (workItem != null) {
                                    String id = ProcessServiceRegistry.getExecutorId(workProcess.name(), workItem.name());
                                    
                                    // method input type
                                    boolean input = false;
                                    if (method.getParameterCount() == 1) {
                                        Class<?> parameter = method.getParameterTypes()[0];
                                        input = ExecutionData.class.equals(parameter);
                                    }
                                    
                                    // method result type
                                    boolean output = true;
                                    Class<?> resultType = method.getReturnType();
                                    if (resultType == null || resultType.equals(Void.TYPE)) {
                                        output = false;
                                    }
                                    
                                    // create the executor
                                    ProcessServiceExecutor executor = new ProcessServiceExecutor(id, workProcess.name(), workItem.name(), service, method, workItem.abort(), input, output);
                                    registry.addExecutor(executor);                                    
                                } else {
                                    System.out.println("No work item annotation specified! Class: " + clazz.getName() + ", method:" + method.getName());
                                }
                            }
                        }
                    } else {
                        System.out.println("No work process annotation specified! Class: " + clazz.getName());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static Set<String> loadClassesFromURL(final URL url) {
        Set<String> result = new HashSet<>();
        if (url != null) {

            InputStream in = null;
            BufferedReader reader = null;
            try {
                in = url.openStream();
                reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String serviceClass;
                while ((serviceClass = reader.readLine()) != null) {
                    result.add(serviceClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }
}
