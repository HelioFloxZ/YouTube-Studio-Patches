package app.template.patches.example

import app.morphe.patcher.fingerprint.methodFingerprint

val updateCheckFingerprint = methodFingerprint(
    returnType = "Z",
    strings = listOf("force_update", "is_update_required")
)

val playStoreRedirectFingerprint = methodFingerprint(
    returnType = "V",
    strings = listOf("market://details?id=com.google.android.apps.youtube.creator")
)

val telemetryLoggerFingerprint = methodFingerprint(
    returnType = "V",
    strings = listOf("ClearcutLogger", "logEvent")
)

val gmsCoreRedirectionFingerprint = methodFingerprint(
    returnType = "Ljava/lang/String;",
    strings = listOf("com.google.android.gms")
)