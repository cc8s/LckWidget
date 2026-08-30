# Contributing

Issues and pull requests are welcome. Please skim this page before you start.

## Getting started

You need JDK 17 and Android Studio Ladybug or newer. There is no API key to
provision.

```bash
git clone https://github.com/cc8s/LckWidget.git
cd LckWidget
./gradlew testDebugUnitTest assembleDebug
```

Widget rendering is not covered by unit tests. If you touched the UI, add the
widget on a device or emulator and verify all three height breakpoints
(1, 2, and 3 matches).

## Two hard constraints

**1. `RemoteViews`**

App widgets run on `RemoteViews`, not on the Compose runtime you may be used to.
Animations, transitions, custom fonts, background blur, and arbitrary shape
rendering are all unavailable. That is why the widget's gradient border is a
`layer-list` drawable rather than a Compose modifier — if a proposal needs any
of those, it cannot be built as an app widget.

**2. Asset policy**

This repository ships no league, team, or organizer logos, and it never will.
**Pull requests that add logo imagery are closed without review.**

- The widget header mark, group markers, and launcher icon are original shapes
  made for this project and are distributed under MIT. Any new icon must
  likewise be original.
- Teams are identified by their code (`GEN`, `T1`, ...) as text. Logo URLs from
  the API stay in the domain model and are never rendered.

## Code style

Kotlin official conventions (`kotlin.code.style=official`). Android Studio's
default formatter is fine; there is no separate lint configuration.

Comments and commit messages may be in English or Korean. Match the comment
density and naming of the surrounding code.

## Before opening a PR

- `./gradlew testDebugUnitTest` passes
- The PR links its issue if one exists (`Closes #12`)
- One change per pull request

CI runs unit tests and a debug build on every pull request against `main`.

## Working on schedule parsing

The schedule comes from an internal lolesports.com endpoint, not a public API,
so it can change without notice. When fixing the parser, add a real captured
response as a fixture — see
[`app/src/test/resources/schedule_lck_20260817.json`](app/src/test/resources/schedule_lck_20260817.json)
— and add a test that reads it.
