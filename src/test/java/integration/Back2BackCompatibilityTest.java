package integration;

/*-
 * #%L
 * Maven PlantUML plugin
 * %%
 * Copyright (C) 2011 - 2019 Julien Eluard
 * %%
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
 * #L%
 */

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecution;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static io.takari.maven.testing.TestResources.assertFilesPresent;

// http://takari.io/book/70-testing.html
@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({ "3.1.1", "3.2.5", "3.3.9", "3.5.4", "3.6.3", "3.8.1" })
public class Back2BackCompatibilityTest {

    @Rule
    public final TestResources resources = new TestResources("src/test/resources/integration", "target/test-integration");

    public final MavenRuntime maven;

    public Back2BackCompatibilityTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
        maven = builder.withCliOptions("-B", "-U").build();
    }

    @Test
    public void checkHuluvu424242Mojo() throws Exception {
        checkMojo("funthomas424242", true);
    }

    @Test
    public void checkJmdesprezMojo() throws Exception {
        checkMojo("jmdesprez", true);
    }

    @Test
    public void checkJeluardMojo() throws Exception {
        checkMojo("jeluard", false);
    }

    private void checkMojo(String githubID, boolean canTruncatePattern) throws Exception {
        File baseDir = resources.getBasedir("truncate-project");
        MavenExecution mavenExecution = maven.forProject(baseDir).withCliOption("-P" + githubID);
        String pluginMavenCoordinates = "com.github." + githubID + ":plantuml-maven-plugin:generate";
        MavenExecutionResult result = mavenExecution.execute("clean", pluginMavenCoordinates);
        result.assertErrorFreeLog();

        // 'jdot' binary not found 
        result.assertNoLogText("java.io.IOException");

        // Problems interacting with Semtana API
        result.assertNoLogText("java.lang.InvocationTargetException");
        result.assertNoLogText("java.lang.UnsupportedOperationException");
        result.assertNoLogText("java.lang.ClassFormatError");

        String subDir = "target/plantuml/" + (canTruncatePattern ? "" : "src/main/plantuml/");
        assertFilesPresent(baseDir, subDir + "AblaufManuelleGenerierung.png");
        assertFilesPresent(baseDir, subDir + "QueueStatechart.png");
    }
}


