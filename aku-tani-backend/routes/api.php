<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\WeatherController;
use App\Http\Controllers\Api\ArticleController;
use App\Http\Controllers\Api\ProfileController;
use App\Http\Controllers\Api\FuzzyPupukController; 
use App\Http\Controllers\Api\PanenController;

Route::get('/test', function () {
    return response()->json([
        'message' => 'API Aku Tani OK'
    ]);
});

Route::post('/register', [AuthController::class, 'register']);
Route::post('/login',    [AuthController::class, 'login']);

Route::get('/weather', [WeatherController::class, 'current']);
Route::get('/weather-forecast', [WeatherController::class, 'forecast']);

Route::get('/articles', [ArticleController::class, 'index']);
Route::get('/articles/{article}', [ArticleController::class, 'show']);

Route::post('/fuzzy/pupuk', [FuzzyPupukController::class, 'hitung']); 
Route::post('/panen/predict', [PanenController::class, 'predict']);

Route::middleware('auth:sanctum')->group(function () {

    Route::get('/profile',  [ProfileController::class, 'show']);
    Route::post('/profile', [ProfileController::class, 'update']);
    Route::post('/change-password', [ProfileController::class, 'changePassword']);

    Route::post('/logout', [AuthController::class, 'logout']);

    Route::post('/articles', [ArticleController::class, 'store']);
    Route::match(['put', 'patch'], '/articles/{article}', [ArticleController::class, 'update']);
    Route::delete('/articles/{article}', [ArticleController::class, 'destroy']);
});
