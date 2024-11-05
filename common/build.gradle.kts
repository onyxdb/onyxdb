plugins {
    alias(libs.plugins.onyxdb.javaLibraryConventions)
}

dependencies {
    api(project(":onyxdbCommon:postgres"))
}
