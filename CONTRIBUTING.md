# 기여하기 / Contributing

이슈와 PR을 환영합니다. 시작하기 전에 이 문서를 한 번 읽어 주세요.

Issues and pull requests are welcome. Please skim this page before you start.

## 시작하기 / Getting started

JDK 17과 Android Studio Ladybug 이상이 필요합니다. API 키 발급 절차는 없습니다.

```bash
git clone https://github.com/cc8s/LckWidget.git
cd LckWidget
./gradlew testDebugUnitTest assembleDebug
```

위젯 변경은 단위 테스트로 잡히지 않습니다. UI를 건드렸다면 실제 기기나
에뮬레이터에 위젯을 추가해서 **1경기 / 2경기 / 3경기 높이 세 가지**를 모두
확인해 주세요.

Widget changes are not covered by unit tests. If you touched the UI, add the
widget on a device or emulator and check all three height breakpoints.

## 반드시 알아야 할 두 가지 제약 / Two hard constraints

**1. `RemoteViews`**

앱 위젯은 `RemoteViews` 위에서 동작합니다. 애니메이션, 전환 효과, 커스텀 폰트,
배경 블러, 임의 도형 렌더링은 구현할 수 없습니다. 위젯의 그라데이션 테두리를
Compose가 아니라 `layer-list` 드로어블로 그린 것도 이 때문입니다.

**2. 에셋 정책 / Asset policy**

이 저장소에는 리그·팀·주최사의 로고나 상표 이미지가 없으며, 앞으로도 추가하지
않습니다. **로고를 추가하는 PR은 검토 없이 닫습니다.**

- 위젯 헤더 마크, 그룹 구분 도형, 런처 아이콘은 이 프로젝트에서 직접 만든
  도형이며 MIT로 배포됩니다. 새 아이콘을 추가한다면 마찬가지로 직접 만든
  도형이어야 합니다.
- 팀은 로고 대신 약어(`GEN`, `T1` 등) 텍스트로 표시합니다. API 응답의
  로고 URL은 도메인 모델에만 남아 있고 렌더링되지 않습니다.

This repository ships no league, team, or organizer logos, and it never will.
**Pull requests that add logo imagery are closed without review.**

## 코드 스타일 / Code style

Kotlin 공식 컨벤션(`kotlin.code.style=official`)을 따릅니다. Android Studio의
기본 포매터를 그대로 쓰시면 됩니다. 별도의 린트 설정은 두지 않았습니다.

주석과 커밋 메시지는 한국어와 영어 모두 괜찮습니다. 기존 코드의 주석 밀도와
네이밍을 따라가 주세요.

## PR 전 확인 / Before opening a PR

- `./gradlew testDebugUnitTest`가 통과하는지
- 관련 이슈가 있다면 본문에 연결했는지 (`Closes #12`)
- 한 PR에 한 가지 변경만 담았는지

CI가 `main` 대상 PR마다 테스트와 디버그 빌드를 자동으로 돌립니다.

## 파싱 관련 기여 / Working on schedule parsing

일정은 공식 API가 아닌 lolesports.com의 내부 엔드포인트에서 옵니다. 스키마가
바뀌면 예고 없이 깨집니다. 파서를 고칠 때는
[`app/src/test/resources/schedule_lck_20260817.json`](app/src/test/resources/schedule_lck_20260817.json)
같은 실제 응답 픽스처를 추가하고, 그 픽스처에 대한 테스트를 함께 올려 주세요.
