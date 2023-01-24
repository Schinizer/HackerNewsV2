# HackerNewsV2

A simple read only HackerNews client built using [Jetpack Compose](https://developer.android.com/jetpack/compose), [Hilt](https://dagger.dev/hilt/), [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html), [Retrofit](https://square.github.io/retrofit/) and [Moshi](https://github.com/square/moshi).

## Features
* Compose UI
* CI with github actions
* Multi modules with inverted dependencies
* MVVM Design Pattern
* Repository Design Pattern with offline support (Retrofit/Room)
* Dependency Injection (Dagger/Hilt)
* Coroutines (suspend functions, StateFlow/SharedFlow)
* Sealed Interface Deserialization
* Profile-able builds
* Unit testing
* Baseline profiles
* Macro benchmarks

## Overview

## To Build
Clone the repo using your preferred client or run the following command
```
git clone https://github.com/Schinizer/HackerNewsV2.git
```

Open and run the project using Android Studio or run the following command
```
./gradlew assembleDebug installDebug
```
to build and install the project to connected devices.

## License
```
Copyright 2021-2023 Schinizer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
