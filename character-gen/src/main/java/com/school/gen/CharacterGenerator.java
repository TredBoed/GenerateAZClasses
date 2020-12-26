package com.school.gen;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Mojo(name = "genFromAToZChar", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class CharacterGenerator extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/", required = true,readonly = true)
    private File pathToGenDir;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Parameter(required = true)
    private String myAdd;

    @Override
    public void execute() {
        try {
            String template = IOUtils.toString(CharacterGenerator.class.getResourceAsStream("/gen.txt"),
                    Charset.defaultCharset());

            for (char c = 'A'; c <= 'Z'; c++) {
                String className = String.valueOf(c);
                String goalClass = template
                        .replace("${className}", className)
                        .replace("${someAdd}", myAdd);

                File directory = new File(pathToGenDir, "com.school");
                FileUtils.forceMkdir(directory);

                File javaClass = new File(directory, className + ".java");
                FileUtils.write(javaClass, goalClass, Charset.defaultCharset());

                mavenProject.addCompileSourceRoot(directory.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

