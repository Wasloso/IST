import models.Light
import models.Material
import models.Scene
import models.Sphere
import models.Vec3
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


class PhongShader(attenuation: Vec3) {
    private val c0: Double = attenuation.x
    private val c1: Double = attenuation.y
    private val c2: Double = attenuation.z

    fun fAtt(r: Double): Double {
        val denominator = c2 * r * r + c1 * r + c0
        if (denominator == 0.0) return 1.0
        return min(1.0 / denominator, 1.0)
    }

    fun shade(scene: Scene): BufferedImage {
        val (width, height) = scene.resolution

        val useParallel = width * height >= 1_000_000

        return if (useParallel) {
            // according to tests renders 2x faster when image is big enough
            shadeParallel(scene)
        } else {
            shadeSerial(scene)
        }
    }
    fun shadeSerial(scene: Scene): BufferedImage {
        val (width, height) = scene.resolution
        val imageOutput = BufferedImage(width,height, BufferedImage.TYPE_INT_RGB)
        val sphere: Sphere = scene.sphere
        val invWidth = 1.0 / width
        val invHeight = 1.0 / height
        val viewDir = Vec3(0.0,0.0,1.0)
        val r = sphere.radius
        for(i in 0 until height){
            val y = r*(1-2.0*i*invHeight)
            for(j in 0 until width){
                val x = r*(2.0*j*invWidth-1)
                val color = calculatePixel(x, y, sphere, scene.ambient,scene.lights,viewDir)
                imageOutput.setRGB(j,i,color)
            }
        }
        return imageOutput
    }

    private fun calculatePixel(
        x: Double,
        y: Double,
        sphere: Sphere,
        ambient: Vec3,
        lights: List<Light>,
        viewDir: Vec3
    ): Int {
        val rPow = sphere.radius * sphere.radius
        val x2 = x.pow(2)
        val y2 = y.pow(2)
        if (x2 + y2 > rPow) return Color.BLACK.rgb

        val z = sqrt(rPow - x2 - y2)
        val localPoint = Vec3(x, y, z)
        val point = localPoint + sphere.center
        val normal = (point - sphere.center).normalize()
        var color: Vec3 = sphere.material.selfLuminance + sphere.material.ka * ambient

        lights.forEach { light ->
            val lightDir = (light.position - point).normalize()
            val ndotl = normal.dot(lightDir)
            if (ndotl < 0.0) return@forEach
            val reflectDir = (normal * 2.0 * ndotl - lightDir)
            val diffuse = sphere.material.kd * light.intensity * ndotl
            val specular = sphere.material.ks * light.intensity *
                    max(viewDir.dot(reflectDir), 0.0).pow(sphere.material.shininess)
            color += (diffuse + specular) * fAtt((light.position - point).length())
        }
        return toColorInt(color)
    }


    fun toColorInt(color: Vec3): Int {
        return color
            .clamp(255.0)
            .let { (r, g, b) -> Color((r).toInt(), (g).toInt(), (b).toInt()).rgb }
    }

    fun shadeParallel(scene: Scene): BufferedImage {
        val (width, height) = scene.resolution
        val imageOutput = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val sphere: Sphere = scene.sphere
        val material = sphere.material
        val invWidth = 1.0 / width
        val invHeight = 1.0 / height
        val viewDir = Vec3(0.0, 0.0, 1.0)
        val r = sphere.radius
        val rPow = r * r

        val chunkSize = 50

        val chunkImages = mutableListOf<BufferedImage>()
        for (startRow in 0 until height step chunkSize) {
            val chunkHeight = minOf(chunkSize, height - startRow)
            val chunkImage = BufferedImage(width, chunkHeight, BufferedImage.TYPE_INT_RGB)
            chunkImages.add(chunkImage)
        }
        (0 until height step chunkSize).toList().parallelStream().forEach { startRow ->
            val endRow = minOf(startRow + chunkSize, height)
            val chunkImage = chunkImages[startRow / chunkSize]

            for (i in startRow until endRow) {
                val y = r * (1 - 2.0 * i * invHeight)
                for (j in 0 until width) {
                    val x = r * (2.0 * j * invWidth - 1)
                    val color = calculatePixel(x, y, sphere, scene.ambient,scene.lights,viewDir)
                    chunkImage.setRGB(j, i - startRow, color)
                }
            }
        }

        for (index in chunkImages.indices) {
            val chunkImage = chunkImages[index]
            val yOffset = index * chunkSize
            for (i in 0 until chunkImage.height) {
                for (j in 0 until chunkImage.width) {
                    val color = chunkImage.getRGB(j, i)
                    imageOutput.setRGB(j, i + yOffset, color)
                }
            }
        }

        return imageOutput
    }





}