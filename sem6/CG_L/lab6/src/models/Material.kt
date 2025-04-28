package models

data class Material(
    /**Diffuse R,G,B*/
    val kd: Vec3,
    /**Specular R,G,B*/
    val ks: Vec3,
    /**Ambient R,G,B*/
    val ka: Vec3,
    val selfLuminance: Vec3,
    val shininess: Double,
)
