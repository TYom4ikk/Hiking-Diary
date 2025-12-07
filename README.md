# Hiking Diary

Мобильное приложение‑дневник походов на Kotlin/Android.

Приложение позволяет:
- добавлять походы с названием, датой, местом, описанием и фотографиями;
- просматривать список походов на главном экране;
- открывать подробную информацию о каждом походе и удалять его;ww
- видеть приветственный экран и простой профиль пользователя;
- вести дневник **на русском языке** (все тексты сохраняются и отображаются корректно).

---

## 1. Архитектура проекта

Проект построен по простой многослойной структуре:

- **ui/** – экраны (Activity и Fragment‑ы), работа с навигацией и отображением данных.
  - `ui.main.MainActivity` – единственная Activity, контейнер для NavHostFragment.
  - `ui.hike_list.HikeListFragment` – главный экран со списком походов.
  - `ui.hike_add.AddHikeFragment` – добавление нового похода.
  - `ui.hike_detail.HikeDetailFragment` – просмотр/удаление выбранного похода.
  - `ui.welcome.WelcomeFragment` – экран приветствия.
  - `ui.profile.ProfileFragment` – экран профиля пользователя.
- **data/models/** – модельные классы:
  - `Hike` – один поход (id, название, дата, место, описание, список фото).
  - `Photo` – одно фото (хранится URI в виде строки).
- **data/repository/** – работа с данными:
  - `HikeRepository` – публичный интерфейс для UI: получить все походы, добавить, удалить, найти по id, сгенерировать id.
  - `LocalStorage` – сохранение/чтение списка походов в/из файла JSON.
- **data/storage/** – низкоуровневая работа с файлами:
  - `FileManager` – запись и чтение текстового файла во внутреннем каталоге приложения.

### Хранение данных

1. Все походы хранятся в файле `hikes.json` во внутреннем хранилище приложения.
2. Сериализация/десериализация выполняется через библиотеку **Gson**:
   - список `List<Hike>` превращается в JSON‑строку;
   - JSON‑строка читается обратно в `List<Hike>`.
3. Тексты (названия, описания и т.п.) – обычные `String` в UTF‑8, поэтому **русский язык полностью поддерживается**.

`LocalStorage`:
- `saveHikes(hikes: List<Hike>)` – сохраняет список походов в файл.
- `loadHikes(): List<Hike>` – читает список из файла или возвращает пустой список, если файла ещё нет.

`HikeRepository` поверх этого:
- `getAllHikes()` – вернуть текущий список.
- `addHike(hike)` – добавить и сразу сохранить.
- `deleteHike(id)` – удалить по id и сохранить.
- `getById(id)` – найти поход по id.
- `createId()` – сгенерировать уникальный идентификатор через `UUID`.

---

## 2. Навигация

Навигация реализована через **Navigation Component** и описана в файле
`app/src/main/res/navigation/nav_graph.xml`.

Стартовый экран:
- `hikeListFragment` (список походов).

Основные переходы:
- из `HikeListFragment` в `AddHikeFragment` – действие `action_to_addHike`;
- из `HikeListFragment` в `HikeDetailFragment` – действие `action_to_hikeDetail` c передачей `hikeId`;
- из нижней навигации:
  - `action_to_welcome` – на `WelcomeFragment` (экран приветствия);
  - `action_to_profile` – на `ProfileFragment` (профиль).

Передача параметра `hikeId`:
- при клике по элементу списка создаётся `Bundle`:
  ```kotlin
  val args = bundleOf("hikeId" to hike.id)
  findNavController().navigate(R.id.action_to_hikeDetail, args)
  ```
- в `HikeDetailFragment` id читается из аргументов и по нему вытаскивается поход из `HikeRepository`.

---

## 3. Экраны и их логика

### 3.1. MainActivity

Файл: `ui/main/MainActivity.kt`

- Наследуется от `AppCompatActivity`.
- В `onCreate` устанавливает layout `activity_main.xml`.
- В layout находится только `FragmentContainerView` с `NavHostFragment` и привязкой к `nav_graph.xml`.

### 3.2. Главный экран – HikeListFragment

Файл: `ui/hike_list/HikeListFragment.kt`

**UI (fragment_hike_list.xml):**
- Тёмный фон `background_dark`.
- Верхняя панель:
  - иконка меню `ivMenu` (пока показывает Toast);
  - текст `exit` – завершает приложение через `activity?.finishAffinity()`.
- Круглый аватар `ivAvatar` и заголовок `"Мои походы"`.
- Список походов `RecyclerView` (`rvHikes`).
- Плавающая кнопка `btnAdd` для перехода к добавлению похода.
- Нижняя панель `bottomNav` с тремя иконками навигации.

**Логика:**
- В `onViewCreated` создаётся `HikeRepository`.
- Инициализируется адаптер `HikeListAdapter` с текущим списком походов и обработчиком клика по элементу.
- `RecyclerView` настроен с `LinearLayoutManager`.
- Кнопка `btnAdd` открывает `AddHikeFragment`.
- Нижняя навигация:
  - левая иконка – `WelcomeFragment` (приветствие);
  - средняя – остаётся на текущем экране;
  - правая – `ProfileFragment` (профиль).
- В `onResume` адаптер обновляется актуальным списком походов `adapter.update(repository.getAllHikes())`.

### 3.3. Экран добавления похода – AddHikeFragment

Файл: `ui/hike_add/AddHikeFragment.kt`, layout `fragment_add_hike.xml`.

**UI:**
- Тёмный фон `background_dark`.
- Поля ввода с тёмным фоном и светлыми подсказками:
  - `etTitle` – название похода.
  - `etLocation` – место.
  - `etDate` – дата.
  - `etDescription` – описание (многострочное).
- Кнопки:
  - `btnPickPhoto` – добавить фото (зелёная кнопка).
  - `btnSave` – сохранить поход (зелёная кнопка).

**Русский язык:**
- Все поля – `EditText` с типом `text`, надписей/ограничений по языку нет.
- Введённый русский текст (название, место, описание) попадает в модель `Hike` и далее в JSON без изменений.

**Логика:**

1. Инициализация репозитория:
   ```kotlin
   repository = HikeRepository(requireContext())
   ```
2. Выбор фото:
   - Используется `ActivityResultContracts.GetContent()`:
     ```kotlin
     private val pickImageLauncher = registerForActivityResult(
         ActivityResultContracts.GetContent()
     ) { uri: Uri? ->
         uri?.let {
             photos.add(Photo(it.toString()))
             Toast.makeText(requireContext(), "Фото добавлено", Toast.LENGTH_SHORT).show()
         }
     }
     ```
   - По нажатию `btnPickPhoto` запускается системный выбор изображения: `pickImageLauncher.launch("image/*")`.
3. Сохранение похода:
   - Считываются значения из полей.
   - Проверяется, что `title` и `date` не пустые.
   - Создаётся объект `Hike` со списком выбранных `photos`.
   - Через `repository.addHike(hike)` происходит сохранение (JSON обновляется).
   - Показывается Toast и выполняется `findNavController().popBackStack()` (возврат на список).

### 3.4. Экран деталей похода – HikeDetailFragment

Файл: `ui/hike_detail/HikeDetailFragment.kt`, layout `fragment_hike_detail.xml`.

**UI:**
- Тёмный фон.
- Текстовые поля:
  - `tvTitle`, `tvDate`, `tvLocation`, `tvDescription` с цветами `text_primary` / `text_secondary`.
- Горизонтальная лента фотографий `RecyclerView` (`rvPhotos`).
- Кнопки:
  - `btnDelete` – зелёная кнопка удаления похода.
  - `btnClose` – тёмная кнопка «Закрыть».

**Отображение:**
- В `onViewCreated` читается `hikeId` из аргументов.
- Через `HikeRepository.getById` ищется поход.
- Если id не найден – выводится Toast, выполняется `popBackStack()`.
- Иначе данные подставляются в TextView.

**Фотографии:**
- `rvPhotos` настроен на горизонтальный `LinearLayoutManager`.
- Используется адаптер `PhotosAdapter` (layout `item_photo.xml`):
  - через Glide загружает `Uri` из `Photo.uri` и показывает миниатюру.

**Удаление:**
- Кнопка `btnDelete`:
  ```kotlin
  repository.deleteHike(hike.id)
  Toast.makeText(requireContext(), "Поход удален", Toast.LENGTH_SHORT).show()
  findNavController().popBackStack()
  ```
- Кнопка `btnClose` просто делает `popBackStack()` без удаления.

### 3.5. Экран приветствия – WelcomeFragment

Файл: `ui/welcome/WelcomeFragment.kt`, layout `fragment_welcome.xml`.

- Показывает текст `"Добро пожаловать в Hiking Diary"` на тёмном фоне.
- Внизу – такая же нижняя навигация, как на главном экране.
- Обработчики:
  - левая иконка – ничего не делает (мы уже на приветствии);
  - средняя – `popBackStack()` (возврат к списку походов);
  - правая – переход в профиль (`profileFragment`).

### 3.6. Экран профиля – ProfileFragment

Файл: `ui/profile/ProfileFragment.kt`, layout `fragment_profile.xml`.

**UI:**
- Аватар `avatar_koala`.
- Поле `etNickname` – никнейм пользователя.
- Статусное поле `tvHikeCount` – автоматически показываемое количество походов.
- Поле `etAbout` – текст «О себе».
- Внизу – нижняя навигация (те же иконки).

**Логика:**
- В `onViewCreated` создаётся `HikeRepository` и считается количество походов:
  ```kotlin
  val count = repository.getAllHikes().size
  binding.tvHikeCount.text = count.toString()
  ```
- Нижняя навигация:
  - левая иконка – переход на приветственный экран;
  - средняя – `popBackStack()` (вернуться к списку походов);
  - правая – остаёмся в профиле.

Данные профиля (ник, "о себе") пока никуда не сохраняются – это просто поля для ввода (по заданию логика не усложнялась).

---

## 4. Работа с фотографиями

1. Пользователь выбирает фото на экране добавления похода.
2. В `AddHikeFragment` `Uri` выбранного файла преобразуется в строку и кладётся в список `photos` как `Photo(uri: String)`.
3. При сохранении похода этот список сериализуется в JSON вместе с остальными полями.
4. В списке походов (`HikeListAdapter`) и на экране деталей (`PhotosAdapter`) используется библиотека **Glide**:
   - загрузка первого фото для превью в списке;
   - отображение всех фото в галерее на экране деталей.

---

## 5. Поддержка русского языка

- Все надписи на UI – на русском языке.
- Ввод любых русских символов в полях `EditText` допустим.
- Gson по умолчанию работает в UTF‑8, а `File.writeText`/`readText` Android‑а тоже используют UTF‑8, поэтому
  **русские названия, описания и места походов корректно сохраняются и читаются**.

Никаких специальных настроек локали или кодировки не требуется.

---

## 6. Как запустить проект

1. Открыть папку `Hiking-Diary` в **Android Studio**.
2. Дождаться синхронизации Gradle.
3. Убедиться, что в `build.gradle.kts` приложения:
   - `compileSdk` и `targetSdk` указаны (уже настроено в проекте).
4. Подключить эмулятор или реальное устройство.
5. Нажать **Run** (зелёный треугольник) для сборки и запуска.

При первом запуске откроется главный экран со списком походов (пока пустой), откуда можно перейти к добавлению похода, экрану приветствия или профилю.

---

## 7. Краткое резюме для преподавателя

- Проект демонстрирует:
  - работу с фрагментами и Navigation Component;
  - хранение данных во внутреннем хранилище в формате JSON с использованием Gson;
  - использование RecyclerView и адаптеров для списков и галереи фото;
  - работу с системным выбором изображений (`ActivityResultContracts.GetContent`);
  - реализацию простого многоэкранного интерфейса с единой тёмной цветовой схемой;
  - поддержку русского языка во всех пользовательских данных.

- Код разделён по слоям: UI (фрагменты), репозиторий, модели и работа с файлами.
- Добавление/удаление походов немедленно отражается в UI и сохраняется между перезапусками приложения.
