<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Carbon\Carbon;

class WeatherController extends Controller
{
    // ================= CUACA SEKARANG =================
    public function current(Request $request)
    {
        $lat = $request->query('lat');
        $lon = $request->query('lon');

        if (!$lat || !$lon) {
            return response()->json(['message' => 'lat & lon wajib diisi'], 400);
        }

        $response = Http::get(
            config('services.tomorrow.base_url') . 'weather/realtime',
            [
                'location' => "$lat,$lon",
                'apikey'   => config('services.tomorrow.key'),
            ]
        );

        if (!$response->successful()) {
            return response()->json([
                'message' => 'Gagal mengambil cuaca',
                'error'   => $response->json()
            ], 500);
        }

        $values = $response->json()['data']['values'];

        return response()->json([
            'name' => 'Lokasi kamu',
            'main' => [
                'temp' => round($values['temperature'], 1),
                'humidity' => $values['humidity'],
            ],
            'weather' => [
                [
                    'main' => 'Clear',
                    'description' => 'Realtime',
                ]
            ],
        ]);
    }

    // ================= FORECAST 5 HARI =================
    public function forecast(Request $request)
    {
        $lat = $request->query('lat');
        $lon = $request->query('lon');

        if (!$lat || !$lon) {
            return response()->json(['message' => 'lat & lon wajib diisi'], 400);
        }

        $response = Http::get(
            config('services.tomorrow.base_url') . 'weather/forecast',
            [
                'location' => "$lat,$lon",
                'apikey'   => config('services.tomorrow.key'),
                'timesteps'=> '1d',
            ]
        );

        if (!$response->successful()) {
            return response()->json([
                'message' => 'Gagal mengambil forecast',
                'error'   => $response->json()
            ], 500);
        }

        $daily = $response->json()['timelines']['daily'] ?? [];

        $list = collect($daily)->take(5)->map(function ($day) {
            return [
                'dt_txt' => Carbon::parse($day['time'])->format('Y-m-d H:i:s'),
                'main' => [
                    'temp' => round($day['values']['temperatureAvg'], 1),
                ],
                'weather' => [
                    [
                        'main' => 'Clear',
                        'description' => 'Forecast',
                    ]
                ],
            ];
        });

        return response()->json(['list' => $list]);
    }
}
