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

import java.lang.reflect.Method;

/**
 * The process service executor.
 *
 * @author Andrej_Petras
 */
public final class ProcessServiceExecutor {

    /**
     * The ID.
     */
    private final String id;

    /**
     * The process.
     */
    private final String process;

    /**
     * The work item.
     */
    private final String workItem;

    /**
     * The service.
     */
    private final Object service;

    /**
     * The service method.
     */
    private final Method method;

    /**
     * The abort flag.
     */
    private final boolean abort;

    /**
     * The input flag.
     */
    private final boolean input;

    /**
     * The output flag.
     */
    private final boolean output;

    /**
     * The default constructor.
     *
     * @param id the ID.
     * @param process the process.
     * @param workItem the work item.
     * @param service the service instance.
     * @param method the service method.
     * @param abort the abort flag.
     * @param input the input parameter flag.
     * @param output
     */
    public ProcessServiceExecutor(String id, String process, String workItem, Object service, Method method, boolean abort, boolean input, boolean output) {
        this.id = id;
        this.process = process;
        this.workItem = workItem;
        this.service = service;
        this.method = method;
        this.input = input;
        this.output = output;
        this.abort = abort;
    }

    public Method getMethod() {
        return method;
    }

    public Object getService() {
        return service;
    }

    public boolean isInput() {
        return input;
    }

    public boolean isOutput() {
        return output;
    }

    /**
     * Gets the abort executor flag.
     *
     * @return the abort executor flag.
     */
    public boolean isAbort() {
        return abort;
    }

    /**
     * Gets the ID.
     *
     * @return the ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the process.
     *
     * @return the process.
     */
    public String getProcess() {
        return process;
    }

    /**
     * Gets the work item.
     *
     * @return the work item.
     */
    public String getWorkItem() {
        return workItem;
    }


}
