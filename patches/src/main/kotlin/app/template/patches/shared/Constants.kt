package app.template.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.SupportedAbi

object Constants {
    // Target Package Name for YouTube Studio
    const val YT_STUDIO_PACKAGE_NAME = "com.google.android.apps.youtube.creator"

    // Compatibility definition for the Morphe framework configuration
    val YT_STUDIO_COMPATIBILITY = Compatibility(
        packageName = YT_STUDIO_PACKAGE_NAME,
        // Enforces compatibility across June 2025 builds and generic versions
        versions = emptySet() 
    )
}
