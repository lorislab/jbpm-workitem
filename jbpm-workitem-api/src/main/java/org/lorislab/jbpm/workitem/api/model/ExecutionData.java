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
package org.lorislab.jbpm.workitem.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * The step data.
 *
 * @author Andrej_Petras
 */
public interface ExecutionData extends Serializable {

    /**
     * Gets the deployment id.
     *
     * @return the deployment id.
     */
    public String getDeploymentId();

    /**
     * Gets the parameters.
     *
     * @return the parameters.
     */
    public Map<String, Object> getParameters();

    /**
     * Gets the item name.
     *
     * @return the item name.
     */
    public String getWorkItemName();

    /**
     * Gets the process instance id.
     *
     * @return the process instance id.
     */
    public long getProcessInstanceId();
}
