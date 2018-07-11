package guru.stefma.bintrayrelease

import guru.stefma.bintrayrelease.rule.TestProjectRule
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

import static org.assertj.core.api.Assertions.assertThat

@RunWith(Parameterized.class)
class AndroidDifferentGradleVersions {

    @Rule
    public TestProjectRule projectRule = new TestProjectRule(TestProjectRule.Project.ANDROID)

    @Parameterized.Parameters(name = "{index}: test Gradle version {0}")
    static Collection<GradleVerionsParams> gradleVersionExpectedOutcome() {
        return [
                new GradleVerionsParams(gradleVersion: "4.5", expectedTaskOutcome: TaskOutcome.SUCCESS),
                new GradleVerionsParams(gradleVersion: "4.5.1", expectedTaskOutcome: TaskOutcome.SUCCESS),
                new GradleVerionsParams(gradleVersion: "4.6", expectedTaskOutcome: TaskOutcome.SUCCESS),
                new GradleVerionsParams(gradleVersion: "4.7", expectedTaskOutcome: TaskOutcome.SUCCESS),
                new GradleVerionsParams(gradleVersion: "4.8", expectedTaskOutcome: TaskOutcome.SUCCESS),
                new GradleVerionsParams(gradleVersion: "4.8.1", expectedTaskOutcome: TaskOutcome.SUCCESS),
        ]
    }

    private GradleVerionsParams testParams

    AndroidDifferentGradleVersions(GradleVerionsParams testParams) {
        this.testParams = testParams
    }

    @Test
    void givenGradleVersion_WhenProjectBuild_ShouldHaveExpectedOutcome() {
        def runner = GradleRunner.create()
                .withProjectDir(projectRule.projectDir)
                .withArguments("build", "bintrayUpload", "-PbintrayKey=key", "-PbintrayUser=user")
                .withPluginClasspath()
                .withGradleVersion(testParams.gradleVersion)
        if (testParams.expectedGradleBuildFailure) {
            runner.buildAndFail()
        } else {
            assertThat(runner.build().task(":bintrayUpload").outcome).isEqualTo(testParams.expectedTaskOutcome)
        }
    }

}