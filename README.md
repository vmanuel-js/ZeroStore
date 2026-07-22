# Zero Store — Android App

- Aplicación móvil de ecommerce de ropa masculina desarrollada en **Kotlin + Jetpack Compose** como proyecto universitario. Consume una API REST propia alojada en AlwaysData e integra Firebase AI para atención al cliente.

---
## Funcionalidades

### 🛍️ Catálogo de productos
- Lista de productos con imagen, nombre y precio consumidos desde API REST
- Filtro por categoría (Polos, Poleras, Jeans, Buzos, etc.)
- Buscador con **historial persistente local** usando Room
- Animaciones de transición entre pantallas

### 📍 Tiendas físicas
- **Activity 1:** Lista de tiendas con foto, título y subtítulo
- **Activity 2:** Mapa individual con marcador y círculo de 200m — al tocar el marcador muestra nombre y distrito, al tocar el círculo abre el detalle
- **Activity 4:** Mapa global con todas las tiendas marcadas
- **Activity 3:** Detalle completo con imagen, descripción, teléfono y horario

### 🤖 ZeroBot — Asistencia inteligente
- Chat integrado con **Firebase AI (Gemini-3.1-flash-lite)**
- Prompt especializado: consultas, reclamos, quejas y devoluciones
- Marco legal peruano: Ley N° 29571, INDECOPI
- Accesible desde burbuja flotante en la pantalla principal

### ❤️ Favoritos
- Agregar y quitar productos favoritos sincronizado con servidor

### 📊 Panel de administración
- Gráfico de barras de stock por categoría con Canvas de Compose (sin librerías externas)
- Lista de productos con stock crítico (≤ 10 unidades)

### 🔐 Autenticación
- Login con sesión persistente via DataStore
- Splash screen con animación de entrada

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + StateFlow |
| Red | Retrofit 2 + Gson |
| BD Local | Room (historial de búsqueda) |
| Imágenes | Coil 3 |
| Mapas | Google Maps Compose |
| IA | Firebase AI — Gemini-3.1-flash-lite |
| Sesión | DataStore Preferences |
| Backend | PHP + MySQL en AlwaysData |

---

## Arquitectura del proyecto
app/
├── data/
│ ├── local/ # Room Database, DAOs, Entities
│ └── remote/ # Retrofit services, RetrofitClient
├── models/ # Data classes
├── pages/
│ ├── initial/ # Home + buscador
│ ├── detalle/ # Detalle de producto
│ ├── favoritos/ # Lista de favoritos
│ ├── tienda/ # Activities de tiendas y mapas
│ ├── chat/ # ZeroBot con Firebase AI
│ ├── grafico/ # Panel de stock
│ └── ...
├── components/ # Componentes reutilizables
└── ui/theme/ # Colores, tipografía, tema

---

## Configuración

Crea `local.properties` en la raíz:

```properties
MAPS_API_KEY=tu_api_key_de_google_maps
```

Coloca `google-services.json` desde Firebase en `/app`.

---

## Instalación

```bash
git clone https://github.com/vmanuel-js/ZeroStore.git
cd ZeroStore
# Agrega local.properties y google-services.json
# Abre en Android Studio y ejecuta
```

---

## Autor

**Manuel Jordan** — [@vmanuel-js](https://github.com/vmanuel-js)  
Desarrollo de Aplicaciones Móviles Android
