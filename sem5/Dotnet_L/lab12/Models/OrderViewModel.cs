using System.ComponentModel.DataAnnotations.Schema;

namespace lab12.Models
{

    [NotMapped]

    public class OrderViewModel
    {
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Country { get; set; }
        public string City { get; set; }
        public string ZipCode { get; set; }
        public string Address { get; set; }
        public string Phone { get; set; }
        public string PaymentMethod { get; set; }
        public decimal TotalCost { get; set; }
        public List<CartItem> CartItems { get; set; }
    }
}
