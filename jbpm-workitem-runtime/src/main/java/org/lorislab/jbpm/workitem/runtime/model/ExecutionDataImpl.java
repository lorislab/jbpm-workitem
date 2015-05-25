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

import java.util.Map;
import org.lorislab.jbpm.workitem.api.model.ExecutionData;

/**
 * The execution data implementation.
 *
 * @author Andrej_Petras
 */
public class ExecutionDataImpl implements ExecutionData {

    /**
     * The UID for this class.
     */
    private static final long serialVersionUID = -1349466531947972401L;

    /**
     * The work item parameters.
     */
    private final Map<String, Object> parameters;

    /**
     * The item name.
     */
    private final String itemName;

    /**
     * The process instance id.
     */
    private final long processInstanceId;

    /**
     * The deployment id.
     */
    private final String deploymentId;

    /**
     * Constructor.
     *
     * @param deploymentId the deployment id.
     * @param parameters input parameter map
     * @param itemName item name
     * @param processInstanceId process instance id
     */
    public ExecutionDataImpl(String deploymentId, Map<String, Object> parameters, String itemName, long processInstanceId) {
        this.deploymentId = deploymentId;
        this.parameters = parameters;
        this.itemName = itemName;
        this.processInstanceId = processInstanceId;
    }

    /**
     * Gets the deployment ID.
     *
     * @return the deployment ID.
     */
    @Override
    public String getDeploymentId() {
        return deploymentId;
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters.
     */
    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Gets the work item name.
     *
     * @return the work item name.
     */
    @Override
    public String getWorkItemName() {
        return itemName;
    }

    /**
     * Gets the process instance ID.
     *
     * @return the process instance ID.
     */
    @Override
    public long getProcessInstanceId() {
        return processInstanceId;
    }

}
