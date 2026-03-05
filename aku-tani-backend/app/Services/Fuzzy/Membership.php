<?php

namespace App\Services\Fuzzy;

class Membership
{
    private static function clamp($value): float
    {
        return max(0.0, min(1.0, (float) $value));
    }

    /* ================= LUAS LAHAN ================= */

    public static function luasKecil($x): float
    {
        if ($x <= 1000) return 1.0;
        if ($x >= 2000) return 0.0;
        return self::clamp((2000 - $x) / 1000);
    }

    public static function luasSedang($x): float
    {
        if ($x <= 1500 || $x >= 5000) return 0.0;
        if ($x <= 3000) return self::clamp(($x - 1500) / 1500);
        return self::clamp((5000 - $x) / 2000);
    }

    public static function luasBesar($x): float
    {
        if ($x <= 4000) return 0.0;
        if ($x >= 10000) return 1.0;
        return self::clamp(($x - 4000) / 6000);
    }

    /* ================= HASIL PANEN ================= */

    public static function hasilRendah($x): float
    {
        if ($x <= 2) return 1.0;
        if ($x >= 4) return 0.0;
        return self::clamp((4 - $x) / 2);
    }

    public static function hasilSedang($x): float
    {
        if ($x <= 3 || $x >= 7) return 0.0;
        if ($x <= 5) return self::clamp(($x - 3) / 2);
        return self::clamp((7 - $x) / 2);
    }

    public static function hasilTinggi($x): float
    {
        if ($x <= 6) return 0.0;
        if ($x >= 10) return 1.0;
        return self::clamp(($x - 6) / 4);
    }

    /* ================= KONDISI TANAH ================= */

    public static function tanahRendah($x): float
    {
        if ($x <= 30) return 1.0;
        if ($x >= 50) return 0.0;
        return self::clamp((50 - $x) / 20);
    }

    public static function tanahSedang($x): float
    {
        if ($x <= 40 || $x >= 80) return 0.0;
        if ($x <= 60) return self::clamp(($x - 40) / 20);
        return self::clamp((80 - $x) / 20);
    }

    public static function tanahTinggi($x): float
    {
        if ($x <= 70) return 0.0;
        if ($x >= 100) return 1.0;
        return self::clamp(($x - 70) / 30);
    }
}
