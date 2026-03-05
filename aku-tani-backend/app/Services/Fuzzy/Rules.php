<?php

namespace App\Services\Fuzzy;

class Rules
{
    public static function get(): array
    {
        return [

            // ================= LUAS BESAR =================
            [
                'luas' => 'besar',
                'hasil' => 'tinggi',
                'tanah' => 'rendah',
                'output' => 'banyak'
            ],
            [
                'luas' => 'besar',
                'hasil' => 'sedang',
                'tanah' => 'rendah',
                'output' => 'banyak'
            ],
            [
                'luas' => 'besar',
                'hasil' => 'sedang',
                'tanah' => 'sedang',
                'output' => 'banyak'
            ],

            // ================= LUAS SEDANG =================
            [
                'luas' => 'sedang',
                'hasil' => 'tinggi',
                'tanah' => 'rendah',
                'output' => 'banyak'
            ],
            [
                'luas' => 'sedang',
                'hasil' => 'sedang',
                'tanah' => 'sedang',
                'output' => 'banyak'
            ],
            [
                'luas' => 'sedang',
                'hasil' => 'rendah',
                'tanah' => 'tinggi',
                'output' => 'sedikit'
            ],

            // ================= LUAS KECIL =================
            [
                'luas' => 'kecil',
                'hasil' => 'tinggi',
                'tanah' => 'sedang',
                'output' => 'sedikit'
            ],
            [
                'luas' => 'kecil',
                'hasil' => 'sedang',
                'tanah' => 'sedang',
                'output' => 'sedikit'
            ],
            [
                'luas' => 'kecil',
                'hasil' => 'rendah',
                'tanah' => 'tinggi',
                'output' => 'sedikit'
            ],
        ];
    }
}
