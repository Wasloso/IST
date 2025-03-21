using UnityEngine;

public class TilemapDebugger : MonoBehaviour
{
    private Vector3 lastPosition;

    private void Start()
    {
        lastPosition = transform.position;
    }

    private void Update()
    {
        if (transform.position != lastPosition)
        {
            Debug.Log($"Tilemap position changed from {lastPosition} to {transform.position}", gameObject);
            lastPosition = transform.position;
        }
    }
}