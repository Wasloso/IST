package models

import java.io.File

typealias Resolution = Pair<Int,Int>

class Scene(
    val ambient: Vec3,
    val resolution: Resolution,
    val lights: List<Light>,
    val sphere: Sphere
) {
//    companion object {
//        fun loadScene(filename: String): Scene {
                //TODO
//
//        }
//    }
}