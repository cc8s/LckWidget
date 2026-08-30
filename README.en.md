<div align="center">

# LCK Widget

Keep the next LCK matches on your Android home screen

[![CI](https://github.com/cc8s/LckWidget/actions/workflows/ci.yml/badge.svg)](https://github.com/cc8s/LckWidget/actions/workflows/ci.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Glance](https://img.shields.io/badge/Jetpack%20Glance-1.1.1-4285F4?logo=android&logoColor=white)](https://developer.android.com/jetpack/androidx/releases/glance)
[![minSdk](https://img.shields.io/badge/minSdk-31-3DDC84?logo=android&logoColor=white)](https://developer.android.com/about/versions/12)
[![Release](https://img.shields.io/github/v/release/cc8s/LckWidget)](../../releases/latest)
[![License](https://img.shields.io/badge/License-MIT-black)](LICENSE)

[한국어](README.md) · **English**

<img src="docs/screenshot.png" width="520" alt="LCK Widget" />

</div>

Checking when the next match starts meant opening an app, waiting for it to load,
finding the schedule tab and scrolling. This removes all of that: the next three
matches simply sit on the home screen.

## Install

Grab the `.apk` from [Releases](../../releases/latest) and open it. You may need
to allow installation from unknown sources the first time. Requires Android 12
(API 31) or higher; not on the Play Store yet.

After installing, long-press the home screen and add **LCK Widget** from the
widget list.

## How it works

`WorkManager` fetches the lolesports schedule every 6 hours and caches it in
DataStore. The Glance widget **only ever reads the cache**, so it never blocks on
the network — the home screen draws instantly and the last known schedule stays
visible offline. To refresh on demand, open the app and tap `지금 동기화` (Sync now).

The widget shows 1-3 matches depending on its height. Group stage matches get a
group marker between the teams (◆ Legend / ▲ Rise), and matches without a
confirmed matchup show the block name instead of team codes. Colors follow
Material You.

```
data/remote   lolesports schedule API (Retrofit + kotlinx.serialization)
data/local    DataStore-backed schedule cache
data          response -> domain mapping, repository
widget        Glance app widget and its components
work          6-hour WorkManager sync
```

## Build

Needs JDK 17 and Android Studio Ladybug or newer. No API key setup required.

```bash
git clone https://github.com/cc8s/LckWidget.git
cd LckWidget
./gradlew assembleDebug
```

To sign a release build yourself, copy
[`keystore.properties.example`](keystore.properties.example) to
`keystore.properties`, fill in the values, and run `./gradlew assembleRelease`.
Without that file the release build falls back to the debug key, so a plain clone
still builds.

## Known limitations

App widgets run on `RemoteViews`, which rules out animations, custom fonts,
background blur, and arbitrary shape rendering. That constraint is why the
widget's gradient border is drawn as a `layer-list` drawable.

The schedule comes from an internal lolesports.com endpoint rather than a public
API, so a schema change can break it without warning.

## Asset policy

This repository contains **no league, team, or organizer logos or trademarked
imagery.**

- The widget header mark, group markers, and launcher icon are original shapes
  made for this project, distributed under MIT.
- Team logos are never rendered. Logo URLs from the API stay in the domain model
  only; the widget shows team codes (`GEN`, `T1`, ...) as text.
- League and team names appear only to identify which match is which.

Pull requests that add logos will not be accepted. Everything else is welcome —
see [CONTRIBUTING.md](CONTRIBUTING.md) before you start.

## License

[MIT](LICENSE)

---

LCK Widget isn't endorsed by Riot Games and doesn't reflect the views or opinions
of Riot Games or anyone officially involved in producing or managing Riot Games
properties. Riot Games and all associated properties are trademarks or registered
trademarks of Riot Games, Inc.
