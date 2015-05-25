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
package org.lorislab.jbpm.workitem.example.service;

import java.util.Map;
import org.lorislab.jbpm.workitem.api.annotation.WorkItem;
import org.lorislab.jbpm.workitem.api.annotation.WorkProcess;
import org.lorislab.jbpm.workitem.api.model.ExecutionData;

/**
 * The example service for the example1 process.
 * 
 * @author Andrej_Petras
 */
@WorkProcess(name = "example1")
public class ExampleService {
    
    @WorkItem(name = "step1")
    public Map<String, Object> test1(ExecutionData data) {
        System.out.println("Execute step1 step!");
        return null;
    }
    
    @WorkItem(name = "step2")
    public Map<String, Object> test2(ExecutionData data) {
        System.out.println("Execute step2 step!");
        return null;
    }
    
    @WorkItem(name = "step3")
    public void test3(ExecutionData data) {
        System.out.println("Execute step3 step!");
    }
    
    @WorkItem(name = "step4")
    public void test4() {
        System.out.println("Execute step4 step!");
    }
    
    @WorkItem(name = "step5")
    public Map<String, Object> test5() {
        System.out.println("Execute step5 step!");
        return null;
    }    
}
