package app.template.patches.example

import app.morphe.patcher.fingerprint.methodFingerprint
import com.android.tools.smali.dexlib2.Opcode

val updateDialogFingerprint = methodFingerprint(
    returnType = "V",
    strings = listOf("market://details?id=com.google.android.apps.youtube.creator", "play.google.com")
)

val forceUpdateCheckFingerprint = methodFingerprint(
    returnType = "Z",
    strings = listOf("force_update", "is_update_required")
)

val gmsCoreAuthFingerprint = methodFingerprint(
    strings = listOf("com.google.android.gms.auth", "oauth2:"),
    opcodes = listOf(
        Opcode.CONST_STRING,
        Opcode.INVOKE_STATIC
    )
)

val clearcutTelemetryFingerprint = methodFingerprint(
    returnType = "V",
    strings = listOf("ClearcutLogger", "logEvent")
)