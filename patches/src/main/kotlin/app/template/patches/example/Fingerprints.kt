package app.template.patches.example

import app.morphe.patcher.fingerprint.methodFingerprint

val exampleFingerprint = methodFingerprint(
    returnType = "Z",
    parameters = listOf()
)

val exampleIntegrationsFingerprint = methodFingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf()
)