# android-common

Add jitpack to repository to all projects

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

and then the dependencies for a module

```
implementation "com.github.lelloman.android-common:core:-SNAPSHOT"
implementation "com.github.lelloman.android-common:http:-SNAPSHOT"

testImplementation "com.github.lelloman.android-common:jvmtestutils:-SNAPSHOT"

androidTestImplementation "com.github.lelloman.android-common:androidtestutils:-SNAPSHOT"
```
