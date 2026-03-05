<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;

class PanenController extends Controller
{
    public function predict(Request $request)
    {
        $data = $request->validate([
            'luas_lahan'     => 'required|numeric',
            'curah_hujan'    => 'required|numeric',
            'kualitas_tanah' => 'required|numeric',
            'jumlah_pupuk'   => 'required|numeric',
            'lama_tanam'     => 'required|numeric',
        ]);

        $response = Http::post(
            'http://127.0.0.1:5000/predict-panen',
            $data
        );

        if (!$response->successful()) {
            return response()->json([
                'message' => 'Gagal memprediksi panen'
            ], 500);
        }

        return response()->json($response->json());
    }
}
