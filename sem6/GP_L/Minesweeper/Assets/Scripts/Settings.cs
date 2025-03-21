using System;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class Settings : MonoBehaviour
{
    public Slider sizeSlider;
    public Slider mineSlider;
    public TMP_Text mineCountText;
    public TMP_Text sizeText;
    public Button startButton;
    public Game game;

    void Start()
    {
        sizeSlider.onValueChanged.AddListener(UpdateSize);
        mineSlider.onValueChanged.AddListener(UpdateMine);
        startButton.onClick.AddListener(StartGame);
        mineSlider.maxValue = sizeSlider.value*sizeSlider.value-1;
        mineCountText.text = Mathf.RoundToInt(mineSlider.value).ToString() + " mines";
        sizeText.text = Mathf.RoundToInt(sizeSlider.value).ToString() + "x" +  Mathf.RoundToInt(sizeSlider.value).ToString();

    }

    private void StartGame()
    {
        game.gameObject.SetActive(true);
        int gridSize = (int)sizeSlider.value;
        int mines = (int)mineSlider.value;
        System.Console.WriteLine(gridSize);
        game.NewGame(gridSize,gridSize,mines);
        game.uiManager.HideSettings();
    }

    private void UpdateMine(float value)
    {
        int count = (int)mineSlider.value;
        UpdateMinesCountText(count);
    }

    private void UpdateSize(float value)
    {
        int gridSize = Mathf.RoundToInt(value);
        UpdateSizeText(gridSize);
        mineSlider.maxValue = gridSize*gridSize-1;
        mineSlider.value = Mathf.Min(mineSlider.value, mineSlider.maxValue);
    }

    private void UpdateSizeText(int size)
    {
        sizeText.text = size + "x" + size;
        
    }

    private void UpdateMinesCountText(int mines)
    {
        mineCountText.text = Mathf.RoundToInt(mines) + " mines";
    }

    
}
