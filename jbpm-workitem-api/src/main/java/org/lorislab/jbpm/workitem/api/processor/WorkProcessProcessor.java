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
package org.lorislab.jbpm.workitem.api.processor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.lorislab.jbpm.workitem.api.annotation.WorkProcess;

/**
 * The work process processor.
 *
 * @author Andrej_Petras
 */
@SupportedAnnotationTypes("org.lorislab.jbpm.extension.api.annotation.WorkProcess")
public class WorkProcessProcessor extends AbstractProcessor {

    /**
     * The 
     */
    public static final String FILE_NAME = "META-INF/services/" + WorkProcess.class.getName();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        try {
            return SourceVersion.valueOf("RELEASE_8");
        } catch (IllegalArgumentException x) {
            // do nothing
        }
        try {
            return SourceVersion.valueOf("RELEASE_7");
        } catch (IllegalArgumentException x) {
            // do nothing
        }
        return SourceVersion.RELEASE_6;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        Set<String> services = new HashSet();
        Elements elements = processingEnv.getElementUtils();

        // create the list of services
        Set<? extends Element> es = roundEnv.getElementsAnnotatedWith(WorkProcess.class);
        for (Element element : es) {
            if (element.getKind().isClass()) {
                WorkProcess anno = element.getAnnotation(WorkProcess.class);
                if (anno != null) {
                    TypeElement type = (TypeElement) element;
                    String cn = elements.getBinaryName(type).toString();
                    services.add(cn);
                }
            }
        }

        // read the services file
        Filer filer = processingEnv.getFiler();
        try {
            FileObject f = filer.getResource(StandardLocation.CLASS_OUTPUT, "", FILE_NAME);
            BufferedReader r = new BufferedReader(new InputStreamReader(f.openInputStream(), "UTF-8"));
            String line;
            while ((line = r.readLine()) != null) {
                services.add(line);
            }
            r.close();
        } catch (FileNotFoundException x) {
            // doesn't exist
        } catch (IOException x) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Failed to load existing service definition files: " + x);
        }

        // write the services file.
        try {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Writing " + FILE_NAME);
            FileObject f = filer.createResource(StandardLocation.CLASS_OUTPUT, "", FILE_NAME);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(f.openOutputStream(), "UTF-8"));
            for (String e : services) {
                pw.println(e);
            }
            pw.close();
        } catch (IOException x) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Failed to write service definition " + FILE_NAME + " error: " + x);
        }

        return false;
    }

}
