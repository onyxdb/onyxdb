rootProject.name = "onyxdb"

include(":common")
include(":common:postgres")
include(":mdb")
include(":mongodb-k8s-operator")
include(":idm")
include("common:redis")
findProject(":common:redis")?.name = "redis"
