<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('welcome');
});

Route::get('/login', function () {
    return response('Halaman login belum dibuat. Ini hanya placeholder.', 200);
})->name('login');
