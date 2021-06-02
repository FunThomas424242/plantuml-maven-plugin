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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static io.takari.maven.testing.TestResources.assertFilesPresent;

// http://takari.io/book/70-testing.html
@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.6.3"})
public class Back2BackCompatibilityTest {

    @Rule
    public final TestResources resources = new TestResources("src/test/resources/integration", "target/test-integration");

    public final MavenRuntime maven;

    public Back2BackCompatibilityTest(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
        this.maven = builder.withCliOptions("-B", "-U").build();
    }

    @Test
    public void checkHuluvu424242Mojo() throws Exception {
        final File basedir = resources.getBasedir("truncate-project");
        final MavenExecution mavenExecution = maven
                .forProject(basedir)
                .withCliOption("-Pfunthomas424242");
        final MavenExecutionResult result = mavenExecution.execute("clean", "com.github.funthomas424242:plantuml-maven-plugin:generate");
        result.assertErrorFreeLog();
        assertFilesPresent(basedir, "target/plantuml/AblaufManuelleGenerierung.png");
        assertFilesPresent(basedir, "target/plantuml/QueueStatechart.png");
    }

    @Test
    public void checkBvFalconMojo() throws Exception {
        final File basedir = resources.getBasedir("truncate-project");
        final MavenExecution mavenExecution = maven
                .forProject(basedir)
                .withCliOption("-Pbvfalcon");
        final MavenExecutionResult result = mavenExecution.execute("clean", "com.github.jmdesprez:plantuml-maven-plugin:generate");
        result.assertErrorFreeLog();
        assertFilesPresent(basedir, "target/plantuml/AblaufManuelleGenerierung.png");
        assertFilesPresent(basedir, "target/plantuml/QueueStatechart.png");
    }

    @Test
    public void checkJeluardMojo() throws Exception {
        final File basedir = resources.getBasedir("truncate-project");
        final MavenExecution mavenExecution = maven
                .forProject(basedir)
                .withCliOption("-Pjeluard");
        final MavenExecutionResult result = mavenExecution.execute("clean", "com.github.jeluard:plantuml-maven-plugin:generate");
        result.assertErrorFreeLog();
        // HINT: jeluard plugin does not support truncatePattern
        assertFilesPresent(basedir, "target/plantuml/src/main/plantuml/AblaufManuelleGenerierung.png");
        assertFilesPresent(basedir, "target/plantuml/src/main/plantuml/QueueStatechart.png");
    }
}


