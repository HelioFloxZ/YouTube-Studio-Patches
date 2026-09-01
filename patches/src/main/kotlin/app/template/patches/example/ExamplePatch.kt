package app.template.patches.example

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.COMPATIBILITY_EXAMPLE

@Suppress("unused")
val youtubeStudioPatch = bytecodePatch(
    name = "YouTube Studio Update Blocker",
    description = "Blocks the identified update action and Play Store update redirect.",
    default = true
) {
    execute {
        StartUpdateFingerprint.method.addInstruction(
            0,
            "return-void"
        )

        PlayStoreRedirectFingerprint.method.addInstruction(
            0,
            "return-void"
        )
    }
}