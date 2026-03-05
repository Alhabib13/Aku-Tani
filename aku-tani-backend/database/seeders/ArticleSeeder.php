<?php

namespace Database\Seeders;

use App\Models\Article;
use Illuminate\Database\Seeder;

class ArticleSeeder extends Seeder
{
    public function run(): void
    {
        Article::truncate();

        Article::insert([
            [
                'title'     => 'Cara Menanam Padi Supaya Panen Lebih Banyak',
                'excerpt'   => 'Langkah-langkah praktis menanam padi agar hasil panen meningkat.',
                'content'   => 'Isi artikel lengkap di sini...',
                'image_url' => 'https://contoh.com/images/padi.jpg',
                'source'    => 'Basis data ilmiah: Perpustakaan digital',
                'created_at'=> now(),
                'updated_at'=> now(),
            ],
            [
                'title'     => 'Cara Menanam Jagung Supaya Panen Memuaskan!',
                'excerpt'   => 'Tips memilih bibit jagung dan perawatan lahan.',
                'content'   => 'Isi artikel lengkap di sini...',
                'image_url' => 'https://contoh.com/images/jagung.jpg',
                'source'    => 'Basis data ilmiah: Perpustakaan digital',
                'created_at'=> now(),
                'updated_at'=> now(),
            ],
            [
                'title'     => 'Pupuk Buatan Sendiri Ramah Lingkungan',
                'excerpt'   => 'Panduan sederhana membuat pupuk organik cair di rumah.',
                'content'   => 'Isi artikel lengkap di sini...',
                'image_url' => 'https://contoh.com/images/pupuk-organik.jpg',
                'source'    => 'Basis data ilmiah: Perpustakaan digital',
                'created_at'=> now(),
                'updated_at'=> now(),
            ],
        ]);
    }
}
