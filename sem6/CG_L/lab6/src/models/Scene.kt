package models

import java.io.File

typealias Resolution = Pair<Int,Int>

class Scene(
    val ambient: Vec3,
    val resolution: Resolution,
    val lights: List<Light>,
    val sphere: Sphere
) {
    companion object {
        fun loadScene(filename: String): Scene {
            val lines = File(filename).readLines()
            var ambient: Vec3 = Vec3(0.0, 0.0, 0.0)
            var resolution: Resolution = Resolution(0, 0)
            val lights = mutableListOf<Light>()
            val spheres = mutableListOf<Sphere>()

            var currentMaterial: Material? = null
            var currentCenter: Vec3? = null
            var currentRadius: Double? = null

            for (line in lines) {
                val trimmedLine = line.trim()

                if (trimmedLine.isEmpty() || trimmedLine.startsWith("//")) continue

                when {
                    trimmedLine.startsWith("ambient") -> {
                        ambient = Vec3.fromString(trimmedLine)
                    }

                    trimmedLine.startsWith("resolution") -> {
                        resolution = trimmedLine.split(":")[1].trim().split(" ").map { it.toInt() }
                            .let { Resolution(it[0], it[1]) }
                    }

                    trimmedLine.startsWith("light") -> {
                        val values = trimmedLine.split(":")[1].trim().split(" ").map { it.toDouble() }
                        val position = Vec3(values[0], values[1], values[2])
                        val intensity = Vec3(values[3], values[4], values[5])
                        lights.add(Light(position, intensity))
                    }

                    trimmedLine.startsWith("sphere") -> {
                        currentCenter = null
                    }

                    trimmedLine.startsWith("material") -> {
                        currentMaterial = null
                    }

                    trimmedLine.startsWith("}") -> {
                        if (currentMaterial != null && currentCenter != null && currentRadius != null) {
                            spheres.add(Sphere(currentCenter, currentRadius, currentMaterial!!))
                        }
                    }

                    trimmedLine.startsWith("radius") -> {
                        currentRadius = trimmedLine.split(":")[1].trim().toDouble()
                    }

                    trimmedLine.startsWith("center") -> {
                        currentCenter = trimmedLine.split(":")[1].trim().split(" ").map { it.toDouble() }
                            .let { Vec3(it[0], it[1], it[2]) }
                    }

                    trimmedLine.startsWith("kd") -> {
                        val values = trimmedLine.split(":")[1].trim().split(" ").map { it.toDouble() }
                        val kd = Vec3(values[0], values[1], values[2])
                        currentMaterial = currentMaterial?.copy(kd = kd) ?: Material(
                            kd,
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            0.0
                        )
                    }

                    trimmedLine.startsWith("ks") -> {
                        val values = trimmedLine.split(":")[1].trim().split(" ").map { it.toDouble() }
                        val ks = Vec3(values[0], values[1], values[2])
                        currentMaterial = currentMaterial?.copy(ks = ks) ?: Material(
                            Vec3(0.0, 0.0, 0.0),
                            ks,
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            0.0
                        )
                    }

                    trimmedLine.startsWith("ka") -> {
                        val values = trimmedLine.split(":")[1].trim().split(" ").map { it.toDouble() }
                        val ka = Vec3(values[0], values[1], values[2])
                        currentMaterial = currentMaterial?.copy(ka = ka) ?: Material(
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            ka,
                            Vec3(0.0, 0.0, 0.0),
                            0.0
                        )
                    }

                    trimmedLine.startsWith("selfLuminance") -> {
                        val values = trimmedLine.split(":")[1].trim().split(" ").map { it.toDouble() }
                        val selfLuminance = Vec3(values[0], values[1], values[2])
                        currentMaterial = currentMaterial?.copy(selfLuminance = selfLuminance) ?: Material(
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            selfLuminance,
                            0.0
                        )
                    }

                    trimmedLine.startsWith("shininess") -> {
                        val shininess = trimmedLine.split(":")[1].trim().toDouble()
                        currentMaterial = currentMaterial?.copy(shininess = shininess) ?: Material(
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            Vec3(0.0, 0.0, 0.0),
                            shininess
                        )
                    }
                }
            }
            return Scene(
                ambient,
                resolution,
                lights,
                spheres.firstOrNull() ?: Sphere(
                    Vec3(0.0, 0.0, 0.0),
                    0.0,
                    currentMaterial ?: Material(
                        Vec3(0.0, 0.0, 0.0),
                        Vec3(0.0, 0.0, 0.0),
                        Vec3(0.0, 0.0, 0.0),
                        Vec3(0.0, 0.0, 0.0),
                        0.0
                    )
                )
            )
        }
    }

    fun saveToString(): String {
        val sb = StringBuilder()
        sb.append("ambient: ${ambient.x} ${ambient.y} ${ambient.z}\n")
        sb.append("resolution: ${resolution.first} ${resolution.second}\n")

        if (lights.isNotEmpty()) {
            sb.append("// x, y, z r, g, b\n")
            lights.forEach { light ->
                sb.append("light: ${light.position.x} ${light.position.y} ${light.position.z} ")
                sb.append("${(light.intensity.x).toInt()} ${(light.intensity.y).toInt()} ${(light.intensity.z).toInt()}\n")
            }
        }

        sb.append("\nsphere {\n")
        sb.append("    center: ${sphere.center.x} ${sphere.center.y} ${sphere.center.z}\n")
        sb.append("    radius: ${sphere.radius}\n")
        sb.append("    material {\n")
        with(sphere.material) {
            sb.append("        kd: ${kd.x} ${kd.y} ${kd.z}\n")
            sb.append("        ks: ${ks.x} ${ks.y} ${ks.z}\n")
            sb.append("        ka: ${ka.x} ${ka.y} ${ka.z}\n")
            sb.append("        selfLuminance: ${selfLuminance.x} ${selfLuminance.y} ${selfLuminance.z}\n")
            sb.append("        shininess: $shininess\n")
        }
        sb.append("    }\n")
        sb.append("}")

        return sb.toString()
    }

    fun saveToFile(filename: String) {
        File(filename).writeText(saveToString())
    }
}