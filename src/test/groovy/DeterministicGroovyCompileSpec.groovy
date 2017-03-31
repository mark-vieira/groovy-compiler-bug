import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class DeterministicGroovyCompileSpec extends Specification {
    @Rule
    TemporaryFolder projectDir = new TemporaryFolder()

    @Unroll
    def "usages of Project.file() result in deterministic bytecode"() {
        given:
        projectDir.newFolder('buildSrc', 'src', 'main', 'groovy')
        projectDir.newFile('buildSrc/src/main/groovy/FooTask.groovy') << """
            import groovy.transform.CompileStatic
            import org.gradle.api.DefaultTask
            import org.gradle.api.tasks.TaskAction
            
            @CompileStatic
            class FooTask extends DefaultTask {
                @TaskAction
                void exec() {
                    def someFile = project.file('src/somefile.txt')
                    println someFile.text
                }
            }
        """.stripIndent()

        when:
        runner('help').build()

        then:
        new File(projectDir.root, 'buildSrc/build/classes/main/FooTask.class').bytes == this.getClass().getClassLoader().getResource('FooTask.class').bytes

        where:
        i << (1..10)
    }

    def runner(String... args) {
        GradleRunner.create().withProjectDir(projectDir.root).forwardOutput().withArguments(args)
    }
}
