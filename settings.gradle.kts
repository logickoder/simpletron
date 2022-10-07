rootProject.name = "simpletron"

include(":core:contract")
include(":core:component")
include(":core:register")
include(":core:instruction")
include(":core:impl")
include(":core:utils")
if (System.getenv("JITPACK").toBoolean().not()) {
    include(":core:cmd_app")
}

include(":compiler:infix-postfix")
include(":compiler:translator")
include(":compiler:compiler")