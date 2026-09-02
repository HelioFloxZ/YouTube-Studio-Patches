package app.template.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.template.patches.shared.Constants.YT_STUDIO_COMPATIBILITY

object Constants {
    const val YT_STUDIO_PACKAGE_NAME = "com.google.android.apps.youtube.creator"

    val YT_STUDIO_COMPABILITY = Compatibility(
        name = "YouTube Studio",
        packageName = com.google.android.apps.youtube.creator,
        apkFileType = ApkFileType.APKM,
        appIconColor = 0xFF0000,
        targets = listOf(
            AppTarget(
                version = null
            )
        )
    )
}
