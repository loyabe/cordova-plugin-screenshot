  
##  build.gradle ������Ҫ

    compile 'io.reactivex:rxjava:1.1.0'
    compile('io.reactivex:rxandroid:1.1.0') {
        exclude module: 'rxjava'
    }

    compile 'com.trello:rxlifecycle:0.4.0'
    compile 'com.trello:rxlifecycle-components:0.4.0'
    compile 'com.android.support:multidex:1.0.0'