import models.*
import java.awt.*
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class RendererApp : JFrame("Sphere Renderer") {
    private val phongShader = PhongShader(Vec3(0.0,0.0,0.0))
    private val lightPanel = JPanel().apply { layout = BoxLayout(this, BoxLayout.Y_AXIS) }
    private val lightEntries = mutableListOf<LightEntry>()
    private lateinit var ambientFields: Triple<JTextField, JTextField, JTextField>
    private lateinit var resolutionField: JTextField
    private lateinit var centerFields: Triple<JTextField, JTextField, JTextField>
    private lateinit var radiusField: JTextField
    private lateinit var kdFields: Triple<JTextField, JTextField, JTextField>
    private lateinit var ksFields: Triple<JTextField, JTextField, JTextField>
    private lateinit var kaFields: Triple<JTextField, JTextField, JTextField>
    private lateinit var selfLumFields: Triple<JTextField, JTextField, JTextField>
    private lateinit var shininessField: JTextField

    init {
        configureWindow()
        setupUI()
    }

    private fun configureWindow() {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
    }

    private fun setupUI() {
        val scenePanel = createScenePanel()
        val lightsContainer = createLightsContainer()
        val buttonPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(createRenderButton())
            add(createSaveButton())
            add(createLoadButton())
        }
        val mainPanel = JPanel(BorderLayout()).apply {
            add(scenePanel, BorderLayout.NORTH)
            add(lightsContainer, BorderLayout.CENTER)
            add(buttonPanel, BorderLayout.SOUTH)
        }

        add(JScrollPane(mainPanel), BorderLayout.CENTER)
        pack()
        isVisible = true
    }
    private fun createLoadButton(): JButton {
        return JButton("Load Scene").apply {
            addActionListener {
                loadSceneFromFile()
            }
        }
    }


    private fun createSaveButton(): JButton {
        return JButton("Save Scene").apply {
            addActionListener {
                saveSceneToFile()
            }
        }
    }
    private fun createScenePanel(): JPanel {
        return JPanel(GridLayout(0, 1)).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            ambientFields = createVectorInput("Ambient", this, "100", "0", "50", isRgb = true)
            resolutionField = createNumberInput("Resolution (n x n)", this, "512", 5)

            centerFields = createVectorInput("Sphere Center", this)
            radiusField = createNumberInput("Radius", this, "5.0")

            kdFields = createVectorInput("Kd (Diffuse)", this)
            ksFields = createVectorInput("Ks (Specular)", this)
            kaFields = createVectorInput("Ka (Ambient)", this)
            selfLumFields = createVectorInput("Self Luminance", this)
            shininessField = createNumberInput("Shininess", this, "25.0")
        }
    }

    private fun createLightsContainer(): JPanel {
        return JPanel(BorderLayout()).apply {
            border = BorderFactory.createTitledBorder("Lights")
            add(JScrollPane(lightPanel), BorderLayout.CENTER)

            val addLightBtn = JButton("Add Light").apply {
                addActionListener { addLight() }
            }
            add(addLightBtn, BorderLayout.SOUTH)
        }.also {
            addLight()
        }
    }

    private fun createRenderButton(): JButton {
        return JButton("Render").apply {
            addActionListener {
                val scene = createSceneFromInputs()
                displayRenderedImage(scene)
            }
        }
    }

    private fun createSceneFromInputs(): Scene {
        val ambient = getVec3(ambientFields)
        val resolution = resolutionField.text.toInt()
        val center = getVec3(centerFields)
        val radius = radiusField.text.toDouble()

        val material = Material(
            kd = getVec3(kdFields),
            ks = getVec3(ksFields),
            ka = getVec3(kaFields),
            selfLuminance = getVec3(selfLumFields),
            shininess = shininessField.text.toDouble()
        )

        val sphere = Sphere(center, radius, material)
        val lights = lightEntries.map { it.toLight() }

        return Scene(
            ambient = ambient,
            resolution = Pair(resolution, resolution),
            lights = lights,
            sphere = sphere
        )
    }

    private fun displayRenderedImage(scene: Scene) {
        val img = phongShader.shade(scene)
        val size = scene.resolution.first.coerceIn(512, 750)

        val frame = JFrame("Rendered Image").apply {
            layout = BorderLayout()
            setSize(size, size + 50)
            isResizable = false
        }

        val imageIcon = ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH))
        val imageLabel = JLabel(imageIcon)

        val saveButton = JButton("Save as PNG").apply {
            addActionListener {
                val fileChooser = JFileChooser().apply {
                    fileFilter = FileNameExtensionFilter("PNG Images", "png")
                    selectedFile = File("newrender.png")
                    currentDirectory = File("./resources").apply { if (!exists()) mkdir() }
                }
                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    val file = fileChooser.selectedFile
                    val finalFile = if (!file.path.endsWith(".png")) File("${file.path}.png") else file
                    try {
                        javax.imageio.ImageIO.write(img, "png", finalFile)
                        JOptionPane.showMessageDialog(frame, "Image saved to ${finalFile.path}", "Success", JOptionPane.INFORMATION_MESSAGE)
                    } catch (e: Exception) {
                        JOptionPane.showMessageDialog(frame, "Failed to save image: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
                    }
                }
            }
        }

        val panel = JPanel(BorderLayout()).apply {
            add(imageLabel, BorderLayout.CENTER)
            add(saveButton, BorderLayout.SOUTH)
        }

        frame.add(panel)
        frame.pack()
        frame.isVisible = true
    }

    private fun createVectorInput(
        label: String,
        parent: Container,
        defValue1: String = "0.5",
        defValue2: String = "0.5",
        defValue3: String = "0.5",
        isRgb: Boolean = false
    ): Triple<JTextField, JTextField, JTextField> {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))
        panel.add(JLabel("$label:"))

        val x = JTextField(defValue1, 4)
        val y = JTextField(defValue2, 4)
        val z = JTextField(defValue3, 4)

        panel.add(JLabel(if (isRgb) "r" else "x")); panel.add(x)
        panel.add(JLabel(if (isRgb) "g" else "y")); panel.add(y)
        panel.add(JLabel(if (isRgb) "b" else "z")); panel.add(z)

        parent.add(panel)
        return Triple(x, y, z)
    }

    private fun createNumberInput(label: String, parent: Container, defaultValue: String, columns: Int = 6): JTextField {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))
        panel.add(JLabel("$label:"))
        val field = JTextField(defaultValue, columns)
        panel.add(field)
        parent.add(panel)
        return field
    }

    private fun addLight(): LightEntry {
        return LightEntry().apply {
            lightEntries.add(this)
            lightPanel.add(panel)
            lightPanel.revalidate()
            pack()
        }
    }

    private fun getVec3(fields: Triple<JTextField, JTextField, JTextField>): Vec3 {
        val (x, y, z) = fields
        return Vec3(
            x.text.toDoubleOrNull() ?: 0.0,
            y.text.toDoubleOrNull() ?: 0.0,
            z.text.toDoubleOrNull() ?: 0.0
        )
    }

    private fun saveSceneToFile() {
        val fileChooser = JFileChooser().apply {
            fileFilter = FileNameExtensionFilter("Scene Files", "scene")
            selectedFile = File("newscene.scene")
            currentDirectory = File("./resources").apply { if (!exists()) mkdir() }
        }

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            val filePath = if (!file.path.endsWith(".scene")) File("${file.path}.scene") else file

            try {
                val scene = createSceneFromInputs()
                scene.saveToFile(filePath.path)
                JOptionPane.showMessageDialog(this, "Scene saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE)
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(this, "Error saving scene: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
            }
        }
    }
    private fun loadSceneFromFile() {
        val fileChooser = JFileChooser().apply {
            currentDirectory = File("./resources").apply { if (!exists()) mkdir() }
            fileFilter = FileNameExtensionFilter("Scene Files", "scene")
        }

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            println(fileChooser.selectedFile.name)
            try {
                val scene = Scene.loadScene(fileChooser.selectedFile.path)
                applySceneToUI(scene)
                JOptionPane.showMessageDialog(
                    this,
                    "Scene loaded successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error loading scene: ${e.message}",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }
    private fun applySceneToUI(scene: Scene) {
        ambientFields.first.text = scene.ambient.x.toString()
        ambientFields.second.text = scene.ambient.y.toString()
        ambientFields.third.text = scene.ambient.z.toString()

        
        resolutionField.text = scene.resolution.first.toString()

        
        centerFields.first.text = scene.sphere.center.x.toString()
        centerFields.second.text = scene.sphere.center.y.toString()
        centerFields.third.text = scene.sphere.center.z.toString()
        radiusField.text = scene.sphere.radius.toString()

        
        with(scene.sphere.material) {
            kdFields.first.text = kd.x.toString()
            kdFields.second.text = kd.y.toString()
            kdFields.third.text = kd.z.toString()

            ksFields.first.text = ks.x.toString()
            ksFields.second.text = ks.y.toString()
            ksFields.third.text = ks.z.toString()

            kaFields.first.text = ka.x.toString()
            kaFields.second.text = ka.y.toString()
            kaFields.third.text = ka.z.toString()

            selfLumFields.first.text = selfLuminance.x.toString()
            selfLumFields.second.text = selfLuminance.y.toString()
            selfLumFields.third.text = selfLuminance.z.toString()

            shininessField.text = shininess.toString()
        }

        lightEntries.clear()
        lightPanel.removeAll()
        scene.lights.forEach { light ->
            addLight().apply {
                (panel.components[1] as JTextField).text = light.position.x.toString()
                (panel.components[2] as JTextField).text = light.position.y.toString()
                (panel.components[3] as JTextField).text = light.position.z.toString()

                (panel.components[5] as JTextField).text = (light.intensity.x).toInt().toString()
                (panel.components[6] as JTextField).text = (light.intensity.y).toInt().toString()
                (panel.components[7] as JTextField).text = (light.intensity.z).toInt().toString()
            }
        }
        lightPanel.revalidate()
        lightPanel.repaint()
    }

    private inner class LightEntry {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            border = BorderFactory.createLineBorder(Color.GRAY)
            addPositionFields()
            addColorFields()
            addRemoveButton()
        }

        private fun JPanel.addPositionFields() {
            add(JLabel("Pos:"))
            add(JTextField(randomPosition(-100, 100), 4))
            add(JTextField(randomPosition(-100, 100), 4))
            add(JTextField(randomPosition(10, 100), 4))
        }

        private fun JPanel.addColorFields() {
            add(JLabel("Color:"))
            add(JTextField(randomColor(), 4))
            add(JTextField(randomColor(), 4))
            add(JTextField(randomColor(), 4))
        }

        private fun JPanel.addRemoveButton() {
            add(JButton("Remove").apply {
                addActionListener { removeLightEntry() }
            })
        }



        private fun removeLightEntry() {
            lightEntries.remove(this)
            lightPanel.remove(panel)
            lightPanel.revalidate()
            lightPanel.repaint()
            pack()
        }

        fun toLight(): Light {
            val components = panel.components
            return Light(
                position = Vec3(
                    (components[1] as JTextField).text.toDoubleOrNull() ?: 0.0,
                    (components[2] as JTextField).text.toDoubleOrNull() ?: 0.0,
                    (components[3] as JTextField).text.toDoubleOrNull() ?: 10.0
                ),
                intensity = Vec3((components[5] as JTextField).text.toDoubleOrNull() ?: 255.0,
                    (components[6] as JTextField).text.toDoubleOrNull() ?: 255.0,
                    (components[7] as JTextField).text.toDoubleOrNull() ?: 255.0
                )
            )
        }



        private fun randomPosition(min: Int, max: Int) = Random.nextInt(min, max).toString()
        private fun randomColor() = Random.nextInt(0, 256).toString()
    }
}

fun main() = SwingUtilities.invokeLater { RendererApp() }