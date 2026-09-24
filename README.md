# ShopLite

A shopping app built with Jetpack Compose, backed by the public [DummyJSON](https://dummyjson.com) API.

Features: login, product catalogue with search, categories and infinite scroll, product details with reviews, cart, and offline support.

## Stack

Kotlin · Jetpack Compose (Material 3) · Hilt · Retrofit + OkHttp · Room · Coroutines/Flow · Navigation Compose · Coil

## Running

1. Open the project in Android Studio (Koala or newer, JDK 17).
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device (API 26+). An internet connection is needed on first launch.

Test login: `emilys` / `emilyspass` (or `michaelw` / `michaelwpass`).

Unit tests: `./gradlew test`

## Project structure

```
app/src/main/java/com/shoplite/app/
├── MainActivity.kt         # entry point + navigation graph
├── ShopLiteApp.kt          # Hilt application
├── di/                     # Hilt modules (network, database)
├── network/                # Retrofit API, interceptors, models
├── data/
│   ├── local/              # Room database, DAO, converters
│   └── repository/         # Auth, Product, Cart repositories
├── ui/
│   ├── login/
│   ├── home/
│   ├── detail/
│   ├── cart/
│   └── theme/
└── utils/
```

## API

Base URL: `https://dummyjson.com/`

| Endpoint | Used for |
|---|---|
| `POST auth/login` | Login, returns access + refresh token |
| `GET auth/me` | Logged-in user's profile |
| `POST auth/refresh` | Refresh the access token |
| `GET products?limit=&skip=` | Paginated catalogue |
| `GET products/search?q=` | Search |
| `GET products/category-list` | Category names |
| `GET products/category/{slug}` | Products in a category |
| `GET products/{id}` | Product detail |
| `POST carts/add` | Add to cart |

## For this session

Your interviewer will give you a task. Treat it like a real ticket on a codebase you've just joined: think out loud, ask questions, and change what you think needs changing.
