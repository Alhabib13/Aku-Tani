<?php

namespace App\Services\Fuzzy;

class TsukamotoService
{
    public function hitung($luas, $hasil, $tanah, $tanaman, $pupuk): float
    {
        // ================= FUZZIFIKASI =================
        $μ = [
            'luas' => [
                'kecil' => Membership::luasKecil($luas),
                'sedang' => Membership::luasSedang($luas),
                'besar' => Membership::luasBesar($luas),
            ],
            'hasil' => [
                'rendah' => Membership::hasilRendah($hasil),
                'sedang' => Membership::hasilSedang($hasil),
                'tinggi' => Membership::hasilTinggi($hasil),
            ],
            'tanah' => [
                'rendah' => Membership::tanahRendah($tanah),
                'sedang' => Membership::tanahSedang($tanah),
                'tinggi' => Membership::tanahTinggi($tanah),
            ],
        ];

        $rules = Rules::get();

        $totalZ = 0.0;
        $totalA = 0.0;

        // ================= INFERENSI =================
        foreach ($rules as $r) {
            $α = min(
                $μ['luas'][$r['luas']],
                $μ['hasil'][$r['hasil']],
                $μ['tanah'][$r['tanah']]
            );

            if ($α <= 0) continue;

            // ================= OUTPUT (INVERSE) =================
            $z = match ($r['output']) {
                'sedikit' => 150 - ($α * 100), // domain 50–150
                'banyak'  => 150 + ($α * 150), // domain 150–300
                default   => 0.0
            };

            $totalZ += $α * $z;
            $totalA += $α;
        }

        // ================= DEFUZZIFIKASI =================
        $hasilFuzzy = $totalA > 0 ? $totalZ / $totalA : 0.0;

        // ================= FAKTOR TANAMAN =================
        $faktorTanaman = match ($tanaman) {
            'Padi'   => 1.0,
            'Jagung' => 0.9,
            'Cabai'  => 1.2,
            default  => 1.0
        };

        // ================= FAKTOR PUPUK =================
        $faktorPupuk = match ($pupuk) {
            'NPK'     => 1.0,
            'Urea'    => 0.85,
            'Organik' => 1.3,
            default   => 1.0
        };

        return round((float) ($hasilFuzzy * $faktorTanaman * $faktorPupuk), 2);
    }
}
