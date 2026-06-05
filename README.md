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

---

## 🛠️ Requisitos de Compilación y Entorno

Para garantizar una construcción limpia sin conflictos de versiones del compilador o herramientas de Gradle, el proyecto se ha configurado para usar el JDK 17 embebido de Android Studio:

*   **JDK Mínimo**: Java 17
*   **Android SDK**:
    *   `compileSdk`: 34
    *   `minSdk`: 28
    *   `targetSdk`: 33
*   **AndroidX habilitado** con soporte para Jetifier en `gradle.properties`.

---

## 📦 Compilación local

Para compilar el proyecto y generar el archivo APK de desarrollo, ejecuta el siguiente comando desde la raíz del proyecto:

### Windows (PowerShell / CMD):
```powershell
.\gradlew.bat assembleDebug
```

### Linux / macOS:
```bash
./gradlew assembleDebug
```

El APK resultante se almacenará en la ruta:  
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📝 Versiones y Cambios

### Versión 1.1 (Junio 2026)
*   **Migración JSON**: Sustitución del parser XML e integración del parser Gson.
*   **Modo Oscuro Integrado**: Implementación del toggle de tema y persistencia de su estado.
*   **Diseño M3**: Reorganización de layouts, optimización del espaciado, y sustitución de flechas por chevrons.
*   **Formato de Fecha**: Fecha de predicción en formato `dd-MM-yyyy` posicionada como subtítulo superior de cabecera.
*   **Persistencia**: Restauración de selección de ubicación al inicio.

---

*Desarrollado como parte de la modernización del proyecto WindMill Weather.*
