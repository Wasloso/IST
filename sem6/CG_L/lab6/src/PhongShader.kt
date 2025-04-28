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
        val imageOutput = BufferedImage(width,height, BufferedImage.TYPE_INT_RGB)
        val sphere: Sphere = scene.sphere
        val material = sphere.material
        val viewDir = Vec3(0.0,0.0,1.0)
        val r = sphere.radius
        val rPow = r*r
        for(i in 0 until height){
            for(j in 0 until width){
                val (x,y) = toWorldPosition(r, i, height, j, width)
                val color = calculatePixel(x, y, rPow, material,scene.ambient, scene.lights,viewDir)
                imageOutput.setRGB(j,i,color)
            }
        }
        return imageOutput
    }

    private fun calculatePixel(
        x: Double,
        y: Double,
        rPow: Double,
        material: Material,
        ambient: Vec3,
        lights: List<Light>,
        viewDir: Vec3
    ): Int {
        if (x.pow(2) + y.pow(2) > rPow) {
            return Color.BLACK.rgb
        }
        val z = sqrt(rPow - x.pow(2) - y.pow(2))
        val point = Vec3(x, y, z)
        val normal = point.normalize()
        var color: Vec3 = material.selfLuminance + material.ka * ambient
        lights.forEach { light ->
            val lightDir = (light.position - point).normalize()
            val reflectDir = (normal * 2.0 * normal.dot(lightDir) - lightDir)
            val diffuse = material.kd * light.intensity * max(normal.dot(lightDir), 0.0)
            val specular = material.ks * light.intensity * max(viewDir.dot(reflectDir), 0.0).pow(material.shininess)
            color += (diffuse + specular) * fAtt((light.position - point).length())
        }
        return toColorInt(color)
    }

    private fun toWorldPosition(
        r: Double,
        i: Int,
        height: Int,
        j: Int,
        width: Int
    ): Pair<Double, Double> {
        val y = r * (1 - 2.0 * i / height)
        val x = r * (2.0 * j / width - 1)
        return Pair(x, y)
    }

    fun toColorInt(color: Vec3): Int {
        return color
            .clamp(255.0)
            .let { (r, g, b) -> Color((r).toInt(), (g).toInt(), (b).toInt()).rgb }
    }




}