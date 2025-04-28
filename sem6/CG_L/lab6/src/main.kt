import models.Light
import models.Material
import models.Resolution
import models.Scene
import models.Sphere
import models.Vec3
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val kd: Vec3 = Vec3(0.5, 0.2, 0.2)  

    val ks: Vec3 = Vec3(0.3, 0.3, 0.3)  

    val ka: Vec3 = Vec3(0.3, 0.3, 0.3)  

    val selfLum = Vec3(0.0, 0.0, 0.0)

    val shininess: Double = 12.0
    val material: Material = Material(kd, ks, ka, selfLum, shininess)

    val center: Vec3 = Vec3(0.0, 5.0, 0.0)  

    val sphere: Sphere = Sphere(center, 10.0, material)

    val ambient: Vec3 = Vec3(100.0, 20.0, 30.0)

    val resolution: Resolution = Resolution(800, 800)

    val light1: Light = Light(Vec3(50.0, 50.0, -100.0), Vec3(100.0, 0.0, 0.0))  

    val light2: Light = Light(Vec3(-50.0, 0.0, -50.0), Vec3(0.0, 0.0, 255.0))

    val scene: Scene = Scene(ambient, resolution, listOf(light1, light2), sphere)

    val attenuation = Vec3(0.5, 0.0, 0.0)

    val shader = PhongShader(attenuation)
    val output = shader.shade(scene)

    val outputFile = File("output_image1.png")
    ImageIO.write(output, "PNG", outputFile)

    println("Image saved as output_image.png")
}
