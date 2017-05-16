package org.apereo.cas.generator

import io.spring.initializr.generator.ProjectGenerator
import io.spring.initializr.generator.ProjectRequest
import java.io.File

open class CasProjectGenerator : ProjectGenerator() {
    init {
        setTmpdir(System.getProperty("java.io.tmpdir"))
    }

    override fun resolveModel(request: ProjectRequest?): MutableMap<String, Any> {
        val model = super.resolveModel(request)
        val casRequest: CasProjectRequest = request as CasProjectRequest
        val webapp = casRequest.getProjectCasWebApplicationDependency()
        model.put(TemplateModel.CAS_WEB_APP_DEPENDENCY, webapp)
        return model
    }

    override fun generateGitIgnore(dir: File?, request: ProjectRequest?) {
        super.generateGitIgnore(dir, request)
        val model = resolveModel(request)

        if (isMavenBuild(request)) {
            write(File(dir, "README.md"), "maven/README.md", model)
            write(File(dir, "build.cmd"), "maven/build.cmd", model)
            write(File(dir, "build.sh"), "maven/build.sh", model)
        }

        val cfg = File(dir, "etc/cas/config")
        cfg.mkdirs()
        
        write(File(cfg, "application.yml"), "etc/cas/config/application.yml", model)
        write(File(cfg, "cas.properties"), "etc/cas/config/cas.properties", model)
        write(File(cfg, "log4j2.xml"), "etc/cas/config/log4j2.xml", model)

        write(File(dir, "LICENSE.txt"), "LICENSE.txt", model)
    }

    private fun isMavenBuild(request: ProjectRequest?): Boolean {
        return "maven" == request?.build
    }

    private fun isGradleBuild(request: ProjectRequest): Boolean {
        return "gradle" == request.build
    }
}