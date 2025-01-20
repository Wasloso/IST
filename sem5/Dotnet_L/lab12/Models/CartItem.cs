namespace lab12.Models
{
    public class CartItem
    {
         required public Article Article { get; set; }
        public int Quantity { get; set; }
    }
}
