rootProject.name = "doma-codegen-plugin"

val releaseVersion = settings.startParameter.projectProperties["release.releaseVersion"]

if (releaseVersion != null) {
    include("codegen")
} else {
    pluginManagement {
        includeBuild("codegen")
    }
    include("codegen-h2-test")
    include("codegen-tc-test")
    include("codegen-template-test")
}
