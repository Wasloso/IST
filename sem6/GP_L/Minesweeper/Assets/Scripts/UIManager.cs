using UnityEngine;

public class UIManager : MonoBehaviour
{
    public GameObject settingsScreen;
    public GameObject gameScreen;

    public GameObject winScreen;
    public GameObject loseScreen;
    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        ShowSettings();
        HideGameScreen();
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void ShowGameScreen()
    {
        gameScreen.SetActive(true);
    }

    public void HideGameScreen()
    {
        gameScreen.SetActive(false);
    }
    

    public void HideSettings()
    {
         settingsScreen.SetActive(false);
    }

    public void ShowSettings()
    {
        settingsScreen.SetActive(true);
    }

    public void ShowWinScreen()
    {
      winScreen.SetActive(true);    
    }

    public void HideWinScreen()
    { 
        winScreen.SetActive(false);
    }

    public void ShowLoseScreen()
    {
        loseScreen.SetActive(true);
    }

    public void HideLoseScreen()
    {
        loseScreen.SetActive(false);
    }
    
    
}
