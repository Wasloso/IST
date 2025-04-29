import models.Scene
import models.Vec3
import java.io.File
import javax.imageio.ImageIO
import javax.swing.SwingUtilities


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        return SwingUtilities.invokeLater {
            RendererApp()
        }
    }
    val inputFile = args[0]
    val scene = Scene.loadScene(inputFile)
    val outputImageFile = File(inputFile.replace(".scene", "_render.png"))
    val attenuation = Vec3(0.0,0.0,0.0)
    val phongShader = PhongShader(attenuation)
    val image = phongShader.shade(scene)

    try {
        ImageIO.write(image, "PNG", outputImageFile)
        println("Image saved as $outputImageFile")
    } catch (e: Exception) {
        println("Error saving image: ${e.message}")
    }
}