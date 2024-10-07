package com.suisei.restfetch.presentation.viewmodel

import android.Manifest
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.gun0912.tedpermission.coroutine.TedPermission
import com.suisei.restfetch.data.repository.QRScannerRepository
import com.suisei.restfetch.presentation.intent.QRScannerIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(private val qrScannerRepository: QRScannerRepository) :
    ViewModel() {
    val qrScannerState = qrScannerRepository.qrScannerState
    val scannedText = qrScannerRepository.scannedText
    val productNickname = qrScannerRepository.productNickname
    private lateinit
            var cameraProvider: ProcessCameraProvider

    private val scanIntent = Channel<QRScannerIntent>()

    init {
        handleIntent()
    }

    fun sendIntent(intent: QRScannerIntent) = viewModelScope.launch(Dispatchers.IO) {
        scanIntent.send(intent)
    }

    private fun handleIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            scanIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    QRScannerIntent.ShowQRRegistration -> setScanning(true)
                    QRScannerIntent.HideQRRegistration -> setScanning(false)

                }
            }
        }
    }

    private fun setScanning(state: Boolean) {
        qrScannerRepository.updateScanningState(state)
        Log.e("TEST", "qrScannerState : $state")
    }

    fun requestPermission() {
        CoroutineScope(Dispatchers.IO).launch {
            val permissionResult =
                TedPermission.create()
                    .setPermissions(
                        Manifest.permission.CAMERA
                    )
                    .check()
        }
    }

    fun startCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCamera(cameraProvider, previewView, lifecycleOwner)
        }, ContextCompat.getMainExecutor(previewView.context))
    }

    private fun bindCamera(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(previewView.context)) { imageProxy ->
                    processImageProxy(imageProxy)
                }
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)

        } catch (exc: Exception) {
            Log.e("QRCodeScanner", "Use case binding failed", exc)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val barcodeScanner = BarcodeScanning.getClient()

            viewModelScope.launch(Dispatchers.Main) {
                barcodeScanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            barcode.rawValue?.let {
                                Log.e("TEST", it)
                                if (scannedText.value != it) {
                                    viewModelScope.launch {
                                        qrScannerRepository.updateScanState(true)
                                    }
                                    qrScannerRepository.updateScannedText(it)
                                }

                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("QRCodeScanner", "Barcode scanning failed", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
        } else {
            imageProxy.close()
        }
    }

    fun stopCamera() {
        cameraProvider.unbindAll()
    }

    fun updateProductNickname(nickname: String) {
        qrScannerRepository.updateProductNickname(nickname)
    }
}