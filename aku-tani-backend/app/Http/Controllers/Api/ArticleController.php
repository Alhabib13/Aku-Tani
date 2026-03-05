<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Article;
use App\Http\Resources\ArticleResource;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class ArticleController extends Controller
{
    // GET /api/articles
    public function index(Request $request)
    {
        $articles = Article::orderBy('created_at', 'desc')->get();
        return ArticleResource::collection($articles);
    }

    // GET /api/articles/{article}
    public function show(Article $article)
    {
        return new ArticleResource($article);
    }

    // POST /api/articles
    public function store(Request $request)
    {
        $data = $request->validate([
            'title'   => 'required|string|max:255',
            'excerpt' => 'nullable|string',
            'content' => 'nullable|string',
            'source'  => 'nullable|string',
            'image'   => 'nullable|image|max:5120' 
        ]);

        $article = new Article();
        $article->title = $data['title'];
        $article->excerpt = $data['excerpt'] ?? null;
        $article->content = $data['content'] ?? null;
        $article->source = $data['source'] ?? null;

        if ($request->hasFile('image')) {
            $path = $request->file('image')->store('articles', 'public'); 
            $article->image_url = $path;
        }

        $article->save();

        return (new ArticleResource($article))->response()->setStatusCode(201);
    }

    // PUT/PATCH /api/articles/{article}
    public function update(Request $request, Article $article)
    {
        $data = $request->validate([
            'title'   => 'sometimes|required|string|max:255',
            'excerpt' => 'nullable|string',
            'content' => 'nullable|string',
            'source'  => 'nullable|string',
            'image'   => 'nullable|image|max:5120'
        ]);

        if (isset($data['title'])) $article->title = $data['title'];
        if (array_key_exists('excerpt', $data)) $article->excerpt = $data['excerpt'];
        if (array_key_exists('content', $data)) $article->content = $data['content'];
        if (array_key_exists('source', $data)) $article->source = $data['source'];

        if ($request->hasFile('image')) {
            // hapus file lama jika ada
            if ($article->image_url && Storage::disk('public')->exists($article->image_url)) {
                Storage::disk('public')->delete($article->image_url);
            }
            $path = $request->file('image')->store('articles', 'public');
            $article->image_url = $path;
        }

        $article->save();

        return new ArticleResource($article);
    }

    // DELETE /api/articles/{article}
    public function destroy(Article $article)
    {
        if ($article->image_url && Storage::disk('public')->exists($article->image_url)) {
            Storage::disk('public')->delete($article->image_url);
        }

        $article->delete();

        return response()->json(['message' => 'Article deleted']);
    }
}
