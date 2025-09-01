# Keep all classes and methods in your app package (no obfuscation that breaks functionality)
-keep class com.example.salarycalculator.** { *; }

# Keep all Activities (including MainActivity)
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# Keep all Android View related classes
-keep class android.view.** { *; }

# Avoid warnings for AndroidX/support types in layouts
-dontwarn android.support.**
-dontwarn androidx.**

# Remove logging (optional, for smaller APK)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# If you use annotations for @Keep, uncomment below:
# -keep class * {
#     @androidx.annotation.Keep *;
# }
