//  CameraView.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 27.11.2024.
//

import SwiftUI
import PhotosUI
import AVFoundation


struct CameraView: View {
    
    @State private var isCameraAccessGranted = false
    @State private var isCameraActive = false

    var body: some View {
        VStack {
            if isCameraActive {
                CameraPreview()

                    .edgesIgnoringSafeArea(.all)

            } else {
                Text("Доступ к камере не предоставлен.")

                    .padding()

                Button("Запросить доступ к камере") {
                    requestCameraAccess()
                }
            }
        }

        .onAppear {
            checkCameraAccess()
        }
    }

    private func checkCameraAccess() {
        let status = AVCaptureDevice.authorizationStatus(for: .video)

        if status == .authorized {
            isCameraAccessGranted = true

            isCameraActive = true

        } else {
            isCameraAccessGranted = false
        }
    }

    private func requestCameraAccess() {
        AVCaptureDevice.requestAccess(for: .video) { granted in

            DispatchQueue.main.async {
                if granted {
                    isCameraAccessGranted = true

                    isCameraActive = true
                }
            }
        }
    }
}

struct CameraPreview: UIViewControllerRepresentable {
    func makeUIViewController(context _: Context) -> UIViewController {
        let viewController = UIViewController()

        let captureSession = AVCaptureSession()

        guard let camera = AVCaptureDevice.default(for: .video) else { return viewController }

        let input = try? AVCaptureDeviceInput(device: camera)

        if let input = input {
            captureSession.addInput(input)
        }

        let previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)

        previewLayer.frame = viewController.view.layer.bounds

        previewLayer.videoGravity = .resizeAspectFill

        viewController.view.layer.addSublayer(previewLayer)

        captureSession.startRunning()

        return viewController
    }

    func updateUIViewController(_: UIViewController, context _: Context) {}
}
