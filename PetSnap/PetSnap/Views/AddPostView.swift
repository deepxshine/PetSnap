import PhotosUI
import SwiftUI

struct AddPostView: View {
    var body: some View {
        TabView {
            CameraView()

                .tabItem {
                    Label("Камера", systemImage: "camera")
                }

            GalleryView()

                .tabItem {
                    Label("Галерея", systemImage: "photo")
                }
        }
    }
}
