package com.jakewharton.diffuse

import java.util.Locale

data class ArchiveFile(
  val path: String,
  val type: Type,
  val size: Size,
  val uncompressedSize: Size,
  val isCompressed: Boolean
) {
  enum class Type {
    Dex,
    Jar,
    ApiJar {
      override val displayName get() = "api-jar"
    },
    LintJar {
      override val displayName get() = "lint-jar"
    },
    JarLibs {
      override val displayName get() = "libs"
    },
    Class,
    Arsc,
    Manifest,
    Res,
    Asset,
    Native,
    Other;

    open val displayName get() = name.toLowerCase(Locale.US)

    companion object {
      @JvmField
      val APK_TYPES = listOf(Dex, Arsc, Manifest, Res, Native, Asset, Other)

      @JvmField
      val AAB_TYPES = listOf(Dex, Manifest, Res, Native, Asset, Other)

      @JvmField
      val AAR_TYPES = listOf(Jar, Manifest, Res, Native, JarLibs, ApiJar, LintJar, Other)

      @JvmField
      val JAR_TYPES = listOf(Class, Other)

      @JvmStatic
      @JvmName("fromApkName")
      fun String.toApkFileType() = when {
        matches(Apk.classesDexRegex) -> Dex
        equals(Apk.manifestFileName) -> Manifest
        equals(Apk.resourcesArscFileName) -> Arsc
        startsWith("lib/") -> Native
        startsWith("assets/") -> Asset
        startsWith("res/") -> Res
        else -> Other
      }

      @JvmStatic
      @JvmName("fromAabName")
      fun String.toAabFileType() = when {
        equals(Aab.Module.manifestFilePath) -> Manifest
        startsWith("dex/") -> Dex
        startsWith("lib/") -> Native
        startsWith("assets/") -> Asset
        startsWith("res/") -> Res
        else -> Other
      }

      @JvmStatic
      @JvmName("fromAarName")
      fun String.toAarFileType() = when {
        equals("classes.jar") -> Jar
        equals("api.jar") -> ApiJar
        equals("lint.jar") -> LintJar
        equals(Apk.manifestFileName) -> Manifest
        startsWith("jni/") -> Native
        matches(Aar.libsJarRegex) -> JarLibs
        startsWith("assets/") -> Asset
        startsWith("res/") -> Res
        else -> Other
      }

      @JvmStatic
      @JvmName("fromJarName")
      fun String.toJarFileType() = when {
        endsWith(".class") -> Class
        else -> Other
      }
    }
  }
}
