/*
 * Created on Aug 25, 2011
 * Copyright 2010 by Eduard Weissmann (edi.weissmann@gmail.com).
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.sejda.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Enumeration of all tasks, configured for testing
 * 
 * @author Eduard Weissmann
 * 
 */
public enum TestableTask {

    DECRYPT,
    ENCRYPT,
    ROTATE,
    SETVIEWERPREFERENCES,
    ALTERNATEMIX(new MultipleInputsAndFileOutputDefaultsProvider()),
    UNPACK,
    MERGE(new MultipleInputsAndFileOutputDefaultsProvider()),
    SPLIT_BY_BOOKMARKS(new SingleInputAndFolderOutputDefaultsProvider());

    private final DefaultsProvider defaultsProvider;

    private TestableTask() {
        // defaults
        this.defaultsProvider = new DefaultDefaultsProvider();
    }

    private TestableTask(DefaultsProvider defaultsProvider) {
        this.defaultsProvider = defaultsProvider;
    }

    public CommandLineTestBuilder getCommandLineDefaults() {
        return defaultsProvider.provideDefaults(getTaskName());
    }

    String getTaskName() {
        return name().toLowerCase().replaceAll("_", "");
    }

    public static List<Object[]> allTasks() {
        return allTasksExceptFor();
    }

    public static List<Object[]> allTasksExceptFor(TestableTask... exceptFor) {
        Collection<TestableTask> exceptForCollection = Arrays.asList(exceptFor);
        List<Object[]> result = new ArrayList<Object[]>();
        for (TestableTask each : TestableTask.values()) {
            if (!exceptForCollection.contains(each)) {
                result.add(new Object[] { each });
            }
        }

        return result;
    }
}

interface DefaultsProvider {

    CommandLineTestBuilder provideDefaults(String taskName);
}

class DefaultDefaultsProvider implements DefaultsProvider {

    public CommandLineTestBuilder provideDefaults(String taskName) {
        return new CommandLineTestBuilder(taskName).defaultTwoInputs().defaultFolderOutput();
    }

}

class MultipleInputsAndFileOutputDefaultsProvider implements DefaultsProvider {

    public CommandLineTestBuilder provideDefaults(String taskName) {
        return new CommandLineTestBuilder(taskName).defaultTwoInputs().defaultFileOutput();
    }

}

class SingleInputAndFolderOutputDefaultsProvider implements DefaultsProvider {

    public CommandLineTestBuilder provideDefaults(String taskName) {
        return new CommandLineTestBuilder(taskName).defaultSingleInput().defaultFolderOutput();
    }

}