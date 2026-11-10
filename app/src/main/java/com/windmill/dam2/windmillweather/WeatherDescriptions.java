package com.windmill.dam2.windmillweather;

import android.content.Context;

public final class WeatherDescriptions {

    private WeatherDescriptions() {
    }

    public static String getSkyDescription(Context context, Integer code) {
        if (code == null) {
            return context.getString(R.string.sky_unknown);
        }

        int normalized = code >= 200 ? code - 100 : code;
        int resId;
        switch (normalized) {
            case 101: resId = R.string.sky_101; break;
            case 102: resId = R.string.sky_102; break;
            case 103: resId = R.string.sky_103; break;
            case 104: resId = R.string.sky_104; break;
            case 105: resId = R.string.sky_105; break;
            case 106: resId = R.string.sky_106; break;
            case 107: resId = R.string.sky_107; break;
            case 108: resId = R.string.sky_108; break;
            case 109: resId = R.string.sky_109; break;
            case 110: resId = R.string.sky_110; break;
            case 111: resId = R.string.sky_111; break;
            case 112: resId = R.string.sky_112; break;
            case 113: resId = R.string.sky_113; break;
            case 114: resId = R.string.sky_114; break;
            case 115: resId = R.string.sky_115; break;
            case 116: resId = R.string.sky_116; break;
            case 117: resId = R.string.sky_117; break;
            case 118: resId = R.string.sky_118; break;
            case 119: resId = R.string.sky_119; break;
            case 120: resId = R.string.sky_120; break;
            case 121: resId = R.string.sky_121; break;
            case 122: resId = R.string.sky_122; break;
            case 123: resId = R.string.sky_123; break;
            case 124: resId = R.string.sky_124; break;
            case 125: resId = R.string.sky_125; break;
            default:
                return context.getString(R.string.sky_unknown_code, code);
        }

        String description = context.getString(resId);
        return code >= 200 ? context.getString(R.string.sky_night_desc, description) : description;
    }

    public static String getWindDescription(Context context, Integer degrees) {
        if (degrees == null) {
            return context.getString(R.string.wind_unknown);
        }

        int normalized = ((degrees % 360) + 360) % 360;
        int index = Math.round(normalized / 45f) % 8;
        String[] directions = context.getResources().getStringArray(R.array.wind_directions);
        return context.getString(R.string.wind_desc, directions[index]);
    }
}
