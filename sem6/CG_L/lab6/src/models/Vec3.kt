package models

import kotlin.math.sqrt

data class Vec3(val x: Double, val y: Double, val z: Double){
    operator fun plus(v: Vec3): Vec3 = Vec3(x+v.x,y+v.y,z+v.z)
    operator fun minus(v: Vec3): Vec3 = Vec3(x-v.x,y-v.y,z-v.z)
    operator fun times(scalar: Double): Vec3 = Vec3(x*scalar,y*scalar,z*scalar)
    operator fun times(other: Vec3) = Vec3(x*other.x,y*other.y,z*other.z)
    fun dot(other: Vec3): Double = x*other.x+y*other.y+z*other.z
    fun normalize(): Vec3 {
        val len = length()
        return Vec3(x/len,y/len,z/len)
    }
    fun length() = sqrt(this.dot(this))
    fun clamp(max: Double = 1.0) = Vec3(x.coerceIn(0.0, max), y.coerceIn(0.0, max), z.coerceIn(0.0, max))
}
