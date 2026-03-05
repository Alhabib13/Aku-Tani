<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;
use Illuminate\Support\Facades\Storage;

class ArticleResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        $image = $this->image_url;

        if ($image && preg_match('/^https?:\\/\\//', $image)) {
            $imageUrl = $image;
        } elseif ($image) {
            
            $imageUrl = url(Storage::url($image));
        } else {
            $imageUrl = null;
        }

        return [
            'id' => $this->id,
            'title' => $this->title,
            'excerpt' => $this->excerpt,
            'content' => $this->content,
            'image_url' => $imageUrl,
            'source' => $this->source,
            'created_at' => optional($this->created_at)->toDateTimeString(),
            'updated_at' => optional($this->updated_at)->toDateTimeString(),
        ];
    }
}
