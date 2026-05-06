# Moderation Helper GUI

Клиентский Minecraft-мод под **Java Edition / Fabric / 1.21.11**.

Мод открывает GUI наказаний по СКМ на строке чата, сохраняет скриншот до открытия меню, сортирует скриншоты по папкам, ведёт статистику сессии, хранит недавних игроков и умеет запускать/останавливать запись OBS через obs-websocket.

## Возможности

- СКМ по строке чата: поиск ника, временный скриншот, открытие меню.
- Исключения для скриншота: `Tick Speed`, `Reach`, `Fighting suspiciously`, `Block Interaction`.
- Игнорирование рангов: `HT5`, `LT5`, `RHT3`, `XHT5`, `I`, `II`, `III` и остальные из ТЗ.
- Игнорирование маркеров серверов: `anarchy-alpha`, `anarchy-beta`, `anarchy-gamma`, `anarchy-new`, `duels`.
- Warn сразу выдаётся по причине `2.1`.
- Mute / Ban / IPBan разделены по категориям.
- Причины наказаний вынесены в отдельный класс `PunishmentRules`.
- H открывает только статистику и недавних игроков, без скриншота и без поиска ника.
- G останавливает запись OBS, но не должен срабатывать при открытом чате.
- Клавиши H и G меняются через стандартные настройки управления Minecraft.
- Кнопка «Вызвать на проверку» отправляет:
  - `/tpp {nick}`
  - `/tp {nick}`
  - `/check {nick}` или команду из конфига
  - `/tell {nick} Здравствуйте, проверка на читы...`
  - запускает OBS-запись
  - включает таймер над хотбаром: `Идёт запись: 00:00`
- При IPBan OBS останавливается автоматически, кроме причин `бот` и `3.8`.
- Скриншоты старше 30 дней удаляются или архивируются по настройке.

## Структура проекта

```text
src/main/java/ru/wqkcpf/moderationhelper/
  ModerationHelperClient.java
  chat/ChatNicknameParser.java
  chat/ChatLineTracker.java
  config/ModConfig.java
  gui/PunishmentScreen.java
  gui/ReasonScreen.java
  gui/DurationScreen.java
  gui/StatsScreen.java
  gui/GuiUtil.java
  keybind/KeybindManager.java
  mixin/ChatHudMixin.java
  mixin/ChatScreenMixin.java
  obs/ObsController.java
  punishment/PunishmentExecutor.java
  punishment/PunishmentRule.java
  punishment/PunishmentRules.java
  punishment/PunishmentType.java
  recent/RecentPlayersManager.java
  screenshot/ScreenshotManager.java
  stats/SessionStats.java
src/main/resources/fabric.mod.json
src/main/resources/moderation_helper_gui.mixins.json
```

## Как собрать мод

1. Установи **JDK 21**.
2. Открой папку проекта в IntelliJ IDEA или VS Code.
3. В терминале из корня проекта выполни:

```bash
gradle build
```

Если добавишь Gradle Wrapper через `gradle wrapper`, тогда можно будет запускать `./gradlew build` или `gradlew.bat build`.

4. Готовый `.jar` будет здесь:

```text
build/libs/moderation-helper-gui-1.0.0.jar
```

## Куда положить jar

Положи jar в папку:

```text
.minecraft/mods/
```

Нужно установить:

- Fabric Loader для Minecraft `1.21.11`
- Fabric API для Minecraft `1.21.11`

## OBS и obs-websocket

В новых версиях OBS obs-websocket обычно уже встроен.

1. Открой OBS.
2. Перейди в:

```text
Инструменты -> Настройки WebSocket-сервера
```

3. Включи WebSocket-сервер.
4. Поставь порт:

```text
4455
```

5. Укажи пароль или оставь пустым.
6. Пароль должен совпадать с конфигом мода.

## Конфиг

После первого запуска появится файл:

```text
.minecraft/config/moderation_helper_gui.json
```

Пример:

```json
{
  "obsEnabled": true,
  "obsHost": "localhost",
  "obsPort": 4455,
  "obsPassword": "",
  "recentPlayersLimit": 15,
  "screenshotCleanupMode": "DELETE",
  "screenshotRetentionDays": 30,
  "screenshotDir": "moderation_screenshots",
  "checkCommandTemplate": "/check {nick}",
  "checkTellTemplate": "Здравствуйте, проверка на читы. В течении 5 минут жду ваш Anydesk (наилучший вариант, скачать можно в любом браузере)/Discord. Также сообщаю, что в случае признания на наличие чит-клиентов срок бана составит 20 дней, вместо 30.",
  "extraQuickMuteReasons": [],
  "extraQuickBanReasons": [],
  "extraQuickIpBanReasons": []
}
```

### screenshotCleanupMode

```text
DELETE  - удалять старые скриншоты
ARCHIVE - переносить старые скриншоты в moderation_screenshots/archive/
OFF     - не чистить
```

## Где лежат скриншоты

По умолчанию:

```text
.minecraft/moderation_screenshots/
```

Временные:

```text
moderation_screenshots/temp/
```

Итоговые:

```text
moderation_screenshots/warn/
moderation_screenshots/mute/
moderation_screenshots/ban/
moderation_screenshots/ipban/
```

Формат имени:

```text
{nick}_{punishment}_{duration}_{reason}_{datetime}.png
```

Запрещённые символы заменяются на `_`.

## Управление

### СКМ по чату

1. Открой чат.
2. Наведи на строку с ником.
3. Нажми СКМ.
4. Мод попытается определить ник, сделать скрин до меню и открыть GUI.

### H

Открывает панель статистики и список недавних игроков.

Важно: H не берёт ник из последнего сообщения и не делает скриншот.

### G

Останавливает запись OBS.

Важно: при открытом чате G игнорируется, чтобы случайно не остановить запись во время ввода текста.

Клавиши можно поменять в:

```text
Настройки -> Управление -> Moderation Helper GUI
```

## Команды наказаний

- Warn:

```text
/warn {nick} 2.1
```

- Mute:

```text
/mute {nick} {duration} {reason}
```

- Ban:

```text
/ban {nick} {duration} {reason}
```

- IPBan:

```text
/ipban {nick} {duration} {reason}
```

## Важное замечание

В Minecraft 1.21.11 экранный ввод GUI использует новый `Click`-API. Поэтому миксин написан под сигнатуру:

```java
mouseClicked(Click click, boolean doubled)
```

Если ты перенесёшь мод на старую версию Minecraft, этот метод нужно будет заменить на старую сигнатуру `mouseClicked(double mouseX, double mouseY, int button)`.
