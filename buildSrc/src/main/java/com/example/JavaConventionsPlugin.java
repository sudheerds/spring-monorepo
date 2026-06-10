package com.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.JavaVersion;

public class JavaConventionsPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getPlugins().apply(JavaPlugin.class);

        project.getExtensions().configure(JavaPluginExtension.class, java -> {
            java.setSourceCompatibility(JavaVersion.VERSION_21);
            java.setTargetCompatibility(JavaVersion.VERSION_21);
        });

        project.getRepositories().mavenCentral();

        project.getTasks().withType(Test.class).configureEach(test -> {
            test.useJUnitPlatform();
            test.testLogging(logging -> {
                logging.setShowStandardStreams(true);
            });
        });
    }
}