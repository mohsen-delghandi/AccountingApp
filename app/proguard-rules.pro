# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in X:\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

-keep class com.backtory.java.internal.* { *; }
-keep class com.backtory.java.internal.*$* { *; }

-keep class com.readystatesoftware.sqliteasset.* { *; }
-keep class com.readystatesoftware.sqliteasset.*$* { *; }

-keep class com.alirezaafkar.sundatepicker.components.* { *; }
-keep class com.alirezaafkar.sundatepicker.components.*$* { *; }

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }