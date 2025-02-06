package com.example.pruebalogin.ui

import android.Manifest
import android.os.Environment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.pruebalogin.model.Publication
import com.example.pruebalogin.viewmodel.PublicationState
import com.example.pruebalogin.viewmodel.PublicationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PublicationViewModel = viewModel()) {

    val publicationState by viewModel.publicationState.collectAsState()


    var showCreateDialog by remember { mutableStateOf(false) }


    var showCamera by remember { mutableStateOf(false) }


    val cameraPermissionState: PermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Publicaciones") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Publicación")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            if (showCamera) {
                if (cameraPermissionState.status.isGranted) {

                    CameraPreviewScreen(onCloseCamera = { showCamera = false })
                } else {

                    LaunchedEffect(Unit) {
                        cameraPermissionState.launchPermissionRequest()
                    }

                    TextButton(onClick = { showCamera = false }) {
                        Text("Regresar sin cámara")
                    }
                }
            } else {

                when (publicationState) {
                    is PublicationState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is PublicationState.Success -> {
                        val publications = (publicationState as PublicationState.Success).publications
                        PublicationList(publications = publications, viewModel = viewModel)
                    }
                    is PublicationState.Error -> {
                        val message = (publicationState as PublicationState.Error).message
                        Text(text = "Error: $message")
                    }
                }

                Button(
                    onClick = { showCamera = true },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text("Abrir Cámara")
                }

                if (showCreateDialog) {
                    CreatePublicationDialog(
                        onDismiss = { showCreateDialog = false },
                        onConfirm = { title, description ->
                            val newPublication = Publication(
                                id = 0,
                                title = title,
                                description = description,
                                createdAt = "",
                                updatedAt = ""
                            )
                            viewModel.createPublication(newPublication)
                            showCreateDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PublicationList(publications: List<Publication>, viewModel: PublicationViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(publications) { publication ->
            PublicationItem(publication = publication, viewModel = viewModel)
        }
    }
}

@Composable
fun PublicationItem(publication: Publication, viewModel: PublicationViewModel) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = publication.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Publicación")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Publicación")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = publication.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Creado: ${publication.createdAt}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Actualizado: ${publication.updatedAt}",
                style = MaterialTheme.typography.labelSmall
            )
        }

        if (showEditDialog) {
            EditPublicationDialog(
                publication = publication,
                onDismiss = { showEditDialog = false },
                onConfirm = { title, description ->
                    val updatedPublication = publication.copy(
                        title = title,
                        description = description
                    )
                    viewModel.updatePublication(publication.id, updatedPublication)
                    showEditDialog = false
                }
            )
        }

        // Diálogo de eliminación
        if (showDeleteDialog) {
            DeletePublicationDialog(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deletePublication(publication.id)
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
fun CreatePublicationDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Publicación") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    onConfirm(title, description)
                }
            }) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditPublicationDialog(
    publication: Publication,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(publication.title) }
    var description by remember { mutableStateOf(publication.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Publicación") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    onConfirm(title, description)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DeletePublicationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar Publicación") },
        text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreviewScreen(onCloseCamera: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val previewView = remember { PreviewView(context) }

    val imageCapture by remember {
        mutableStateOf(
            ImageCapture.Builder().build()
        )
    }


    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }


    var photoPath by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()

        // Use case de Preview
        val previewUseCase = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }


        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                previewUseCase,
                imageCapture
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }


    fun takePhoto() {

        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
            .format(System.currentTimeMillis())

        val photoFile = File(outputDir, "IMG_$timeStamp.jpg")


        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    photoPath = photoFile.absolutePath
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vista de Cámara") },
                navigationIcon = {
                    IconButton(onClick = onCloseCamera) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Cerrar Cámara"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { takePhoto() }) {
                Text("Foto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // mostrar nuestra fotico abajo
            photoPath?.let { path ->
                Text(
                    text = "Foto Guardada en: $path",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                )
                val painter = rememberAsyncImagePainter(path)
                Image(
                    painter = painter,
                    contentDescription = "Foto tomada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}
