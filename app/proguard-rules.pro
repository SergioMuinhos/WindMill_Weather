# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,EnclosingMethod,*Annotation*

# Keep Gson classes
-keep class com.google.gson.** { *; }

# Keep MainActivity model classes from being obfuscated so Gson can map JSON fields
-keep class com.windmill.dam2.windmillweather.MainActivity$PrediccionResponse { *; }
-keep class com.windmill.dam2.windmillweather.MainActivity$PredConcello { *; }
-keep class com.windmill.dam2.windmillweather.MainActivity$DiaConcello { *; }
-keep class com.windmill.dam2.windmillweather.MainActivity$FranxaTemp { *; }
-keep class com.windmill.dam2.windmillweather.MainActivity$FranxaChoiva { *; }
-keep class com.windmill.dam2.windmillweather.MainActivity$FranxaCeo { *; }
-keep class com.windmill.dam2.windmillweather.MainActivity$FranxaVento { *; }
