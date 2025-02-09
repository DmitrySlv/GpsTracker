# GpsTracker

Android-приложение для отслеживания маршрутов в реальном времени с возможностью сохранения и просмотра истории перемещений.

## Функциональность

- Отслеживание местоположения в реальном времени
- Отображение маршрута на карте OpenStreetMap
- Запись маршрутов с сохранением в базу данных
- Просмотр истории маршрутов
- Отображение информации о текущем маршруте:
  - Время в пути
  - Пройденная дистанция
  - Текущая скорость
  - Средняя скорость
- Настройка параметров отслеживания:
  - Интервал обновления местоположения
  - Цвет отображения маршрута на карте

## Технологии

- Kotlin
- MVVM архитектура
- Jetpack компоненты:
  - ViewModel
  - LiveData
  - Room
  - Navigation
- Coroutines & Flow
- OpenStreetMap для отображения карты
- Location Services
- Foreground Service для отслеживания местоположения
- SharedPreferences
- ViewBinding

## Требования

- Android 5.0 (API level 21) и выше
- Разрешения:
  - ACCESS_FINE_LOCATION
  - ACCESS_COARSE_LOCATION
  - ACCESS_BACKGROUND_LOCATION
  - INTERNET

## Скриншоты

*Добавьте скриншоты основных экранов приложения*

## Установка

1. Склонируйте репозиторий:
git clone https://github.com/your-username/GpsTracker.git

3. Откройте проект в Android Studio

4. Соберите и запустите приложение
