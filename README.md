# 📖 하루독후감
<p align="left"><img src="https://user-images.githubusercontent.com/79504043/196118811-a08a4517-00f5-4d2e-bb50-a07621a854b1.png" width="960" height="480"/></p>

</br>

## 🤖 Google Play 스토어

[![텍스트](https://user-images.githubusercontent.com/79504043/199650037-59fadea7-77b8-429a-b487-8a87742320ab.png)](https://play.google.com/store/apps/details?id=com.stark.booksearchapp)


</br>


## 🚀 프로젝트 기획 의도
책과 멀어진 현대인들!

하루 독후감으로 간편하게 나만의 하루 독후감을 기록해보세요:)

</br>

## ⚙ 주요 기능
- 독후감 등록 / 수정 / 삭제
- 관심있는 책 추가 / 삭제
- 검색 정렬 선택
- 앱 내 캐시 정리
- 독후감 알람 설정 (버전 1.2에 추가)

</br>

## 📱 동작 화면
<Blockquote>
하루독후감의 실제 동작화면 입니다.📖
</Blockquote>

| 책 검색 | 독후감 등록 | 독후감 수정 및 삭제 | 
|:--------:|:--------:|:--------:|
| ![](https://user-images.githubusercontent.com/79504043/197685470-a81b6f61-7a4e-4c2d-a6b9-8b08d4469bea.gif) | ![](https://user-images.githubusercontent.com/79504043/197686020-802409e5-3bd4-49df-9e7f-b4e65e313989.gif) | ![](https://user-images.githubusercontent.com/79504043/197703976-29d05793-b255-4283-8f27-163f194a3ea2.gif) | 


| 즐겨찾기 등록 및 삭제 | 검색 정렬 설정 및 캐시 비우기 | 알람 설정 |
|:--------:|:--------:|:--------:|
| ![](https://user-images.githubusercontent.com/79504043/197687493-2ae69315-3ed0-4877-bed0-8684e8d12567.gif) | ![](https://user-images.githubusercontent.com/79504043/197687561-039e9f8a-1756-4172-8f5a-28951e9fca07.gif) | ![](https://user-images.githubusercontent.com/79504043/199972836-955cc295-f171-4f7f-92fa-ec7015270c66.gif) |


</br>


## 😎 기술적 고민

### 📌 테스트 코드

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/196134114-b2a1d796-3be6-46f9-9f1f-ebdf0ad9ef05.png" width="460" height="350"/></p>

> 하루독후감📖 은 Roboletric과 Espresso를 이용하여 앱 테스트를 진행했습니다.

</br>

### 🤷‍♂️ 테스트 자동화의 필요성?

- 수동 테스트
  - 빌드시간 증가, 비용 증가
- 자동 테스트
  - 개발시간 감소, 비용 절감, 견고한 구조

앱을 개발하면 정상적으로 작동하는지 테스트를 수행해야합니다. 하루독후감을 예로 들면 Room 데이터베이스에 독후감과 관심목록이 정상적으로 저장이 되는지 확인하기 위해 애뮬레이터나 단말기를 실행하여
직접 결과를 확인하는 식의 테스트를 진행합니다. 문제가 있다면 로그를 찍어가며 문제가 무엇인지 파악하고 수정하고 다시 테스트 하는 과정을 반복합니다.

이러한 작업은 앱의 규모가 커진다면 빌드하는 시간과 테스트를 UI로 입력하는 시간이 점점 길어질 것입니다. 따라서 하루독후감에 자동 테스트를 적용하기로 하였습니다.

</br>


### 📌 테스트 코드 작성 스타일

**Given-When-Then 스타일**
- **Given** : 어떠한 상태 하에서
- **When** : 어떠한 기능을 실행하면
- **Then** : 어떠한 결과가 나와야 한다.

```kotlin
@Test
fun x1_multiplyBy_2() {
	// Given
	val x = 1
	
	// When
	val result = Utils.multipleBy2(x)

	// Then
	asserEquals(2, result)
}

```
- Given : x가 1일 때
- When : 2를 곱하면
- Then : 2가 나와야한다.


</br>


### 📌 테스트 대상 항목

- **단위 테스트 대상**
  - ViewModel
  - 데이터 레이어 (Data Layer)

- **UI 테스트**
  - 스크린 UI
  - 유저 플로우 (User Flow)
  - 네비게이션 (Navigation)

- **테스트 제외 대상**
  - 프레임워크 자체 동작
  - Activity, Fragment. Service에는 테스트가 필요한 로직을 가능한 배치하지 않음

</br>


### 📌 사용 라이브러리

- **Testing Framework**

  - **Junit4** : Java의 단위 테스트 코드를 작성하기 위해 만들어진 프레임워크로 Jetpack Test 라이브러리는 JUnit4를 기준으로 만들어져 있음. 현재 최산 버전은 JUnit5이나, 안드로이드를 완벽히 지원하지 않음
  - **Robolectric** : JVM 만으로 안드로이드 프레임워크를 테스팅하기 위해 만들어진 프레임워크

- **Assertion**

  - **Truth** : Test를 수행할 때는 Test된 값이 맞는지 Assertion 즉 역설(증명)을 해야하는데 이때 쓰이는 라이브러리

- **UI Testing**

  - **Espresso** : 단일 안드로이드 앱의 UI를 테스트하는 프레임워크



</br>
</div>
</details>

### 📌 데이터 관리

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/196143103-3d3fb747-ac4c-47ee-bf38-7fa8f1e604e0.png" width="360" height="250"/></p>


> 하루독후감📖 은 데이터 관리를 위해 Jetpack Room 을 사용했습니다.

</br>

### 📌 서버 없는 데이터관리

하루독후감은 단독으로 진행한 프로젝트이기 때문에 백엔드 즉 서버가 존재하지 않았습니다. 하루독후감에서 사용하는 데이터가 영상과 같은 큰 데이터가 아니기 때문에 데이터를 관리하기 위해 내장 DB를 사용하기로 했습니다.

</br>


### 🤷‍♂️ Room을 선택한 이유

기존에 Android 진영에서는 SQLite를 이용해 단말기 내부에 데이터를 저장하고 있었습니다.

- 미 해군의 구축함에서 이용하기 위해 만들어졌다고 함
- 전 세계에서 1조개가 넘는 DB가 운용되고 있음

</br>


하지만 아래와 같은 이유로 인해 구글에서는 SQLite 보다 Jetpack 라이브러리에 포함된 Room을 사용을 권장하고 있습니다.
- SQL 쿼리에 대해서 올바르게 작성이 되었는지 컴파일 타임에 확인할 수 없다. 이로 인해 잘못된 쿼리 사용으로 영향을 받는 데이터가 생긴다면, 오류를 직접 업데이트를 해야 한다. 이 과정이 시간이 오래 걸리고 에러를 발생시키기도 한다.
- SQL 쿼리와 데이터 객체와의 변환이 자유롭지 못하다. 쿼리를 통해 필터들을 각각 읽고 하나의 데이터 객체의 생성자로서 대입하기 때문에 상용구 코드들이 많이 사용될 수밖에 없다는 단점이 존재한다.


<p align="center"><img src="https://user-images.githubusercontent.com/79504043/196145396-559ba2f7-cff5-47f4-adaa-1395aa6dbe8c.png" width="700" height="200"/></p>

위와 같은 이유로 하루독후감에서는 내장 DB에 데이터를 저장하기 위해 Room 라이브러리를 사용했습니다.


</br>
</div>
</details>

### 📌 Coroutine Flow를 이용한 실시간 검색

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/196150169-a3085626-44c2-4f97-9ede-a4366128d72f.png" width="420" height="230"/></p>


> 하루독후감📖은 반응형 프로그래밍을 구현하기 위헤 **Flow**를 이용했습니다.

### 🤷‍♂️ 왜 Flow?

- 기존 명령형 프로그래밍에서는 데이터의 소비자는 데이터를 요청한 후 받은 결과값을 일회성으로 수신합니다.
- 하지만 이러한 방식은 데이터가 필요할 때마다 결과값을 매번 요청해야한다는 점에서 비효율적입니다.
- Coroutine Flow는 단일 값을 반환하는 suspend 함수와 다르게 순차적으로 여러값을 내보낼 수 있습니다.
- 실시간으로 데이터를 내보내며 값을 소비하지 않고도 처리할 수 있는 장점이 있습니다.

<img width="607" alt="Flow" src="https://user-images.githubusercontent.com/79504043/189902836-daefd6b7-54d2-4cd6-867f-796b01f772ca.png">

<br>
</br>

이 장점들을 이용하여 사용자의 이벤트를 받아서 처리하는 기능들을 구현하는데 Flow를 사용하였습니다.

</br>


### 📌 Detail

```kotlin
private fun searchBooks() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_BOOKS_TIME_DELAY) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        searchViewModel.searchBooksPaging(query)
                    }
                }
            }
            startTime = endTime
        }
    }
```

- **실시간 검색기능**
  - EditText에 TextChaneListener를 달아주어 지정한 시간(SEARCH_BOOKS_TIME_DELAY)보다 간격이 커지는 순간 ViewModel의 searchBookPaging 함수에 요청을 보냅니다.
  - 사용자의 입력 이벤트가 발생할 때마다 Flow가 순차적으로 여러값을 처리합니다.
  - 그렇게 받아온 데이터들을 MutableStateFlow에 보관하며 collectLatestStateFlow 확장함수를 통해 옵저빙하며 갱신합니다.

```kotlin
fun <T> Fragment.collectLatestStateFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collector)
        }
    }
}
```

</br>

### 📌 버전 1.1 추가

- 스토어 배포 후 유저 피드백 결과, 빠르게 검색어를 입력할 경우 몇몇 경우 EditText에 친 검색어와 검색결과가 일치하지 않는 이슈가 보고되었습니다.
- 따라서 Flow의 Debounce 키워드를 사용해 사용자의 입력 이벤트가 종료되고 0.2초 후 서버에 검색 요청을 보내는 로직으로 개선하여 이슈를 해결하였습니다.

**SearchFragment**
```kotlin
private fun listenSearchWordChange() {
    binding.etSearch.addTextChangedListener { text ->
        if (text != null) searchViewModel.handleSearchWord(text.toString())
    }
}

private fun updateSearchWord() {
    collectLatestStateFlow(searchViewModel.searchWord) { query ->
        searchViewModel.searchBooksPaging(query)
    }
}
```

**SearchViewModel**
```kotlin
private val _searchWord = MutableSharedFlow<String>()
val searchWord = _searchWord.debounce { 200 }


fun handleSearchWord(word: String) {
    viewModelScope.launch {
        _searchWord.emit(word)
    }
}

fun searchBooksPaging(query: String) {
    viewModelScope.launch {
        bookSearchRepository.searchBooksPaging(query, getSortMode())
            .cachedIn(viewModelScope)
            .collect {
                _searchPagingResult.value = it
            }
    }
}
```


</div>
</details>


### 📌 Clean Architecture & MVVM

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/189823885-b9eddb33-861b-4bdf-b152-01ce5fac4a7e.png" width="560" height="400"/></p>

> 하루독후감📖은 프로젝트 아키텍처 패턴으로 MVVM 패턴을 사용했습니다.
</br>

### 🤷‍♂️ 왜 아키텍처 패턴?

아키텍처패턴을 적용한 가장 큰 이유는 [안드로이드 공식문서](https://developer.android.com/topic/architecture)에서 말하는 **Seperation of concerns** 즉 **관심사의 분리**를 하기 위해서 입니다.

코드를 관심사 단위로 나누게 되면 한쪽에서 코드가 변경된다고 해도 다른쪽에서 신경쓸 필요가 없어지므로 유지 보수가 용이하다는 장점이 있습니다.

프로젝트를 꾸준히 유지보수 하기 위해 안드로이드 아키텍처패턴의 적용은 꼭 필요하다고 판단했습니다.

</br>

### 🤷‍♂️ 그렇다면 왜 MVVM?

**MVC 패턴의 단점**
- View와 Model 사이의 의존성이 높음
- Controller가 안드로이드에 종속되기 때문에 테스트가 어려워잠
- Controller에 많은 코드가 모이게 되어 Activity가 비대해잠
- 안드로이드 특성상 Activity가 View 표시와 Controller 역할을 같이 수행해야 하기 때문에 두 요소의 결합도가 높아잠

**MVP 패턴의 단점**
- View와 Presenter가 1:1로 강한 의존성을 가지게 됨
- 각각의 View마다 Presenter가 존재하게 되어서 코드량이 많아져 유지 보수가 힘들어질 수 있음

</br>

**이에 비해 MVVM 패턴은**
- View와 Model 사이의 의존성이 없음
- View는 ViewModel을 참조하지만 ViewModel은 View를 참조하지 않음
- 각각 부분이 독립적이라 모듈화 개발에 적합

이에 따라 하루독후감은 MVVM 패턴을 적용하게 되었습니다.

</div>
</details>


### 📌 의존성 주입 DI

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/189824297-fff7ce52-6b99-40fa-835a-894678bb25e5.png" width="660" height="400"/></p>

> 하루독후감📖은 의존성 주입(DI)을 위해 Hilt를 사용했습니다.
</br>

### 🤷‍♂️ 의존성주입이 필요한 이유?
- 의존성 주입을 사용하지 않는다면 클래스 내부에서 직접 의존 항목의 인스턴스를 생성하거나, 직접 DI 객체를 만들어 수동으로 의존성을 주입해야 합니다.
- 이러한 방식은 코드의 재사용이 어렵고 리팩토링이 힘듭니다. 또한 ViewModelFactory의 경우 보일러 플레이트 코드가 발생하게 됩니다.

```kotlin
// ViewModel이 Repository를 가지고 있고, Repository가 DataSource를, DataSource는 AssetLoader를 ...
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(HomeRepository(HomeAssetDataSource(AssetLoader(context)))) as T
            }
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                val repository = CategoryRepository(CategoryRemoteDataSource(ApiClient.create()))
                CategoryViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Failed to create ViewModel: ${modelClass.name}")
            }
        }
    }
}
```

</br>

### 🤷‍♂️ 그렇다면 왜 Hilt?

안드로이드 DI 라이브러리로는 Dagger2, Koin 등의 다른 선택지도 있었지만
- 안드로이드 애플리케이션을 위한 Dagger와 관련 기반 코드들을 간소화
- 쉬운 설정과 가독성/이해도 그리고 앱간 코드 공유를 위한 표준 컴포넌트, 스코프 세트를 생성
- 다양한 빌드 유형에 대한 서로 다른 바인딩을 제공하는 쉬운 방법을 제공

위의 이유와 최근 기업 기술블로그를 보면 DI라이브러리를 Hilt로 이전하는 글을 많이 볼 수 있기에 Hilt를 선택했습니다.


</div>
</details>

### 📌 페이징

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/189824780-88d9439f-9f1d-4d69-a663-53db92c6e195.jpeg" width="660" height="350"/></p>

> 하루독후감📖은 서버에서 데이터 페이징 처리를 위해 **Paging3** 와 **Scrool Listener 를 통한 커스텀 구현** 2가지를 이용했습니다.
</br>

### 🤷‍♂️ 왜 페이징인가?

집 매물 데이터 전체를 한번에 요청하여 가져오는 경우, 아래와 같은 문제가 있었습니다.

- 과도한 데이터 요청에 따른 메모리, 데이터 손해

  ⇒ 끝까지 피드를 보는 경우를 제외하면, 필요 이상의 메모리와 데이터가 소모됨

- Layer 사이의 불필요한 데이터 전달

  ⇒ UI Layer, Data Layer 사이에서 필요 이상의 데이터를 주고 받음

즉 **속도는 빠르게, 부하는 적게** 하기 위해 지금 당장 필요한 데이터만 가져올 수 있도록 데이터를 분리하는 작업을 위해 페이징을 적용했습니다.

</br>


### 📌 Paging3 와 Room 의 호환성

- 서버에서 데이터를 페이징 처리하여 가져오려면 PagingSource를 직접 구현하여야 합니다.
- 아래 코드는 실제 하루독후감의 내부 코드로, Room이 아닌 실제 카카오 서버에서 책 데이터를 가져와 페이징처리하는 부분입니다
```kotlin
interface BookSearchApi {

    @Headers("Authorization: KakaoAK $API_KEY")
    @GET("v3/search/book")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<SearchResponse>
}
```

```kotlin
class BookSearchPagingSource(
    private val api: BookSearchApi,
    private val query: String,
    private val sort: String,
) : PagingSource<Int, Book>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
		...
		val response = api.searchBooks(query, sort, pageNumber, params.loadSize)
		...
	}

	override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        ...
    }
```

</br>

- 하지만 Room 을 사용한다면 쿼리 결과를 자동으로 PagingSource 타입으로 반환받을 수 있습니다.
- 아래 코드는 하루독후감에서 Room 의 쿼리 결과를 PaigingSouce 타입으로 반환받는 부분입니다.

```kotlin
@Dao
interface BookReportDao {

    ...

    @Query("SELECT * FROM bookReports")
    fun getBookReportsPaging(): PagingSource<Int, BookReport> // Room 은 쿼리 결과를 PagingSource 타입으로 반환받을 수 있다.
}

```

</br>

- 이처럼 Paging3 라이브러리가 Room과의 좋은 호환성을 보이고 있음을 알 수 있었습니다.


</div>
</details>

### 📌 build.gradle을 KTS로 마이그레이션

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">

<p align="center"><img src="https://user-images.githubusercontent.com/79504043/196152051-90688473-db4b-4865-a63d-187ad90132f8.png" width="350" height="400"/></p>

> 하루독후감📖은 빌드과정을 도와주는 빌드 툴(Build Tool)을 기존 Groovy를 사용하던 Gradle 스크립트를 코틀린으로 마이그레이션했습니다.
</br>

### 🤷‍♂️ Build Tool?

📌 **Build Tool**

- **빌드(Build)란 무엇인가?**
  - **소스 코드를 바이너리 코드로 컴파일 한 다음에 그 바이너리 코드를 서로 링크(Link)해서 실행 가능한 파일로 패키징** 하는 것을 소프트웨어를 빌드한다고 한다,
- **프로덕션의 빌드(Build) 과정**
  - 연관된 의존성 다운로드
  - 소스 코드를 바이너리 코드로 컴파일
  - 바이너리 코드 링크하여 실행가능 파일로 패키징
  - 테스트 수행
  - 프로덕션 시스템에 배포

소스코드를 수정할 때마다 빌드과정을 하나하나 수행하는 것은 노력이 많이 들어가기 때문에 **일반적으로 이 과정을 자동으로 수행해주는 빌드 툴(Build Tool)을 사용**하게 됩니다.

- **Gradle**
  - 2008년에 발표된 **Groovy 언어 기반의 빌드 툴**
  - 규칙에 따라 빌드 파일명은 build.gradle
  - Java와 비슷한 문법을 가진 Groovy 언어를 채택함으로 가독성을 높임
  - **컴파일을 한 필요가 없어짐**

</br>


📌 **안드로이드에서의 Gradle**

- **구글은 Gradle을 기반으로하는 Android SDK Build System을 발표**
- 그런 이유로 Android Studio에서 Gradle을 사용하기 위해서 **Android Gradle Plugin(AGP)**를 제작
- AGP는 Gradle을 기반으로 하면서 안드로이드앱을 빌드하는데 사용하는 몇가지 기능을 추가한 것
- 초기에는 Gradle 릴리즈 버전과 AGP 버전이 일치하지 않는 문제가 있었지만 7.0부터 AGP 메이저 버전을 Gradle버전과 동기화

</br>


📌 **Kotlin Script(KTS)**

- Gradle 5.0에는 큰 변화가 생겼습니다.
  - Java 11 도입
  - Kotlin-DSL 도입
- 이 말은 Groovy를 사용하던 기존의 Gradle 스크립트를 코틀린에서도 할 수 있게 되었습니다.

</br>

📌 **KTS 장점**

- IDE의 지원으로 향상된 편집환경
  - 컴파일 타임에 에러 확인 / 코드 탐색 / 자동 완성 / 구문 강조
- 익숙한 코틀린 언어로 작성 가능

위와 같은 장점을 경험해보고자 Groovy를 사용하던 Gradle 스크립트를 코틀린으로 마이그레이션했습니다.

</div>
</details>
</br>

