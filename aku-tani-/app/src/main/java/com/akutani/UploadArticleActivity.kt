package com.akutani

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.akutani.network.Api.ArticleApiClient
import com.akutani.utils.uriToMultipart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UploadArticleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                UploadArticleScreen()
            }
        }
    }
}

@Composable
fun UploadArticleScreen() {
    val context = LocalContext.current
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var excerpt by remember { mutableStateOf(TextFieldValue("")) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var loading by remember { mutableStateOf(false) }

    // launcher for picking image (must be inside @Composable)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (selectedUri != null) {
                AsyncImage(
                    model = selectedUri,
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "Preview gambar (ketuk tombol pilih gambar)")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = excerpt,
            onValueChange = { excerpt = it },
            label = { Text("Excerpt / Ringkasan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Pilih Gambar")
            }

            Button(onClick = {
                if (title.text.isBlank()) {
                    Toast.makeText(context, "Isi judul dulu", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Start upload (uses activity lifecycleScope)
                val activity = (context as? ComponentActivity)
                if (activity == null) {
                    Toast.makeText(context, "Context tidak didukung untuk upload", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                activity.lifecycleScope.launch {
                    loading = true
                    try {
                        uploadArticle(
                            context = activity,
                            title = title.text,
                            excerpt = excerpt.text.ifBlank { null },
                            imageUri = selectedUri,
                            onStart = { /* optional */ },
                            onFinish = { /* optional */ }
                        )
                    } finally {
                        loading = false
                    }
                }
            }) {
                Text(if (loading) "Mengunggah..." else "Upload Artikel")
            }
        }
    }
}

suspend fun uploadArticle(
    context: Context,
    title: String,
    excerpt: String?,
    imageUri: Uri?,
    onStart: () -> Unit = {},
    onFinish: () -> Unit = {}
) {
    // only call from an Activity context
    val activity = context as? ComponentActivity
    if (activity == null) {
        Toast.makeText(context, "Context bukan Activity", Toast.LENGTH_SHORT).show()
        return
    }

    onStart()

    try {
        val titleRb = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val excerptRb = excerpt?.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageUri?.let { uriToMultipart(context, it, "image") }

        val resp = ArticleApiClient.api.createArticle(
            title = titleRb,
            excerpt = excerptRb,
            content = null,
            image = imagePart
        )

        if (resp.isSuccessful) {
            Toast.makeText(context, "Artikel berhasil dibuat", Toast.LENGTH_LONG).show()
            activity.finish()
        } else {
            val err = resp.errorBody()?.string()
            Toast.makeText(context, "Gagal: ${resp.code()} : $err", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        onFinish()
    }
}
