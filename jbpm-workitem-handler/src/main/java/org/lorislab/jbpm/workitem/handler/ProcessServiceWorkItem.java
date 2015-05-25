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
package org.lorislab.jbpm.workitem.handler;

import java.util.Map;
import org.drools.core.process.instance.impl.WorkItemImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.lorislab.jbpm.workitem.runtime.service.ProcessExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The process service work item.
 *
 * @author Andrej_Petras
 */
public class ProcessServiceWorkItem implements WorkItemHandler {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessServiceWorkItem.class);

    /**
     * The KIE session.
     */
    private KieSession ksession;

    /**
     * The project class loader.
     */
    private ClassLoader classLoader;

    /**
     * The default constructor.
     */
    public ProcessServiceWorkItem() {
        // empty constructor
    }

    /**
     * The default constructor.
     *
     * @param ksession the KIE session.
     * @param classLoader the project class loader.
     */
    public ProcessServiceWorkItem(KieSession ksession, ClassLoader classLoader) {
        this.ksession = ksession;
        this.classLoader = classLoader;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        try {
            WorkItemImpl impl = (WorkItemImpl) workItem;
            ProcessInstance instance = getProcessInstance(workItem);

            String name = getWorkItemName(impl, instance);
            String deploymentId = impl.getDeploymentId();

            Map<String, Object> result = ProcessExecutionService.execute(classLoader, deploymentId, instance.getProcessId(), workItem.getParameters(), name, workItem.getProcessInstanceId());
            manager.completeWorkItem(workItem.getId(), result);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        try {
            WorkItemImpl impl = (WorkItemImpl) workItem;
            ProcessInstance instance = getProcessInstance(workItem);

            String name = getWorkItemName(impl, instance);
            String deploymentId = impl.getDeploymentId();

            ProcessExecutionService.abort(classLoader, deploymentId, instance.getProcessId(), workItem.getParameters(), name, workItem.getProcessInstanceId());
            manager.abortWorkItem(workItem.getId());
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /**
     * Gets the work item name.
     *
     * @param workItem the work item.
     * @param instance the process instance.
     * @return the work item name.
     */
    private String getWorkItemName(WorkItemImpl workItem, ProcessInstance instance) {
        WorkflowProcessInstance wpi = (WorkflowProcessInstance) instance;
        NodeInstance ni = wpi.getNodeInstance(workItem.getNodeInstanceId());
        String name = ni.getNodeName();
        return name;
    }

    /**
     * Gets the process instance.
     *
     * @param workItem the work item.
     * @return the corresponding process instance.
     */
    private ProcessInstance getProcessInstance(WorkItem workItem) {
        ProcessInstance instance = ksession.getProcessInstance(workItem.getProcessInstanceId());
        return instance;
    }

    /**
     * Handles the execute exception.
     *
     * @param ex the exception.
     * @throws RuntimeException the standard process exception.
     */
    private void handleException(Exception ex) throws RuntimeException {
        RuntimeException exception;
        LOGGER.error("Error execute work item: " + ex.getMessage(), ex);
        if (ex instanceof RuntimeException) {
            exception = (RuntimeException) ex;
        } else {
            exception = new RuntimeException(ex);
        }
        throw exception;
    }
}
