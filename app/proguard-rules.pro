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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep class cz.msebera.android.httpclient.** { *; }
-keep class com.loopj.android.http.** { *; }
-dontwarn com.squareup.okhttp.**
#-keep class org.apache.http.* { ; }
#-dontwarn org.apache.http.

-dontwarn org.apache.**
-keep class org.apache.http.**

-keep interface org.apache.http.**


-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *;}

#-dontwarn okio.**
#-dontwarn com.squareup.okhttp3.**
#-keep class com.squareup.okhttp3.** { ; }
#-keep interface com.squareup.okhttp3.* { *; }
#-dontwarn javax.annotation.Nullable
#-dontwarn javax.annotation.ParametersAreNonnullByDefault

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase




-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-dontwarn com.squareup.okhttp.**

-dontwarn org.apache.lang.**

-keep public class com.google.ads.** {
   public *;
}

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
-keep class android.support.v7.widget.RoundRectDrawable { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}


-keep class com.facebook.ads.** { *; }
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }




-overloadaggressively
-mergeinterfacesaggressively
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-optimizations !method/marking/static
-keepattributes *Annotation*
-dontskipnonpubliclibraryclasses
-optimizationpasses 5
-flattenpackagehierarchy
-adaptresourcefilenames    **.properties,**.gif,**.jpg,**.png
-renamesourcefileattribute sourcefile