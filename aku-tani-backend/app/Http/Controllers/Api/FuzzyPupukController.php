<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Services\Fuzzy\TsukamotoService;

class FuzzyPupukController extends Controller
{
    public function hitung(Request $request, TsukamotoService $service)
    {
        $data = $request->validate([
            'tanaman' => 'required|string',
            'luas'    => 'required|numeric',
            'hasil'   => 'required|numeric',
            'tanah'   => 'required|numeric',
            'pupuk'   => 'required|string',
        ]);

        $jumlah = (float) $service->hitung(
            $data['luas'],
            $data['hasil'],
            $data['tanah'],
            $data['tanaman'],
            $data['pupuk']
        );

        return response()->json([
            'status'  => true,
            'data' => [
                'jumlah' => $jumlah,
                'satuan' => 'kg'
            ]
        ]);
    }
}
