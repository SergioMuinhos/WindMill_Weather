# WindMill Weather 🌦️

**WindMill Weather** es una aplicación móvil nativa para Android que proporciona predicciones meteorológicas detalladas para todos los municipios (concellos) de Galicia, obteniendo datos en tiempo real de los servidores oficiales de **MeteoGalicia**.

Esta versión representa una modernización completa de una aplicación construida originalmente hace más de 6 años, adaptándola a los estándares actuales de desarrollo y diseño en Android.

---

## 🚀 Características Principales

*   **Migración de API (JSON)**: Se ha reemplazado la antigua API de fuentes XML (RSS) —la cual arrojaba un error 404 debido a su deprecación oficial— por el nuevo servicio JSON de MeteoGalicia (`jsonPredConcellos.action`), con deserialización de datos estructurada mediante la biblioteca **Gson**.
*   **Diseño Material Design 3 (M3)**: Interfaz de usuario renovada utilizando tarjetas redondeadas (`MaterialCardView`), tipografía moderna y una organización limpia del pronóstico por franjas horarias (Mañana, Tarde y Noche).
*   **Modo Oscuro Persistente**: Conmutador discreto en la barra de herramientas para cambiar dinámicamente entre el modo claro y oscuro. La preferencia de tema se almacena de forma persistente y se aplica automáticamente en cada inicio.
*   **Persistencia de Ubicación**: La provincia y el municipio seleccionados se guardan automáticamente en `SharedPreferences`. Al reabrir la app, los datos climatológicos de tu última ubicación guardada se consultan de manera inmediata sin requerir selección manual.
*   **Detalles Visuales Pulidos**:
    *   Sustitución de las flechas toscas del sistema por un chevron minimalista y estilizado (`ic_chevron_down.xml`).
    *   Eliminación de zonas blancas o contrastes deficientes en modo noche mediante la configuración global de los atributos M3 (`colorSurface`, `colorOnSurface`, `android:colorBackground`) y el uso de layouts personalizados para las listas del Spinner.
    *   Formateo de fechas adaptado al estándar estándar europeo `dd-MM-yyyy` (ej. `05-06-2026`).
*   **SplashScreen Animado Adaptativo**: Pantalla de carga integrada que reproduce el GIF transparente `splash_clean.gif` centrado y adaptado dinámicamente al fondo del tema activo (azul para modo claro y gris oscuro para modo oscuro) logrando una experiencia de inicio inmersiva.
*   **Soporte Multilingüe (Galego / Español)**: Soporte completo para Gallego (`gl`) y Español (`es`), con selector dinámico de idioma en la barra de herramientas y persistencia de la selección, además de respetar el idioma predeterminado del sistema.
*   **Widget de Escritorio Inteligente (App Widget)**:
    *   Diseño en formato tarjeta Material Design con bordes redondeados y colores que se adaptan automáticamente al modo claro/oscuro del sistema operativo.
    *   Soporte para múltiples dimensiones: diseño extendido grande (`4x2`) con desglose de pronóstico por períodos (Mañana, Tarde, Noche) y diseño compacto horizontal (`2x1` / `4x1`).
    *   Actualización asíncrona periódica de red (cada 2 horas) con descarga en background de iconos meteorológicos, y actualización por broadcast instantáneo al cambiar de concello en la app.
    *   Pulsar sobre el widget abre directamente la aplicación en la pantalla principal.

---

## 🛠️ Requisitos de Compilación y Entorno

Para garantizar una construcción limpia sin conflictos de versiones del compilador o herramientas de Gradle, el proyecto se ha configurado para usar el JDK 17 embebido de Android Studio:

*   **JDK Mínimo**: Java 17
*   **Android SDK**:
    *   `compileSdk`: 37
    *   `minSdk`: 28
    *   `targetSdk`: 37
*   **AndroidX habilitado** con soporte para Jetifier en `gradle.properties`.

---

## 📦 Compilación local

Para compilar el proyecto y generar los ejecutables, puedes usar los siguientes comandos desde la raíz del proyecto:

### Generar APK de desarrollo (Debug)
```powershell
# Windows
.\gradlew.bat assembleDebug

# Linux / macOS
./gradlew assembleDebug
```
El APK se genera en: `app/build/outputs/apk/debug/app-debug.apk`

### Generar binarios de producción firmados (Release APK & AAB)
```powershell
# Windows
.\gradlew.bat assembleRelease bundleRelease

# Linux / macOS
./gradlew assembleRelease bundleRelease
```
*   El **APK de Release** se genera en: `app/build/outputs/apk/release/app-release.apk` (copiado en la raíz como `WindMill_Weather_v1.3.apk`).
*   El **App Bundle (.aab)** se genera en: `app/build/outputs/bundle/release/app-release.aab` (copiado en la raíz como `WindMill_Weather_v1.6.aab`).

## 📦 Descarga de Binarios (Releases)

Puedes descargar los paquetes compilados y firmados de cada versión directamente desde la sección de Releases del repositorio:

*   **[Última Versión v1.6](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.6)**: Soporte completo para Galego / Español con botón interactivo ES / GL en la barra superior.
    *   Descargar APK: [WindMill_Weather_v1.6.apk](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.6/WindMill_Weather_v1.6.apk)
    *   Descargar AAB: [WindMill_Weather_v1.6.aab](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.6/WindMill_Weather_v1.6.aab)
*   **[Versión v1.5](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.5)**: Minificación y optimización R8/ProGuard.
    *   Descargar APK: [WindMill_Weather_v1.5.apk](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.5/WindMill_Weather_v1.5.apk)
    *   Descargar AAB: [WindMill_Weather_v1.5.aab](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.5/WindMill_Weather_v1.5.aab)
*   **[Versión v1.4](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.4)**: Navegación horizontal por gestos táctiles (Swipe) entre pestañas.
    *   Descargar APK: [WindMill_Weather_v1.4.apk](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.4/WindMill_Weather_v1.4.apk)
    *   Descargar AAB: [WindMill_Weather_v1.4.aab](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.4/WindMill_Weather_v1.4.aab)
*   **[Versión v1.3](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.3)**: Contiene el Widget de escritorio y soporte para la API 37 de Android.
    *   Descargar APK: [WindMill_Weather_v1.3.apk](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.3/WindMill_Weather_v1.3.apk)
    *   Descargar AAB: [WindMill_Weather_v1.3.aab](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.3/WindMill_Weather_v1.3.aab)
*   **[Versión v1.2](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.2)**: Contiene el SplashScreen animado adaptativo con GIF transparente.
    *   Descargar APK: [WindMill_Weather_v1.2.apk](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.2/WindMill_Weather_v1.2.apk)
    *   Descargar AAB: [WindMill_Weather_v1.2.aab](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.2/WindMill_Weather_v1.2.aab)
*   **[Versión v1.1](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.1)**: Contiene la migración completa a la API JSON, modo oscuro y Material Design 3.
    *   Descargar APK: [WindMill_Weather_v1.1.apk](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.1/WindMill_Weather_v1.1.apk)
    *   Descargar AAB: [WindMill_Weather_v1.1.aab](https://github.com/SergioMuinhos/WindMill_Weather/releases/download/v1.1/WindMill_Weather_v1.1.aab)

---

## 📝 Versiones y Cambios

### [Versión 1.6 (Junio 2026)](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.6)
*   **Localización en Galego**: Traducción completa de todos los elementos visuales, pestañas, descripciones y widgets a gallego y español.
*   **Botón Conmutador ES / GL**: Botón interactivo en la barra superior que alterna de forma inmediata entre español y gallego con un solo toque y almacena la preferencia del usuario.

### [Versión 1.5 (Junio 2026)](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.5)
*   **Minificación R8/ProGuard**: Reducción del tamaño del APK y obfuscación segura con reglas específicas para Gson.

### [Versión 1.4 (Junio 2026)](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.4)
*   **Swipe Navigation**: Soporte para deslizamiento lateral para cambiar rápidamente entre Hoy, Mañana y Pasado.

### [Versión 1.3 (Junio 2026)](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.3)
*   **Widget de Escritorio**: Implementación de `WeatherWidgetProvider` con soporte para diseño grande (`4x2`) y diseño pequeño horizontal (`2x1`), descarga en background de iconos del cielo desde MeteoGalicia y sincronización mediante `SharedPreferences`.
*   **Actualizaciones Automáticas**: Carga de fondo programada para actualizar el widget cada 2 horas y actualización forzada e inmediata al cambiar la ubicación en la app.
*   **Target SDK 37**: Actualización de versión de API de compilación y destino para cumplir con los estándares de Google Play Console.

### [Versión 1.2 (Junio 2026)](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.2)
*   **SplashScreen con GIF Transparente**: Reversión de la API nativa a una pantalla de carga clásica con el GIF adaptativo y transparente `splash_clean.gif`.
*   **Corrección de Iconos Toolbar**: Modificación de offsets vectoriales y viewport de `ic_moon.xml` y `ic_sun.xml` para resolver el recorte lateral del icono de luna en el toggle del modo oscuro.

### [Versión 1.1 (Junio 2026)](https://github.com/SergioMuinhos/WindMill_Weather/releases/tag/v1.1)
*   **Migración JSON**: Sustitución del parser XML e integración del parser Gson.
*   **Modo Oscuro Integrado**: Implementación del toggle de tema y persistencia de su estado.
*   **Diseño M3**: Reorganización de layouts, optimización del espaciado, y sustitución de flechas por chevrons.
*   **Formato de Fecha**: Fecha de predicción en formato `dd-MM-yyyy` posicionada como subtítulo superior de cabecera.
*   **Persistencia**: Restauración de selección de ubicación al inicio.

---

## 📄 Licencia

Este proyecto está bajo una licencia de **Todos los derechos reservados** (All Rights Reserved). Queda prohibida la reproducción, distribución o modificación de este software y sus derivados sin la autorización previa y por escrito de su autor (**Sergio Muíños**). Consulta el archivo [LICENSE](file:///c:/Users/sergi/Documents/WindMill_Weather/LICENSE) para más detalles.

---

*Desarrollado como parte de la modernización del proyecto WindMill Weather.*
