rootProject.name = "doma-codegen-plugin"

pluginManagement {
    includeBuild("codegen")
}

include("codegen-h2-test")
include("codegen-tc-test")
include("codegen-template-test")
