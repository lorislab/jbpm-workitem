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
package org.lorislab.jbpm.workitem.runtime.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The process service registry.
 *
 * @author Andrej_Petras
 */
public final class ProcessServiceRegistry {

    /**
     * The execute executor.
     */
    private final Map<String, ProcessServiceExecutor> executeExecutor;

    /**
     * The abort executor.
     */
    private final Map<String, ProcessServiceExecutor> abortExecutor;

    /**
     * The default constructor.
     */
    public ProcessServiceRegistry() {
        this.executeExecutor = new HashMap<>();
        this.abortExecutor = new HashMap<>();
    }

    /**
     * Gets the abort executor.
     *
     * @param process the process.
     * @param workItem the work item.
     * @return the corresponding executor.
     */
    public ProcessServiceExecutor getAbort(final String process, final String workItem) {
        return abortExecutor.get(getExecutorId(process, workItem));
    }

    /**
     * Adds the executor.
     *
     * @param executor the executor.
     */
    public void addExecutor(ProcessServiceExecutor executor) {
        if (executor != null) {
            if (executor.isAbort()) {
                if (abortExecutor.containsKey(executor.getId())) {
                    System.out.println("Abort executor for " + executor.getId() + " already registred");
                } else {
                    abortExecutor.put(executor.getId(), executor);
                }
            } else {
                if (executeExecutor.containsKey(executor.getId())) {
                    System.out.println("Execute executor for " + executor.getId() + " already registred");
                } else {
                    executeExecutor.put(executor.getId(), executor);
                }
            }
        }
    }

    /**
     * Gets the execute executor.
     *
     * @param process the process.
     * @param workItem the work item.
     * @return the corresponding executor.
     */
    public ProcessServiceExecutor getExecute(final String process, final String workItem) {
        return executeExecutor.get(getExecutorId(process, workItem));
    }

    /**
     * Gets the executor ID by process and work item.
     *
     * @param process the process.
     * @param workItem the work item.
     * @return the corresponding ID.
     */
    public static String getExecutorId(String process, String workItem) {
        StringBuilder sb = new StringBuilder();
        sb.append(process).append(workItem);
        return sb.toString();
    }

}
