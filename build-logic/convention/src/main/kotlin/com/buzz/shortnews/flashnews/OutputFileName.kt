package com.buzz.shortnews.flashnews

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import java.util.Date


fun configOutputFileName(
    project: Project,
    componentsExtension: ApplicationAndroidComponentsExtension
) {
    componentsExtension.onVariants {
        val finalName = "FlashNews"
        it.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                // 获取原始文件名和后缀
                project.setProperty(
                    "archivesBaseName",
                    "${finalName}-v${output.versionCode.get()}"
                )
            }
        }
    }
}